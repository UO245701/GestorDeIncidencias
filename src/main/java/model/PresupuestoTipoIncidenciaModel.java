package model;

import java.time.LocalDate;
import java.util.List;

import util.ApplicationException;
import util.Database;

public class PresupuestoTipoIncidenciaModel {

    private final Database db = new Database();

    public PersonaEntity getOperadorByEmail(String email) {
        String sql = ""
                + "SELECT id_persona as id, usuario, nombre, apellidos, tipo, email "
                + "FROM Persona "
                + "WHERE email = ? AND tipo = 'OPERADOR'";
        List<PersonaEntity> operadores = db.executeQueryPojo(PersonaEntity.class, sql, email.trim());
        return operadores.isEmpty() ? null : operadores.get(0);
    }

    public List<Object[]> getTiposIncidencia() {
        String sql = ""
                + "SELECT tipo FROM ("
                + "  SELECT DISTINCT tipo FROM Incidencia WHERE tipo IS NOT NULL AND TRIM(tipo) <> '' "
                + "  UNION SELECT 'Alumbrado' "
                + "  UNION SELECT 'Calzada' "
                + "  UNION SELECT 'Limpieza' "
                + "  UNION SELECT 'Mobiliario urbano' "
                + "  UNION SELECT 'Señalizacion' "
                + "  UNION SELECT 'Zonas verdes' "
                + ") "
                + "ORDER BY tipo";
        return db.executeQueryArray(sql);
    }

    public List<PresupuestoTipoIncidenciaDTO> getPresupuestos() {
        actualizarEstadosPorVigencia();

        String sql = ""
                + "SELECT id_presupuesto AS id, "
                + "       tipo, "
                + "       importe_maximo AS importeMaximo, "
                + "       importe_consumido AS importeConsumido, "
                + "       importe_maximo - importe_consumido AS importeDisponible, "
                + "       fecha_inicio AS fechaInicio, "
                + "       fecha_fin AS fechaFin, "
                + "       CASE "
                + "           WHEN activo = 1 "
                + "            AND date('now','localtime') BETWEEN date(fecha_inicio) AND date(fecha_fin) "
                + "           THEN 'SI' "
                + "           ELSE 'NO' "
                + "       END AS activo "
                + "FROM PresupuestoTipoIncidencia "
                + "ORDER BY activo DESC, tipo ASC, fecha_inicio DESC";
        return db.executeQueryPojo(PresupuestoTipoIncidenciaDTO.class, sql);
    }

    public void crearPresupuesto(String tipo, double importeMaximo, LocalDate fechaInicio,
            LocalDate fechaFin, int idOperador) {
        validarPresupuesto(tipo, importeMaximo, fechaInicio, fechaFin, idOperador);

        double importeConsumido = 0.0;
        int activo = estaEnPeriodoDeVigencia(fechaInicio, fechaFin) && !existePresupuestoActivo(tipo) ? 1 : 0;

        String insert = ""
                + "INSERT INTO PresupuestoTipoIncidencia "
                + "(tipo, importe_maximo, importe_consumido, fecha_inicio, fecha_fin, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        db.executeUpdate(insert, tipo.trim(), importeMaximo, importeConsumido,
                fechaInicio.toString(), fechaFin.toString(), activo);

        registrarHistorial(idOperador, tipo.trim(), importeMaximo, importeConsumido, fechaInicio, fechaFin);
    }

    private boolean existePresupuestoActivo(String tipo) {
        actualizarEstadosPorVigencia();

        String sql = ""
                + "SELECT id_presupuesto "
                + "FROM PresupuestoTipoIncidencia "
                + "WHERE tipo = ? AND activo = 1";
        return !db.executeQueryArray(sql, tipo.trim()).isEmpty();
    }

    private void actualizarEstadosPorVigencia() {
        String desactivarFueraDeVigencia = ""
                + "UPDATE PresupuestoTipoIncidencia "
                + "SET activo = 0 "
                + "WHERE activo = 1 "
                + "  AND date('now','localtime') NOT BETWEEN date(fecha_inicio) AND date(fecha_fin)";
        db.executeUpdate(desactivarFueraDeVigencia);
    }

    private boolean estaEnPeriodoDeVigencia(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDate hoy = LocalDate.now();
        return !hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaFin);
    }

    private void registrarHistorial(int idOperador, String tipo, double importeMaximo,
            double importeConsumido, LocalDate fechaInicio, LocalDate fechaFin) {
        String sql = ""
                + "INSERT INTO Historial (estado, accion, detalle, fk_persona) "
                + "VALUES ('PRESUPUESTO', 'CREACION_PRESUPUESTO', ?, ?)";
        String detalle = "Presupuesto activo para " + tipo
                + ". Maximo: " + importeMaximo
                + ". Consumido inicial: " + importeConsumido
                + ". Vigencia: " + fechaInicio + " - " + fechaFin;
        db.executeUpdate(sql, detalle, idOperador);
    }

    private void validarPresupuesto(String tipo, double importeMaximo, LocalDate fechaInicio,
            LocalDate fechaFin, int idOperador) {
        if (idOperador <= 0) {
            throw new ApplicationException("Debe identificarse como operador antes de crear presupuestos");
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new ApplicationException("Debe indicar el tipo de incidencia");
        }
        if (importeMaximo <= 0) {
            throw new ApplicationException("El importe maximo debe ser mayor que 0");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new ApplicationException("Debe indicar fecha inicial y fecha final");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new ApplicationException("La fecha final no puede ser anterior a la fecha inicial");
        }
    }
}
