package giis.demo.tkrun;

import java.util.List;

import giis.demo.util.Database;

public class IncidenciaModel {
    private Database db = new Database();

    /**
     * Registra una nueva incidencia (Flujo del Ciudadano)
     */
    public void registrarIncidencia(IncidenciaEntity incidencia) {
        String sql = "INSERT INTO Incidencia (tipo, descripcion, localizacion, estado, fk_ciudadano) " +
                     "VALUES (?, ?, ?, 'ABIERTA', ?)";
        db.executeUpdate(sql, incidencia.getTipo(), incidencia.getDescripcion(), 
                         incidencia.getLocalizacion(), incidencia.getIdCiudadano());
    }

    /**
     * Obtiene incidencias según el estado (ej. 'ABIERTA' para el Operador)
     */
    public List<IncidenciaEntity> getIncidenciasPorEstado(String estado) {
        String sql = "SELECT id_incidencia as id, tipo, descripcion, localizacion, fecha_hora as fechaHora, " +
                     "estado, fk_ciudadano as idCiudadano FROM Incidencia WHERE estado=?";
        return db.executeQueryPojo(IncidenciaEntity.class, sql, estado);
    }

    /**
     * Asigna un técnico a una incidencia y cambia su estado (Flujo del Operador)
     */
    public void asignarTecnico(int idIncidencia, int idTecnico) {
        String sql = "UPDATE Incidencia SET fk_tecnico=?, estado='ASIGNADA' WHERE id_incidencia=?";
        db.executeUpdate(sql, idTecnico, idIncidencia);
    }
}
