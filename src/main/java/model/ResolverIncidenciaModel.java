package model;

import java.util.List;

import util.ApplicationException;
import util.Database;

public class ResolverIncidenciaModel {

    private Database db = new Database();

    /**
     * Busca un técnico por su usuario/correo.
     * Cambia "usuario" por "email" si en tu BD el campo se llama email.
     */
    public PersonaEntity getTecnicoByCorreo(String correo) {
        validarCorreo(correo);

        String sql = "SELECT * FROM Persona WHERE usuario = ? AND tipo = 'TECNICO'";
        List<PersonaEntity> tecnicos = db.executeQueryPojo(PersonaEntity.class, sql, correo.trim());

        if (tecnicos.isEmpty()) {
            return null;
        }

        return tecnicos.get(0);
    }

    /**
     * Devuelve las incidencias EN CURSO asignadas a un técnico a partir de su id.
     */
    public List<IncidenciaDisplayDTO> getIncidenciasEnCurso(int idTecnico) {
        String sql = "SELECT i.id_incidencia as id, "
                   + "i.tipo, "
                   + "i.descripcion, "
                   + "z.nombre as localizacion, "
                   + "i.fecha_hora as fechaHoraRegistro, "
                   + "i.estado, "
                   + "p.usuario as usuarioCiudadano "
                   + "FROM Incidencia i "
                   + "JOIN Persona p ON i.fk_ciudadano = p.id_persona "
                   + "JOIN Zona z ON z.id_zona = i.fk_zona "
                   + "WHERE i.estado = 'EN CURSO' "
                   + "AND i.fk_tecnico = ? "
                   + "ORDER BY i.fecha_hora ASC";

        return db.executeQueryPojo(IncidenciaDisplayDTO.class, sql, idTecnico);
    }

    /**
     * Devuelve las incidencias EN CURSO asignadas a un técnico a partir de su correo/usuario.
     */
    public List<IncidenciaDisplayDTO> getIncidenciasEnCursoPorCorreo(String correo) {
        PersonaEntity tecnico = getTecnicoByCorreo(correo);

        if (tecnico == null) {
            throw new ApplicationException("No existe un técnico con ese correo");
        }

        return getIncidenciasEnCurso(tecnico.getId());
    }

    /**
     * Marca una incidencia como RESUELTA, guardando tiempo real y trabajos realizados.
     * IMPORTANTE: esto requiere que la tabla Incidencia tenga las columnas:
     * - tiempo_real
     * - trabajos_realizados
     */
    public void marcarComoResuelta(int idIncidencia, int tiempoReal, String trabajosRealizados) {
        validarDatosResolucion(idIncidencia, tiempoReal, trabajosRealizados);

        String sql = "UPDATE Incidencia "
                   + "SET tiempo_real = ?, "
                   + "trabajos_realizados = ?, "
                   + "estado = 'RESUELTA' "
                   + "WHERE id_incidencia = ? "
                   + "AND estado = 'EN CURSO'";

        db.executeUpdate(sql, tiempoReal, trabajosRealizados.trim(), idIncidencia);
    }

    private void validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new ApplicationException("El correo del técnico es obligatorio");
        }
    }

    private void validarDatosResolucion(int idIncidencia, int tiempoReal, String trabajosRealizados) {
        if (idIncidencia <= 0) {
            throw new ApplicationException("Debe seleccionar una incidencia válida");
        }

        if (tiempoReal <= 0) {
            throw new ApplicationException("El tiempo real debe ser mayor que 0");
        }

        if (trabajosRealizados == null || trabajosRealizados.trim().isEmpty()) {
            throw new ApplicationException("La descripción de los trabajos realizados es obligatoria");
        }
    }
}