package model;

import java.util.List;
import util.Database;

public class InformeMensualModel {
    private Database db = new Database();

    public List<InformeTecnicoDTO> getInformeUltimoMes() {
        // Agrupamos por técnico y sumamos sus tiempos y cantidades. 
        // Se incluyen los estados RESUELTA y CERRADA, ya que ambas representan trabajo finalizado.
        // date('now', '-1 month') filtra los últimos 30 días exactos.
        String sql = "SELECT p.nombre || ' ' || p.apellidos AS nombreTecnico, " +
                     "COUNT(i.id_incidencia) AS incidenciasResueltas, " +
                     "COALESCE(SUM(i.tiempo_real), 0) AS tiempoTotal " +
                     "FROM Persona p " +
                     "JOIN Incidencia i ON p.id_persona = i.fk_tecnico " +
                     "WHERE i.estado IN ('RESUELTA', 'CERRADA') " +
                     "AND i.fecha_hora >= date('now', '-1 month') " +
                     "GROUP BY p.id_persona, p.nombre, p.apellidos " +
                     "ORDER BY incidenciasResueltas DESC";
                     
        return db.executeQueryPojo(InformeTecnicoDTO.class, sql);
    }
}