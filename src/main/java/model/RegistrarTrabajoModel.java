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
                + "SELECT id_incidencia AS id, "
                + "       tipo, "
                + "       descripcion, "
                + "       estado "
                + "FROM Incidencia "
                + "WHERE fk_tecnico=? AND estado='EN CURSO'";

        return db.executeQueryPojo(IncidenciaListadoDTO.class, sql, idTecnico);
    }

    public List<HistorialDTO> getTrabajos(long idIncidencia) {

        String sql = ""
                + "SELECT detalle, fecha_hora AS fechaHora "
                + "FROM Historial "
                + "WHERE fk_incidencia=? AND accion='TRABAJO' "
                + "ORDER BY fecha_hora ASC";

        return db.executeQueryPojo(HistorialDTO.class, sql, idIncidencia);
    }

    public void addTrabajo(long idIncidencia, long idTecnico, String detalle) {

        String sql = ""
                + "INSERT INTO Historial (fecha_hora, estado, accion, detalle, fk_incidencia, fk_persona) "
                + "VALUES (datetime('now','localtime'), 'EN CURSO', 'TRABAJO', ?, ?, ?)";

        db.executeUpdate(sql, detalle, idIncidencia, idTecnico);
    }
}
