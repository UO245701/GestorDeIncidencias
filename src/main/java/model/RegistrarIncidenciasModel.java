package model;

import java.util.ArrayList;
import java.util.List;

import util.ApplicationException;
import util.Database;

public class RegistrarIncidenciasModel {

    private static final String MSG_USUARIO_OBLIGATORIO = "El correo o DNI es obligatorio";
    private static final String MSG_TIPO_OBLIGATORIO = "El tipo de incidencia es obligatorio";
    private static final String MSG_DESCRIPCION_OBLIGATORIA = "La descripción es obligatoria";
    private static final String MSG_LOCALIZACION_OBLIGATORIA = "La localización es obligatoria";

    private Database db = new Database();

    public boolean esCiudadanoRegistrado(String identificador) {
        validateNotBlank(identificador, MSG_USUARIO_OBLIGATORIO);

        String sql = "SELECT id_persona FROM Persona WHERE (email=? OR dni=?) AND tipo='CIUDADANO'";
        List<Object[]> rows = db.executeQueryArray(sql, identificador.trim(), identificador.trim());

        return !rows.isEmpty();
    }

    public int getCiudadanoId(String identificador) {
        validateNotBlank(identificador, MSG_USUARIO_OBLIGATORIO);

        String sql = "SELECT id_persona FROM Persona WHERE (email=? OR dni=?) AND tipo='CIUDADANO'";
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

    public List<String> getZonas() {
        String sql = "SELECT nombre FROM Zona ORDER BY nombre";
        List<Object[]> rows = db.executeQueryArray(sql);

        List<String> zonas = new ArrayList<>();
        for (Object[] row : rows) {
            zonas.add((String) row[0]);
        }
        return zonas;
    }

    public int getZonaId(String nombreZona) {
        validateNotBlank(nombreZona, MSG_LOCALIZACION_OBLIGATORIA);

        String sql = "SELECT id_zona FROM Zona WHERE nombre = ?";
        List<Object[]> rows = db.executeQueryArray(sql, nombreZona.trim());

        if (rows.isEmpty()) {
            throw new ApplicationException("La zona seleccionada no existe");
        }

        Object value = rows.get(0)[0];
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(value.toString());
    }

    public void registrarIncidencia(String identificador, String tipo, String descripcion, String localizacion) {
        validateNotBlank(identificador, MSG_USUARIO_OBLIGATORIO);
        validateNotBlank(tipo, MSG_TIPO_OBLIGATORIO);
        validateNotBlank(descripcion, MSG_DESCRIPCION_OBLIGATORIA);
        validateNotBlank(localizacion, MSG_LOCALIZACION_OBLIGATORIA);

        int ciudadanoId = getCiudadanoId(identificador);
        int zonaId = getZonaId(localizacion);

        String sql = "INSERT INTO Incidencia(tipo, descripcion, fk_zona, fecha_hora, estado, fk_ciudadano) "
                + "VALUES (?, ?, ?, datetime('now','localtime'), 'NUEVA', ?)";

        db.executeUpdate(sql, tipo.trim(), descripcion.trim(), zonaId, ciudadanoId);
    }

    public IncidenciaDisplayDTO getUltimaIncidencia(String identificador) {
        validateNotBlank(identificador, MSG_USUARIO_OBLIGATORIO);

        String sql = "SELECT i.id_incidencia as id, i.tipo as tipo, i.descripcion as descripcion, "
                + "z.nombre as localizacion, "
                + "i.fecha_hora as fechaHoraRegistro, i.estado as estado, p.email as usuarioCiudadano "
                + "FROM Incidencia i "
                + "JOIN Persona p ON p.id_persona = i.fk_ciudadano "
                + "JOIN Zona z ON z.id_zona = i.fk_zona "
                + "WHERE (p.email=? OR p.dni=?) "
                + "ORDER BY i.id_incidencia DESC "
                + "LIMIT 1";

        List<IncidenciaDisplayDTO> incidencias = db.executeQueryPojo(
                IncidenciaDisplayDTO.class,
                sql,
                identificador.trim(),
                identificador.trim()
        );

        if (incidencias.isEmpty()) {
            throw new ApplicationException("No se encontró la incidencia registrada");
        }

        return incidencias.get(0);
    }

    private void validateNotBlank(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new ApplicationException(message);
        }
    }
}