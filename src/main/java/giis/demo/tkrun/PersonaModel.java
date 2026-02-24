package giis.demo.tkrun;

import java.util.List;

import giis.demo.util.Database;

public class PersonaModel {
    private Database db = new Database();

    /**
     * Busca una persona por usuario y contraseña (Login)
     */
    public PersonaEntity login(String usuario, String pass) {
        String sql = "SELECT id_persona as id, usuario, contrasena, tipo, nombre, apellidos, dni " +
                     "FROM Persona WHERE usuario=? AND contrasena=?";
        List<PersonaEntity> usuarios = db.executeQueryPojo(PersonaEntity.class, sql, usuario, pass);
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

    /**
     * Obtiene la lista de técnicos disponibles para asignar incidencias
     */
    public List<PersonaEntity> getListaTecnicos() {
        String sql = "SELECT id_persona as id, nombre || ' ' || apellidos as nombre FROM Persona WHERE tipo='TECNICO'";
        return db.executeQueryPojo(PersonaEntity.class, sql);
    }
}
