package model;

import java.util.List;
import util.Database;

public class AsignarIncidenciaModel {

	private Database db = new Database();

	/**
	 * Incidencias asignables: estado 'VALIDADA' ordenadas por fecha (antiguas primero).
	 * Usamos IncidenciaDisplayDTO (JOIN con Persona para sacar usuarioCiudadano).
	 */
	public List<IncidenciaDisplayDTO> getIncidenciasValidadas() {
		String sql =
			"SELECT i.id_incidencia as id, i.tipo, i.descripcion, i.localizacion, " +
			"i.fecha_hora as fechaHoraRegistro, i.estado, p.usuario as usuarioCiudadano " +
			"FROM Incidencia i " +
			"JOIN Persona p ON i.fk_ciudadano = p.id_persona " +
			"WHERE i.estado = 'VALIDADA' " +
			"ORDER BY i.fecha_hora ASC";
		return db.executeQueryPojo(IncidenciaDisplayDTO.class, sql);
	}

	/**
	 * Lista técnicos (PersonaEntity) para asignar.
	 */
	public List<PersonaEntity> getTecnicos() {
		String sql =
			"SELECT id_persona as id, usuario, nombre, apellidos, tipo, email " +
			"FROM Persona " +
			"WHERE tipo = 'TECNICO' " +
			"ORDER BY apellidos, nombre";
		return db.executeQueryPojo(PersonaEntity.class, sql);
	}

	/**
	 * Asigna incidencia -> estado 'ASIGNADA' y fk_tecnico.
	 * (condición estado='VALIDADA' para que solo asigne si es asignable)
	 */
	public void asignarIncidencia(int idIncidencia, int idTecnico) {
		String sql =
			"UPDATE Incidencia " +
			"SET fk_tecnico = ?, estado = 'ASIGNADA' " +
			"WHERE id_incidencia = ? AND estado = 'VALIDADA'";
		db.executeUpdate(sql, idTecnico, idIncidencia);
	}

	/**
	 * Registrar cambio en historial (quien cambia = operador)
	 */
	public void registrarEnHistorial(int idIncidencia, int idOperador, String detalle) {
	    String sql =
	        "INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) " +
	        "VALUES ('ASIGNADA', 'ASIGNACION', ?, ?, ?)";
	    db.executeUpdate(sql, detalle, idIncidencia, idOperador);
	}

	/**
	 * Obtener operador por email (igual que tu compi)
	 */
	public PersonaEntity getOperadorByEmail(String email) {
		String sql =
			"SELECT id_persona as id, usuario, nombre, apellidos, tipo, email " +
			"FROM Persona WHERE email = ? AND tipo = 'OPERADOR'";
		List<PersonaEntity> lista = db.executeQueryPojo(PersonaEntity.class, sql, email);
		return lista.isEmpty() ? null : lista.get(0);
	}
}