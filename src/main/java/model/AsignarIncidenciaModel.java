package model;

import java.util.List;
import util.Database;

public class AsignarIncidenciaModel {

    private Database db = new Database();

    /**
     * Incidencias asignables: estado VALIDADA ordenadas por fecha (antiguas primero).
     */
    public List<IncidenciaDisplayDTO> getIncidenciasValidadas() {
        String sql =
            "SELECT i.id_incidencia as id, i.tipo, i.descripcion, z.nombre as localizacion, " +
            "i.fecha_hora as fechaHoraRegistro, i.estado, p.usuario as usuarioCiudadano " +
            "FROM Incidencia i " +
            "JOIN Persona p ON i.fk_ciudadano = p.id_persona " +
            "JOIN Zona z ON i.fk_zona = z.id_zona " +
            "WHERE i.estado = 'VALIDADA' " +
            "ORDER BY i.fecha_hora ASC";
        return db.executeQueryPojo(IncidenciaDisplayDTO.class, sql);
    }

    /**
     * Técnicos disponibles para la incidencia seleccionada:
     * - solo técnicos
     * - del mismo tipo_responsable que el tipo de la incidencia
     * - que no estén ya asignados a esa incidencia
     * - con menos de 3 incidencias no cerradas
     * - ordenados por menor carga
     *
     * Modo mixto: cuenta tanto asignaciones antiguas (fk_tecnico)
     * como nuevas (IncidenciaTecnico).
     */
    public List<PersonaEntity> getTecnicosDisponiblesParaIncidencia(int idIncidencia) {
        String sql =
            "SELECT p.id_persona as id, p.usuario, p.nombre, p.apellidos, p.tipo, p.email " +
            "FROM Persona p, Incidencia inc " +
            "WHERE inc.id_incidencia = ? " +
            "AND p.tipo = 'TECNICO' " +
            "AND p.tipo_responsable = inc.tipo " +
            "AND p.id_persona NOT IN ( " +
            "    SELECT DISTINCT x.id_tecnico " +
            "    FROM ( " +
            "        SELECT i.fk_tecnico as id_tecnico " +
            "        FROM Incidencia i " +
            "        WHERE i.id_incidencia = ? AND i.fk_tecnico IS NOT NULL " +
            "        UNION " +
            "        SELECT it2.fk_tecnico as id_tecnico " +
            "        FROM IncidenciaTecnico it2 " +
            "        WHERE it2.fk_incidencia = ? " +
            "    ) x " +
            ") " +
            "AND ( " +
            "    SELECT COUNT(DISTINCT y.id_incidencia) " +
            "    FROM ( " +
            "        SELECT i1.id_incidencia as id_incidencia " +
            "        FROM Incidencia i1 " +
            "        WHERE i1.fk_tecnico = p.id_persona " +
            "        AND i1.estado <> 'CERRADA' " +
            "        UNION " +
            "        SELECT i2.id_incidencia as id_incidencia " +
            "        FROM IncidenciaTecnico it " +
            "        JOIN Incidencia i2 ON i2.id_incidencia = it.fk_incidencia " +
            "        WHERE it.fk_tecnico = p.id_persona " +
            "        AND i2.estado <> 'CERRADA' " +
            "    ) y " +
            ") < 3 " +
            "ORDER BY ( " +
            "    SELECT COUNT(DISTINCT y.id_incidencia) " +
            "    FROM ( " +
            "        SELECT i1.id_incidencia as id_incidencia " +
            "        FROM Incidencia i1 " +
            "        WHERE i1.fk_tecnico = p.id_persona " +
            "        AND i1.estado <> 'CERRADA' " +
            "        UNION " +
            "        SELECT i2.id_incidencia as id_incidencia " +
            "        FROM IncidenciaTecnico it " +
            "        JOIN Incidencia i2 ON i2.id_incidencia = it.fk_incidencia " +
            "        WHERE it.fk_tecnico = p.id_persona " +
            "        AND i2.estado <> 'CERRADA' " +
            "    ) y " +
            ") ASC, p.apellidos ASC, p.nombre ASC";

        return db.executeQueryPojo(PersonaEntity.class, sql, idIncidencia, idIncidencia, idIncidencia);
    }

    /**
     * Asigna uno o varios técnicos a una incidencia:
     * - inserta en IncidenciaTecnico
     * - pone la incidencia en estado ASIGNADA
     *
     * No toca fk_tecnico: la HU nueva trabaja con la tabla intermedia.
     */
    public void asignarTecnicosAIncidencia(int idIncidencia, List<Integer> idsTecnicos) {
        String sqlInsert =
            "INSERT INTO IncidenciaTecnico (fk_incidencia, fk_tecnico) VALUES (?, ?)";

        for (Integer idTecnico : idsTecnicos) {
            db.executeUpdate(sqlInsert, idIncidencia, idTecnico);
        }

        String sqlUpdateIncidencia =
            "UPDATE Incidencia " +
            "SET estado = 'ASIGNADA' " +
            "WHERE id_incidencia = ? AND estado = 'VALIDADA'";

        db.executeUpdate(sqlUpdateIncidencia, idIncidencia);
    }

    /**
     * Registrar cambio en historial.
     */
    public void registrarEnHistorial(int idIncidencia, int idOperador, String detalle) {
        String sql =
            "INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) " +
            "VALUES ('ASIGNADA', 'ASIGNACION', ?, ?, ?)";
        db.executeUpdate(sql, detalle, idIncidencia, idOperador);
    }

    /**
     * Obtener operador por email.
     */
    public PersonaEntity getOperadorByEmail(String email) {
        String sql =
            "SELECT id_persona as id, usuario, nombre, apellidos, tipo, email " +
            "FROM Persona " +
            "WHERE email = ? AND tipo = 'OPERADOR'";

        List<PersonaEntity> lista = db.executeQueryPojo(PersonaEntity.class, sql, email);
        return lista.isEmpty() ? null : lista.get(0);
    }
}