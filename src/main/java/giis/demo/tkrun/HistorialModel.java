package giis.demo.tkrun;

import java.util.List;

import giis.demo.util.Database;

public class HistorialModel {
    private Database db = new Database();

    /**
     * Registra una nueva entrada en el historial (Auditoría)
     */
    public void registrarAccion(int idIncidencia, int idPersona, String estado, String accion, String detalle) {
        String sql = "INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) " +
                     "VALUES (?, ?, ?, ?, ?)";
        db.executeUpdate(sql, estado, accion, detalle, idIncidencia, idPersona);
    }

    /**
     * Obtiene todo el historial de una incidencia concreta
     */
    public List<HistorialEntity> getHistorialPorIncidencia(int idIncidencia) {
        String sql = "SELECT id_historial as id, fecha_hora as fechaHora, estado, accion, detalle, " +
                     "fk_incidencia as fkIncidencia, fk_persona as fkPersona " +
                     "FROM Historial WHERE fk_incidencia=? ORDER BY fecha_hora DESC";
        return db.executeQueryPojo(HistorialEntity.class, sql, idIncidencia);
    }
}
