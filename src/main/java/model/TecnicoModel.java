package model;

import java.util.List;
import util.Database;

public class TecnicoModel {
    private Database db = new Database();

    // Obtener incidencias asignadas al técnico (Estado 'VALIDADA')
    public List<IncidenciaDisplayDTO> getIncidenciasAsignadas(int idTecnico) {
        String sql = "SELECT i.id_incidencia as id, i.tipo, i.descripcion, i.localizacion, " +
                     "i.fecha_hora as fechaHoraRegistro, i.estado, p.usuario as usuarioCiudadano " +
                     "FROM Incidencia i " +
                     "JOIN Persona p ON i.fk_ciudadano = p.id_persona " +
                     "WHERE i.estado = 'VALIDADA' AND i.fk_tecnico = ?"; 
        return db.executeQueryPojo(IncidenciaDisplayDTO.class, sql, idTecnico);
    }

    // Actualizar a "EN CURSO" con la previsión del técnico
    public void anotarPrevision(int idIncidencia, int horas, String trabajos) {
        String sql = "UPDATE Incidencia SET estado = 'EN CURSO', horas_prevision = ?, " +
                     "trabajos_reparacion = ? WHERE id_incidencia = ?";
        db.executeUpdate(sql, horas, trabajos, idIncidencia);
    }

    // Registrar en el historial el cambio a "EN CURSO"
    public void registrarHistorialTecnico(int idIncidencia, int idTecnico, String detalle) {
        String sql = "INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) " +
                     "VALUES ('EN CURSO', 'INICIO REPARACION', ?, ?, ?)";
        db.executeUpdate(sql, detalle, idIncidencia, idTecnico);
    }

    // Reutilizamos la búsqueda de persona por email
    public PersonaEntity getTecnicoByEmail(String email) {
        String sql = "SELECT id_persona as id, usuario, nombre, tipo FROM Persona WHERE email = ? AND tipo = 'TECNICO'";
        List<PersonaEntity> lista = db.executeQueryPojo(PersonaEntity.class, sql, email);
        return lista.isEmpty() ? null : lista.get(0);
    }
}