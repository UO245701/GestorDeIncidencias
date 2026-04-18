package model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import util.ApplicationException;
import util.Database;

public class ExportacionHistorialModel {

    private final Database db = new Database();

    public PersonaEntity validarOperadorOTecnico(String identificador) {
        if (identificador == null || identificador.trim().isEmpty()) {
            throw new ApplicationException("El identificador es obligatorio");
        }

        String sql = ""
                + "SELECT id_persona AS id, usuario, nombre, apellidos, tipo, email "
                + "FROM Persona "
                + "WHERE email = ? OR dni = ?";

        List<PersonaEntity> personas = db.executeQueryPojo(
                PersonaEntity.class,
                sql,
                identificador.trim(),
                identificador.trim());

        if (personas.isEmpty()) {
            throw new ApplicationException("Usuario no encontrado");
        }

        PersonaEntity persona = personas.get(0);
        if (!"OPERADOR".equalsIgnoreCase(persona.getTipo())
                && !"TECNICO".equalsIgnoreCase(persona.getTipo())) {
            throw new ApplicationException("Acceso permitido solo para OPERADOR o TECNICO");
        }

        return persona;
    }

    public List<Object[]> getTiposIncidencia() {
        String sql = ""
                + "SELECT tipo "
                + "FROM ("
                + "  SELECT DISTINCT tipo FROM Incidencia WHERE tipo IS NOT NULL AND TRIM(tipo) <> '' "
                + "  UNION SELECT 'Alumbrado' "
                + "  UNION SELECT 'Calzada' "
                + "  UNION SELECT 'Limpieza' "
                + "  UNION SELECT 'Mobiliario urbano' "
                + "  UNION SELECT 'Señalizacion' "
                + "  UNION SELECT 'Zonas verdes' "
                + ") "
                + "ORDER BY tipo";
        return db.executeQueryArray(sql);
    }

    public List<Object[]> getZonas() {
        return db.executeQueryArray("SELECT nombre FROM Zona ORDER BY nombre");
    }

    public List<IncidenciaExportacionDTO> buscarIncidencias(String tipo, String zona,
            String fechaInicio, String fechaFin) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT i.id_incidencia AS id, ")
                .append("i.tipo, ")
                .append("z.nombre AS zona, ")
                .append("i.descripcion, ")
                .append("i.estado, ")
                .append("i.fecha_hora AS fechaAlta, ")
                .append("COALESCE(i.coste, 0) AS costeEstimado, ")
                .append("COALESCE(i.coste_materiales, 0) AS costeMateriales, ")
                .append("COALESCE(i.coste_total, 0) AS costeTotal, ")
                .append("TRIM(COALESCE(t.nombre, '') || ' ' || COALESCE(t.apellidos, '')) AS tecnico ")
                .append("FROM Incidencia i ")
                .append("JOIN Zona z ON z.id_zona = i.fk_zona ")
                .append("LEFT JOIN Persona t ON t.id_persona = i.fk_tecnico ")
                .append("WHERE 1 = 1 ");

        if (tipo != null && !tipo.trim().isEmpty()) {
            sql.append("AND i.tipo = ? ");
            params.add(tipo.trim());
        }
        if (zona != null && !zona.trim().isEmpty()) {
            sql.append("AND z.nombre = ? ");
            params.add(zona.trim());
        }
        if (fechaInicio != null && !fechaInicio.trim().isEmpty()) {
            sql.append("AND date(i.fecha_hora) >= date(?) ");
            params.add(fechaInicio.trim());
        }
        if (fechaFin != null && !fechaFin.trim().isEmpty()) {
            sql.append("AND date(i.fecha_hora) <= date(?) ");
            params.add(fechaFin.trim());
        }

        sql.append("ORDER BY i.fecha_hora ASC, i.id_incidencia ASC");

        return db.executeQueryPojo(
                IncidenciaExportacionDTO.class,
                sql.toString(),
                params.toArray());
    }

    public int exportarHistorialCsv(List<Integer> idsIncidencia, Path destino) {
        if (idsIncidencia == null || idsIncidencia.isEmpty()) {
            throw new ApplicationException("Debe seleccionar al menos una incidencia para exportar");
        }
        if (destino == null) {
            throw new ApplicationException("Debe seleccionar el fichero de destino");
        }

        List<String> lineas = new ArrayList<>();
        lineas.add(String.join(",",
                "id_incidencia",
                "tipo",
                "zona",
                "descripcion",
                "estado_actual",
                "fecha_alta",
                "coste_estimado",
                "coste_materiales",
                "coste_total",
                "tecnico",
                "fecha_cambio",
                "estado_cambio",
                "comentarios"));

        for (Integer id : idsIncidencia) {
            IncidenciaExportacionDTO incidencia = getIncidencia(id);
            List<HistorialDTO> historial = getHistorial(id);

            if (historial.isEmpty()) {
                lineas.add(toCsvLine(incidencia, "", "", ""));
            } else {
                for (HistorialDTO cambio : historial) {
                    lineas.add(toCsvLine(
                            incidencia,
                            cambio.getFechaHora(),
                            cambio.getEstado(),
                            cambio.getDetalle()));
                }
            }
        }

        try {
            Files.write(destino, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ApplicationException("No se pudo escribir el fichero CSV: " + e.getMessage());
        }

        return idsIncidencia.size();
    }

    private IncidenciaExportacionDTO getIncidencia(int idIncidencia) {
        List<IncidenciaExportacionDTO> incidencias = buscarIncidenciasPorId(idIncidencia);
        if (incidencias.isEmpty()) {
            throw new ApplicationException("No existe la incidencia " + idIncidencia);
        }
        return incidencias.get(0);
    }

    private List<IncidenciaExportacionDTO> buscarIncidenciasPorId(int idIncidencia) {
        String sql = ""
                + "SELECT i.id_incidencia AS id, "
                + "       i.tipo, "
                + "       z.nombre AS zona, "
                + "       i.descripcion, "
                + "       i.estado, "
                + "       i.fecha_hora AS fechaAlta, "
                + "       COALESCE(i.coste, 0) AS costeEstimado, "
                + "       COALESCE(i.coste_materiales, 0) AS costeMateriales, "
                + "       COALESCE(i.coste_total, 0) AS costeTotal, "
                + "       TRIM(COALESCE(t.nombre, '') || ' ' || COALESCE(t.apellidos, '')) AS tecnico "
                + "FROM Incidencia i "
                + "JOIN Zona z ON z.id_zona = i.fk_zona "
                + "LEFT JOIN Persona t ON t.id_persona = i.fk_tecnico "
                + "WHERE i.id_incidencia = ?";
        return db.executeQueryPojo(IncidenciaExportacionDTO.class, sql, idIncidencia);
    }

    private List<HistorialDTO> getHistorial(int idIncidencia) {
        String sql = ""
                + "SELECT id_historial AS id, "
                + "       fecha_hora AS fechaHora, "
                + "       estado, "
                + "       detalle "
                + "FROM Historial "
                + "WHERE fk_incidencia = ? "
                + "ORDER BY fecha_hora ASC, id_historial ASC";
        return db.executeQueryPojo(HistorialDTO.class, sql, idIncidencia);
    }

    private String toCsvLine(IncidenciaExportacionDTO incidencia, String fechaCambio,
            String estadoCambio, String comentarios) {
        return String.join(",",
                csv(incidencia.getId()),
                csv(incidencia.getTipo()),
                csv(incidencia.getZona()),
                csv(incidencia.getDescripcion()),
                csv(incidencia.getEstado()),
                csv(incidencia.getFechaAlta()),
                csv(incidencia.getCosteEstimado()),
                csv(incidencia.getCosteMateriales()),
                csv(incidencia.getCosteTotal()),
                csv(incidencia.getTecnico()),
                csv(fechaCambio),
                csv(estadoCambio),
                csv(comentarios));
    }

    private String csv(Object value) {
        String text = value == null ? "" : value.toString();
        text = text.replace("\"", "\"\"");
        return "\"" + text + "\"";
    }
}
