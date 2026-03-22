package model;

import java.util.ArrayList;
import java.util.List;

import util.Database;
import util.ApplicationException;

public class CerrarIncidenciasModel {

    private Database db = new Database();

    //LOGIN
    public int loginTecnico(String identificador) {

        String sql = "SELECT id_persona, tipo FROM Persona WHERE email=? OR dni=?";

        List<Object[]> rows = db.executeQueryArray(sql, identificador, identificador);

        if (rows.isEmpty()) {
            throw new ApplicationException("Usuario no encontrado");
        }

        int id = ((Number) rows.get(0)[0]).intValue();
        String tipo = rows.get(0)[1].toString();

        if (!"TECNICO".equalsIgnoreCase(tipo)) {
            throw new ApplicationException("Solo técnicos pueden acceder");
        }

        return id;
    }

    //OBTENER TIPO RESPONSABLE
    public String getTipoResponsable(int idTecnico) {

        String sql = "SELECT tipo_responsable FROM Persona WHERE id_persona=?";

        List<Object[]> rows = db.executeQueryArray(sql, idTecnico);

        if (rows.isEmpty()) {
            throw new ApplicationException("Usuario no encontrado");
        }

        String tipoResponsable = (String) rows.get(0)[0];

        if (tipoResponsable == null) {
            throw new ApplicationException("El técnico no es responsable");
        }

        return tipoResponsable;
    }

    //LISTADO DE INCIDENCIAS RESUELTAS POR TIPO
    public List<IncidenciaDisplayDTO> getIncidenciasResueltas(String tipo) {

        String sql = "SELECT * FROM Incidencia WHERE estado='RESUELTA' AND tipo=?";

        List<Object[]> rows = db.executeQueryArray(sql, tipo);

        List<IncidenciaDisplayDTO> lista = new ArrayList<>();

        for (Object[] r : rows) {
            lista.add(new IncidenciaDisplayDTO(
                    ((Number) r[0]).intValue(),
                    (String) r[1],
                    (String) r[2],
                    String.valueOf(r[10]),
                    (String) r[3],
                    (String) r[4],
                    String.valueOf(r[8]),
                    (Integer) r[6],
                    (String) r[7]
            ));
        }

        return lista;
    }

    //CIERRE + HISTORIAL
    public void cerrarIncidencias(List<Long> ids, int idTecnico) {

        String update = "UPDATE Incidencia SET estado='CERRADA' WHERE id_incidencia=?";
        String historial = "INSERT INTO Historial (fk_incidencia, fk_persona, estado, accion) VALUES (?, ?, 'CERRADA', 'CIERRE')";

        for (Long id : ids) {
            db.executeUpdate(update, id);
            db.executeUpdate(historial, id, idTecnico);
        }
    }
}
