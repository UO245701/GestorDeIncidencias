package model;

import java.util.List;
import util.Database;

public class ValidarIncidenciasModel {
	private Database db = new Database();

	/**
	 * Obtiene las incidencias con estado 'NUEVA' para el operador.
	 * Se usa el DTO para incluir el nombre del ciudadano haciendo un JOIN.
	 */
	public List<IncidenciaDisplayDTO> getIncidenciasNuevas() {
		String sql = "SELECT i.id_incidencia as id, i.tipo, i.descripcion, i.localizacion, " +
				     "i.fecha_hora as fechaHoraRegistro, i.estado, p.usuario as usuarioCiudadano " +
				     "FROM Incidencia i " +
				     "JOIN Persona p ON i.fk_ciudadano = p.id_persona " +
				     "WHERE i.estado = 'NUEVA'";
		return db.executeQueryPojo(IncidenciaDisplayDTO.class, sql);
	}

	/**
	 * Actualiza el tipo y el estado de la incidencia a 'VALIDADA'
	 */
	public void validarIncidencia(int idIncidencia, String nuevoTipo) {
		String sql = "UPDATE Incidencia SET tipo = ?, estado = 'VALIDADA' WHERE id_incidencia = ?";
		db.executeUpdate(sql, nuevoTipo, idIncidencia);
	}

	/**
	 * Registra el cambio en el historial
	 */
	public void registrarEnHistorial(int idIncidencia, int idOperador, String tipo, String detalle) {
		String sql = "INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) " +
				     "VALUES ('VALIDADA', 'VALIDACION', ?, ?, ?)";
		db.executeUpdate(sql, detalle, idIncidencia, idOperador);
	}
	
	/**
	 * Busca al operador por su email para obtener su ID
	 */
	public PersonaEntity getOperadorByEmail(String email) {
		String sql = "SELECT id_persona as id, usuario, nombre, tipo FROM Persona WHERE email = ? AND tipo = 'OPERADOR'";
		List<PersonaEntity> lista = db.executeQueryPojo(PersonaEntity.class, sql, email);
		return lista.isEmpty() ? null : lista.get(0);
	}
}