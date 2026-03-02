package model;

import java.util.List;

import util.ApplicationException;
import util.Database;

public class VisualizarHistorialModel {

    private final Database db = new Database();

    public void validarOperadorOTecnico(String identificador) {

        if (identificador == null || identificador.trim().isEmpty()) {
            throw new ApplicationException("El identificador es obligatorio");
        }

        String sql = ""
                + "SELECT tipo "
                + "FROM Persona "
                + "WHERE email=? OR dni=?";

        List<Object[]> rows = db.executeQueryArray(sql, identificador, identificador);

        if (rows.isEmpty()) {
            throw new ApplicationException("Usuario no encontrado");
        }

        String tipo = rows.get(0)[0].toString();

        if (!"OPERADOR".equalsIgnoreCase(tipo) &&
            !"TECNICO".equalsIgnoreCase(tipo)) {
            throw new ApplicationException("Acceso permitido solo para OPERADOR o TECNICO");
        }
    }

    public List<IncidenciaListadoDTO> getIncidencias() {

        String sql = ""
                + "SELECT id_incidencia AS id, "
                + "       tipo, "
                + "       descripcion, "
                + "       fecha_hora AS fechaHora, "
                + "       estado "
                + "FROM Incidencia "
                + "ORDER BY fecha_hora DESC";

        return db.executeQueryPojo(IncidenciaListadoDTO.class, sql);
    }

    public List<HistorialDTO> getHistorial(int incidenciaId) {

        String sql = ""
                + "SELECT fecha_hora AS fechaHora, "
                + "       id_historial AS id, "
                + "       estado "
                + "FROM Historial "
                + "WHERE fk_incidencia=? "
                + "ORDER BY fecha_hora DESC";

        return db.executeQueryPojo(HistorialDTO.class, sql, incidenciaId);
    }
}
