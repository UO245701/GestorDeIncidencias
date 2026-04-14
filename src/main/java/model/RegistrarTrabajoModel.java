package model;

import java.util.List;

import util.Database;
import util.ApplicationException;

public class RegistrarTrabajoModel {

    private final Database db = new Database();

    public long getIdTecnico(String input) {
        String sql = "SELECT id_persona FROM Persona WHERE (email=? OR dni=?) AND tipo='TECNICO'";

        List<Object[]> rows = db.executeQueryArray(sql, input, input);

        if (rows.isEmpty()) {
            throw new ApplicationException("Técnico no encontrado");
        }

        return Long.parseLong(rows.get(0)[0].toString());
    }

    public List<IncidenciaListadoDTO> getIncidencias(long idTecnico) {
        String sql = ""
                + "SELECT DISTINCT i.id_incidencia AS id, "
                + "       i.tipo, "
                + "       i.descripcion, "
                + "       i.estado "
                + "FROM Incidencia i "
                + "LEFT JOIN IncidenciaTecnico it ON i.id_incidencia = it.fk_incidencia "
                + "WHERE (i.fk_tecnico = ? OR it.fk_tecnico = ?) "
                + "AND i.estado = 'EN CURSO' "
                + "ORDER BY i.id_incidencia ASC";

        return db.executeQueryPojo(IncidenciaListadoDTO.class, sql, idTecnico, idTecnico);
    }

    public List<HistorialDTO> getTrabajos(long idIncidencia) {
        String sql = ""
                + "SELECT detalle, fecha_hora AS fechaHora "
                + "FROM Historial "
                + "WHERE fk_incidencia=? AND accion='TRABAJO' "
                + "ORDER BY fecha_hora ASC";

        return db.executeQueryPojo(HistorialDTO.class, sql, idIncidencia);
    }

    public void addTrabajo(long idIncidencia, long idTecnico, String detalle, String fechaHora) {
        String sql = ""
                + "INSERT INTO Historial (fecha_hora, estado, accion, detalle, fk_incidencia, fk_persona) "
                + "VALUES (?, 'EN CURSO', 'TRABAJO', ?, ?, ?)";

        db.executeUpdate(sql, fechaHora, detalle, idIncidencia, idTecnico);
    }
}