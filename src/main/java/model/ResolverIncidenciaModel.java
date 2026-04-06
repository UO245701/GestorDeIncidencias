package model;

import java.util.List;

import util.ApplicationException;
import util.Database;

public class ResolverIncidenciaModel {

    private Database db = new Database();

    public int getIdTecnicoByCorreo(String correo) {
        validarCorreo(correo);

        String sql = "SELECT id_persona FROM Persona "
                   + "WHERE email = ? "
                   + "AND UPPER(TRIM(tipo)) = 'TECNICO'";

        List<Object[]> rows = db.executeQueryArray(sql, correo.trim());

        if (rows.isEmpty()) {
            throw new ApplicationException("No existe ningún técnico registrado con ese correo");
        }

        return ((Number) rows.get(0)[0]).intValue();
    }

    public List<IncidenciaDisplayDTO> getIncidenciasEnCurso(int idTecnico) {
        String sql = "SELECT i.id_incidencia as id, "
                   + "i.tipo, "
                   + "i.descripcion, "
                   + "z.nombre as localizacion, "
                   + "i.fecha_hora as fechaHoraRegistro, "
                   + "i.estado, "
                   + "i.horas_prevision as horasPrevision, "
                   + "i.trabajos_reparacion as trabajosReparacion, "
                   + "p.email as usuarioCiudadano "
                   + "FROM Incidencia i "
                   + "JOIN Persona p ON i.fk_ciudadano = p.id_persona "
                   + "JOIN Zona z ON z.id_zona = i.fk_zona "
                   + "WHERE i.fk_tecnico = ? "
                   + "AND i.estado = 'EN CURSO' "
                   + "ORDER BY i.fecha_hora ASC";

        return db.executeQueryPojo(IncidenciaDisplayDTO.class, sql, idTecnico);
    }

 // Obtener precio/hora del técnico
    public double getPrecioHoraTecnico(int idTecnico) {
        String sql = "SELECT precio_hora FROM Persona WHERE id_persona = ?";
        List<Object[]> rows = db.executeQueryArray(sql, idTecnico);
        
        if (rows.isEmpty() || rows.get(0)[0] == null) {
            return 25.0; // Precio por defecto si falla
        }
        return Double.parseDouble(rows.get(0)[0].toString());
    }

    // MÉTODO ACTUALIZADO: Resolver con costes
    public void resolverIncidencia(int idIncidencia, int idTecnico, int tiempoReal, String trabajosRealizados, 
                                   double costeMateriales, String descripcionMateriales, double costeTotal) {
        
        validarDatosResolucion(idIncidencia, idTecnico, tiempoReal, trabajosRealizados);
        asegurarQueLaIncidenciaEsDelTecnicoYEstaEnCurso(idIncidencia, idTecnico);

        // 1. Actualizamos la incidencia con los nuevos campos de costes
        String updateIncidencia = "UPDATE Incidencia "
                                + "SET tiempo_real = ?, "
                                + "trabajos_realizados = ?, "
                                + "coste_materiales = ?, "
                                + "descripcion_materiales = ?, "
                                + "coste_total = ?, "
                                + "estado = 'RESUELTA' "
                                + "WHERE id_incidencia = ?";

        db.executeUpdate(updateIncidencia, tiempoReal, trabajosRealizados.trim(), 
                         costeMateriales, descripcionMateriales, costeTotal, idIncidencia);

        // 2. Insertamos en el historial un desglose completo
        String insertHistorial = "INSERT INTO Historial "
                               + "(fecha_hora, estado, accion, detalle, fk_incidencia, fk_persona) "
                               + "VALUES (datetime('now','localtime'), 'RESUELTA', 'RESOLVER INCIDENCIA', ?, ?, ?)";

        String detalle = "Tiempo: " + tiempoReal + "h. "
                       + "Trabajos: " + trabajosRealizados.trim() + ". "
                       + "Coste Materiales: " + costeMateriales + "€. "
                       + "Coste Total: " + costeTotal + "€.";

        db.executeUpdate(insertHistorial, detalle, idIncidencia, idTecnico);
    }

    private void asegurarQueLaIncidenciaEsDelTecnicoYEstaEnCurso(int idIncidencia, int idTecnico) {
        String sql = "SELECT id_incidencia "
                   + "FROM Incidencia "
                   + "WHERE id_incidencia = ? "
                   + "AND fk_tecnico = ? "
                   + "AND estado = 'EN CURSO'";

        List<Object[]> rows = db.executeQueryArray(sql, idIncidencia, idTecnico);

        if (rows.isEmpty()) {
            throw new ApplicationException(
                "La incidencia seleccionada no está asignada al técnico o ya no se encuentra en estado EN CURSO");
        }
    }

    private void validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new ApplicationException("El correo del técnico es obligatorio");
        }
    }

    private void validarDatosResolucion(int idIncidencia, int idTecnico, int tiempoReal, String trabajosRealizados) {
        if (idIncidencia <= 0) {
            throw new ApplicationException("Debe seleccionar una incidencia");
        }

        if (idTecnico <= 0) {
            throw new ApplicationException("No se ha identificado correctamente al técnico");
        }

        if (tiempoReal <= 0) {
            throw new ApplicationException("El tiempo real empleado es obligatorio y debe ser mayor que 0");
        }

        if (trabajosRealizados == null || trabajosRealizados.trim().isEmpty()) {
            throw new ApplicationException("La descripción de los trabajos realizados es obligatoria");
        }
    }
}