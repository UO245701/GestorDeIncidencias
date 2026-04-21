package model;

import java.util.List;
import util.Database;

public class TecnicoModel {
    private Database db = new Database();

    // Obtener incidencias del técnico en las que participa:
    // tanto las antiguas (fk_tecnico) como las nuevas (IncidenciaTecnico)
    // y que estén ASIGNADA o EN CURSO
    public List<IncidenciaDisplayDTO> getIncidenciasAsignadas(int idTecnico) {
        String sql =
            "SELECT DISTINCT i.id_incidencia as id, i.tipo, i.descripcion, z.nombre as localizacion, " +
            "i.fecha_hora as fechaHoraRegistro, i.estado, p.usuario as usuarioCiudadano " +
            "FROM Incidencia i " +
            "JOIN Persona p ON i.fk_ciudadano = p.id_persona " +
            "JOIN Zona z ON z.id_zona = i.fk_zona " +
            "LEFT JOIN IncidenciaTecnico it ON i.id_incidencia = it.fk_incidencia " +
            "WHERE (i.fk_tecnico = ? OR it.fk_tecnico = ?) " +
            "AND i.estado IN ('ASIGNADA') " +
            "ORDER BY i.fecha_hora ASC";

        return db.executeQueryPojo(IncidenciaDisplayDTO.class, sql, idTecnico, idTecnico);
    }

    // Actualizar a EN CURSO con la previsión del técnico
    // El estado pertenece a la incidencia completa
    public void anotarPrevision(int idIncidencia, int horas, String trabajos) {
        String sql =
            "UPDATE Incidencia " +
            "SET estado = 'EN CURSO', horas_prevision = ?, trabajos_reparacion = ? " +
            "WHERE id_incidencia = ?";
        db.executeUpdate(sql, horas, trabajos, idIncidencia);
    }

    // Registrar en el historial el cambio a EN CURSO
    public void registrarHistorialTecnico(int idIncidencia, int idTecnico, String detalle) {
        String sql =
            "INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) " +
            "VALUES ('EN CURSO', 'INICIO REPARACION', ?, ?, ?)";
        db.executeUpdate(sql, detalle, idIncidencia, idTecnico);
    }

    // Reutilizamos la búsqueda de persona por email
    public PersonaEntity getTecnicoByEmail(String email) {
        String sql =
            "SELECT id_persona as id, usuario, nombre, tipo, email " +
            "FROM Persona " +
            "WHERE email = ? AND tipo = 'TECNICO'";
        List<PersonaEntity> lista = db.executeQueryPojo(PersonaEntity.class, sql, email);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // Rechazar incidencia asignada
    // En tu modelo actual, el rechazo afecta a toda la incidencia
    public void rechazarIncidencia(int idIncidencia, int idTecnico, String motivo) {
        String sqlUpdate =
            "UPDATE Incidencia " +
            "SET estado = 'RECHAZADA' " +
            "WHERE id_incidencia = ? AND estado = 'ASIGNADA'";
        db.executeUpdate(sqlUpdate, idIncidencia);

        String sqlHistorial =
            "INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) " +
            "VALUES ('RECHAZADA', 'RECHAZO', ?, ?, ?)";

        String detalleHistorial = "Incidencia rechazada. Motivo: " + motivo.trim();
        db.executeUpdate(sqlHistorial, detalleHistorial, idIncidencia, idTecnico);
    }
}