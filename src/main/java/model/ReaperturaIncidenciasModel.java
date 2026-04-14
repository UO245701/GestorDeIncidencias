package model;

import java.util.List;

import util.ApplicationException;
import util.Database;

public class ReaperturaIncidenciasModel {

    private static final String MSG_USUARIO_OBLIGATORIO = "El correo o DNI es obligatorio";
    private static final String MSG_MOTIVO_OBLIGATORIO = "El motivo de reapertura es obligatorio";

    private Database db = new Database();

    public boolean esCiudadanoRegistrado(String identificador) {
        validateNotBlank(identificador, MSG_USUARIO_OBLIGATORIO);

        String sql = "SELECT id_persona "
                + "FROM Persona "
                + "WHERE (email = ? OR dni = ?) AND tipo = 'CIUDADANO'";

        List<Object[]> rows = db.executeQueryArray(sql, identificador.trim(), identificador.trim());
        return !rows.isEmpty();
    }

    public int getCiudadanoId(String identificador) {
        validateNotBlank(identificador, MSG_USUARIO_OBLIGATORIO);

        String sql = "SELECT id_persona "
                + "FROM Persona "
                + "WHERE (email = ? OR dni = ?) AND tipo = 'CIUDADANO'";

        List<Object[]> rows = db.executeQueryArray(sql, identificador.trim(), identificador.trim());

        if (rows.isEmpty()) {
            throw new ApplicationException("No existe un ciudadano registrado con ese correo o DNI");
        }

        Object value = rows.get(0)[0];
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(value.toString());
    }

    public List<IncidenciaDisplayDTO> getIncidenciasCiudadano(String identificador) {
        validateNotBlank(identificador, MSG_USUARIO_OBLIGATORIO);

        String sql = "SELECT i.id_incidencia as id, "
                + "i.tipo as tipo, "
                + "i.descripcion as descripcion, "
                + "z.nombre as localizacion, "
                + "i.fecha_hora as fechaHoraRegistro, "
                + "i.estado as estado, "
                + "p.email as usuarioCiudadano "
                + "FROM Incidencia i "
                + "JOIN Persona p ON p.id_persona = i.fk_ciudadano "
                + "JOIN Zona z ON z.id_zona = i.fk_zona "
                + "WHERE (p.email = ? OR p.dni = ?) "
                + "AND i.estado IN ('RECHAZADA', 'CERRADA') "
                + "ORDER BY i.fecha_hora DESC";

        return db.executeQueryPojo(
                IncidenciaDisplayDTO.class,
                sql,
                identificador.trim(),
                identificador.trim()
        );
    }

    public IncidenciaDisplayDTO getIncidenciaSeleccionada(int idIncidencia, String identificador) {
        validateNotBlank(identificador, MSG_USUARIO_OBLIGATORIO);

        String sql = "SELECT i.id_incidencia as id, "
                + "i.tipo as tipo, "
                + "i.descripcion as descripcion, "
                + "z.nombre as localizacion, "
                + "i.fecha_hora as fechaHoraRegistro, "
                + "i.estado as estado, "
                + "p.email as usuarioCiudadano "
                + "FROM Incidencia i "
                + "JOIN Persona p ON p.id_persona = i.fk_ciudadano "
                + "JOIN Zona z ON z.id_zona = i.fk_zona "
                + "WHERE i.id_incidencia = ? "
                + "AND (p.email = ? OR p.dni = ?)";

        List<IncidenciaDisplayDTO> incidencias = db.executeQueryPojo(
                IncidenciaDisplayDTO.class,
                sql,
                idIncidencia,
                identificador.trim(),
                identificador.trim()
        );

        if (incidencias.isEmpty()) {
            throw new ApplicationException("No se encontró la incidencia seleccionada");
        }

        return incidencias.get(0);
    }

    public boolean puedeReabrirse(int idIncidencia, String identificador) {
        validateNotBlank(identificador, MSG_USUARIO_OBLIGATORIO);

        String sql = "SELECT COUNT(*) "
                + "FROM Incidencia i "
                + "JOIN Persona p ON p.id_persona = i.fk_ciudadano "
                + "WHERE i.id_incidencia = ? "
                + "AND (p.email = ? OR p.dni = ?) "
                + "AND i.estado IN ('RECHAZADA', 'CERRADA')";

        List<Object[]> rows = db.executeQueryArray(
                sql,
                idIncidencia,
                identificador.trim(),
                identificador.trim()
        );

        if (rows.isEmpty()) {
            return false;
        }

        Object value = rows.get(0)[0];
        int count;

        if (value instanceof Number) {
            count = ((Number) value).intValue();
        } else {
            count = Integer.parseInt(value.toString());
        }

        return count > 0;
    }

    public void reabrirIncidencia(int idIncidencia, String identificador, String motivo) {
        validateNotBlank(identificador, MSG_USUARIO_OBLIGATORIO);
        validateNotBlank(motivo, MSG_MOTIVO_OBLIGATORIO);

        if (!puedeReabrirse(idIncidencia, identificador)) {
            throw new ApplicationException("La incidencia no puede reabrirse");
        }

        String sqlUpdate = "UPDATE Incidencia "
                + "SET estado = 'REABIERTA' "
                + "WHERE id_incidencia = ?";

        db.executeUpdate(sqlUpdate, idIncidencia);

        registrarReapertura(idIncidencia, identificador, motivo);
    }

    public void registrarReapertura(int idIncidencia, String identificador, String motivo) {
        validateNotBlank(motivo, MSG_MOTIVO_OBLIGATORIO);

        int idCiudadano = getCiudadanoId(identificador);

        String sql = "INSERT INTO Historial "
                + "(fecha_hora, estado, accion, detalle, fk_incidencia, fk_persona) "
                + "VALUES (datetime('now','localtime'), 'REABIERTA', 'REAPERTURA', ?, ?, ?)";

        db.executeUpdate(sql, motivo.trim(), idIncidencia, idCiudadano);
    }

    public List<HistorialDTO> getHistorialIncidencia(int idIncidencia, String identificador) {
        validateNotBlank(identificador, MSG_USUARIO_OBLIGATORIO);

        String sql = "SELECT h.fecha_hora as fechaHora, "
                + "h.fk_persona as id, "
                + "COALESCE(h.estado, h.accion) as estado, "
                + "h.detalle as detalle "
                + "FROM Historial h "
                + "JOIN Incidencia i ON i.id_incidencia = h.fk_incidencia "
                + "JOIN Persona c ON c.id_persona = i.fk_ciudadano "
                + "WHERE h.fk_incidencia = ? "
                + "AND (c.email = ? OR c.dni = ?) "
                + "ORDER BY h.fecha_hora ASC";

        return db.executeQueryPojo(
                HistorialDTO.class,
                sql,
                idIncidencia,
                identificador.trim(),
                identificador.trim()
        );
    }

    private void validateNotBlank(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new ApplicationException(message);
        }
    }
}