package model;

import java.util.List;

import util.ApplicationException;
import util.Database;

public class ListadoIncidenciasPeriodoModel {

    private static final String MSG_USUARIO_OBLIGATORIO = "El email o DNI es obligatorio";
    private static final String MSG_FECHA_INICIO_OBLIGATORIA = "La fecha de inicio es obligatoria";
    private static final String MSG_FECHA_FIN_OBLIGATORIA = "La fecha de fin es obligatoria";

    private Database db = new Database();

    public boolean esUsuarioRegistrado(String identificador) {
        validateNotBlank(identificador, MSG_USUARIO_OBLIGATORIO);

        String sql =
                "SELECT id_persona " +
                "FROM Persona " +
                "WHERE email = ? " +
                "   OR (dni = ? AND tipo = 'CIUDADANO')";

        List<Object[]> rows = db.executeQueryArray(sql, identificador.trim(), identificador.trim());
        return !rows.isEmpty();
    }

    public String getNombreUsuario(String identificador) {
        validateNotBlank(identificador, MSG_USUARIO_OBLIGATORIO);

        String sql =
                "SELECT usuario " +
                "FROM Persona " +
                "WHERE email = ? " +
                "   OR (dni = ? AND tipo = 'CIUDADANO')";

        List<Object[]> rows = db.executeQueryArray(sql, identificador.trim(), identificador.trim());

        if (rows.isEmpty()) {
            throw new ApplicationException("No existe usuario registrado con ese identificador");
        }

        return (String) rows.get(0)[0];
    }

    public List<IncidenciaDisplayDTO> getIncidenciasRegistradasEntreFechas(String fechaInicio, String fechaFin) {
        validateNotBlank(fechaInicio, MSG_FECHA_INICIO_OBLIGATORIA);
        validateNotBlank(fechaFin, MSG_FECHA_FIN_OBLIGATORIA);

        String sql =
                "SELECT i.id_incidencia as id, " +
                "i.tipo as tipo, " +
                "i.descripcion as descripcion, " +
                "z.nombre as localizacion, " +
                "i.fecha_hora as fechaHoraRegistro, " +
                "i.estado as estado, " +
                "p.usuario as usuarioCiudadano " +
                "FROM Incidencia i " +
                "JOIN Persona p ON i.fk_ciudadano = p.id_persona " +
                "JOIN Zona z ON i.fk_zona = z.id_zona " +
                "WHERE DATE(i.fecha_hora) BETWEEN DATE(?) AND DATE(?) " +
                "ORDER BY i.fecha_hora ASC";

        return db.executeQueryPojo(
                IncidenciaDisplayDTO.class,
                sql,
                fechaInicio.trim(),
                fechaFin.trim()
        );
    }

    private void validateNotBlank(String value, String msg) {
        if (value == null || value.trim().isEmpty()) {
            throw new ApplicationException(msg);
        }
    }
}