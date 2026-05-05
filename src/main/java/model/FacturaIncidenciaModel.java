package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import util.ApplicationException;
import util.Database;

public class FacturaIncidenciaModel {

    private static final DateTimeFormatter NUMERO_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final Database db = new Database();

    public FacturaIncidenciaModel() {
        asegurarTablaFactura();
    }

    public int loginOperador(String identificador) {
        String sql = "SELECT id_persona, tipo FROM Persona WHERE email=? OR dni=?";

        List<Object[]> rows = db.executeQueryArray(sql, identificador, identificador);

        if (rows.isEmpty()) {
            throw new ApplicationException("Usuario no encontrado");
        }

        String tipo = String.valueOf(rows.get(0)[1]);
        if (!"OPERADOR".equalsIgnoreCase(tipo)) {
            throw new ApplicationException("Solo los operadores pueden emitir facturas");
        }

        return ((Number) rows.get(0)[0]).intValue();
    }

    public List<FacturaIncidenciaDTO> getIncidenciasCerradasSinFactura() {
        String sql = ""
                + "SELECT i.id_incidencia AS id, "
                + "       i.tipo, "
                + "       i.descripcion, "
                + "       i.fecha_hora AS fechaHoraRegistro, "
                + "       i.estado, "
                + "       COALESCE(t.nombre || ' ' || t.apellidos, t.usuario, 'Sin tecnico') AS tecnico, "
                + "       i.tiempo_real AS tiempoReal, "
                + "       COALESCE(t.precio_hora, 0) AS precioHora, "
                + "       COALESCE(i.coste_materiales, 0) AS costeMateriales, "
                + "       i.descripcion_materiales AS descripcionMateriales, "
                + "       i.trabajos_realizados AS trabajosRealizados, "
                + "       COALESCE(i.coste_total, i.coste, 0) AS costeTotal "
                + "FROM Incidencia i "
                + "LEFT JOIN Persona t ON i.fk_tecnico = t.id_persona "
                + "LEFT JOIN Factura f ON f.fk_incidencia = i.id_incidencia "
                + "WHERE i.estado = 'CERRADA' AND f.id_factura IS NULL "
                + "ORDER BY i.fecha_hora DESC";

        return db.executeQueryPojo(FacturaIncidenciaDTO.class, sql);
    }

    public FacturaDTO generarFactura(long idIncidencia, int idOperador) {
        if (existeFactura(idIncidencia)) {
            throw new ApplicationException("La incidencia seleccionada ya tiene una factura asociada");
        }

        FacturaIncidenciaDTO incidencia = getIncidenciaCerrada(idIncidencia);
        String emisor = getNombreOperador(idOperador);
        String numeroFactura = generarNumeroFactura(idIncidencia);
        String detalle = construirDetalle(incidencia);
        double costeTotal = calcularTotal(incidencia);

        String insert = "INSERT INTO Factura "
                + "(numero_factura, fecha_emision, emisor, detalle, coste_total, fk_incidencia, fk_operador) "
                + "VALUES (?, datetime('now','localtime'), ?, ?, ?, ?, ?)";
        db.executeUpdate(insert, numeroFactura, emisor, detalle, costeTotal, idIncidencia, idOperador);

        String historial = "INSERT INTO Historial (fecha_hora, estado, accion, detalle, fk_incidencia, fk_persona) "
                + "VALUES (datetime('now','localtime'), 'CERRADA', 'FACTURACION', ?, ?, ?)";
        db.executeUpdate(historial, "Factura emitida: " + numeroFactura + " por importe de " + costeTotal + " EUR",
                idIncidencia, idOperador);

        return getFacturaPorNumero(numeroFactura);
    }

    private void asegurarTablaFactura() {
        String sql = "CREATE TABLE IF NOT EXISTS Factura ("
                + "id_factura INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "numero_factura TEXT NOT NULL UNIQUE, "
                + "fecha_emision DATETIME NOT NULL DEFAULT (datetime('now','localtime')), "
                + "emisor TEXT NOT NULL, "
                + "detalle TEXT NOT NULL, "
                + "coste_total REAL NOT NULL CHECK (coste_total >= 0), "
                + "fk_incidencia INTEGER NOT NULL UNIQUE, "
                + "fk_operador INTEGER NOT NULL, "
                + "FOREIGN KEY (fk_incidencia) REFERENCES Incidencia(id_incidencia), "
                + "FOREIGN KEY (fk_operador) REFERENCES Persona(id_persona))";
        db.executeUpdate(sql);
    }

    private boolean existeFactura(long idIncidencia) {
        String sql = "SELECT id_factura FROM Factura WHERE fk_incidencia = ?";
        return !db.executeQueryArray(sql, idIncidencia).isEmpty();
    }

    private FacturaIncidenciaDTO getIncidenciaCerrada(long idIncidencia) {
        String sql = ""
                + "SELECT i.id_incidencia AS id, "
                + "       i.tipo, "
                + "       i.descripcion, "
                + "       i.fecha_hora AS fechaHoraRegistro, "
                + "       i.estado, "
                + "       COALESCE(t.nombre || ' ' || t.apellidos, t.usuario, 'Sin tecnico') AS tecnico, "
                + "       i.tiempo_real AS tiempoReal, "
                + "       COALESCE(t.precio_hora, 0) AS precioHora, "
                + "       COALESCE(i.coste_materiales, 0) AS costeMateriales, "
                + "       i.descripcion_materiales AS descripcionMateriales, "
                + "       i.trabajos_realizados AS trabajosRealizados, "
                + "       COALESCE(i.coste_total, i.coste, 0) AS costeTotal "
                + "FROM Incidencia i "
                + "LEFT JOIN Persona t ON i.fk_tecnico = t.id_persona "
                + "WHERE i.id_incidencia = ? AND i.estado = 'CERRADA'";

        List<FacturaIncidenciaDTO> rows = db.executeQueryPojo(FacturaIncidenciaDTO.class, sql, idIncidencia);

        if (rows.isEmpty()) {
            throw new ApplicationException("Solo se pueden facturar incidencias cerradas");
        }

        return rows.get(0);
    }

    private String getNombreOperador(int idOperador) {
        String sql = "SELECT COALESCE(nombre || ' ' || apellidos, usuario) FROM Persona "
                + "WHERE id_persona = ? AND tipo = 'OPERADOR'";
        List<Object[]> rows = db.executeQueryArray(sql, idOperador);

        if (rows.isEmpty()) {
            throw new ApplicationException("Operador no encontrado");
        }

        return String.valueOf(rows.get(0)[0]);
    }

    private String generarNumeroFactura(long idIncidencia) {
        return "FAC-" + LocalDateTime.now().format(NUMERO_FORMAT) + "-" + idIncidencia;
    }

    private String construirDetalle(FacturaIncidenciaDTO incidencia) {
        double manoObra = calcularManoObra(incidencia);
        double materiales = valor(incidencia.getCosteMateriales());
        double total = calcularTotal(incidencia);

        StringBuilder detalle = new StringBuilder();
        detalle.append("Incidencia ").append(incidencia.getId()).append(" - ").append(incidencia.getTipo()).append('\n');
        detalle.append("Descripcion: ").append(texto(incidencia.getDescripcion())).append('\n');
        detalle.append("Tecnico: ").append(texto(incidencia.getTecnico())).append('\n');
        detalle.append("Trabajos realizados: ").append(texto(incidencia.getTrabajosRealizados())).append('\n');
        detalle.append("Mano de obra: ").append(valor(incidencia.getTiempoReal()))
                .append(" minutos x ").append(valor(incidencia.getPrecioHora())).append(" EUR/h = ")
                .append(manoObra).append(" EUR").append('\n');
        detalle.append("Materiales: ").append(texto(incidencia.getDescripcionMateriales())).append(" = ")
                .append(materiales).append(" EUR").append('\n');
        detalle.append("Total facturado: ").append(total).append(" EUR");
        return detalle.toString();
    }

    private double calcularTotal(FacturaIncidenciaDTO incidencia) {
        double totalCierre = valor(incidencia.getCosteTotal());
        if (totalCierre > 0) {
            return redondear(totalCierre);
        }
        return redondear(calcularManoObra(incidencia) + valor(incidencia.getCosteMateriales()));
    }

    private double calcularManoObra(FacturaIncidenciaDTO incidencia) {
        double minutos = valor(incidencia.getTiempoReal());
        double precioHora = valor(incidencia.getPrecioHora());
        return redondear((minutos / 60.0) * precioHora);
    }

    private FacturaDTO getFacturaPorNumero(String numeroFactura) {
        String sql = "SELECT id_factura, numero_factura, fecha_emision, emisor, detalle, coste_total, fk_incidencia "
                + "FROM Factura WHERE numero_factura = ?";

        List<Object[]> rows = db.executeQueryArray(sql, numeroFactura);
        if (rows.isEmpty()) {
            throw new ApplicationException("No se pudo recuperar la factura generada");
        }

        Object[] row = rows.get(0);
        return new FacturaDTO(
                ((Number) row[0]).longValue(),
                String.valueOf(row[1]),
                String.valueOf(row[2]),
                String.valueOf(row[3]),
                String.valueOf(row[4]),
                ((Number) row[5]).doubleValue(),
                ((Number) row[6]).longValue());
    }

    private String texto(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "No consta";
        }
        return value.trim();
    }

    private double valor(Number value) {
        return value == null ? 0.0 : value.doubleValue();
    }

    private double redondear(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
