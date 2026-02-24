package model;

import java.util.List;

import util.ApplicationException;
import util.Database;

/**
 * Modelo para la HU 33818 - Registrar Incidencia
 * Sigue exactamente el mismo patrón que CarrerasModel
 */
public class RegistrarIncidenciasModel {

	private static final String MSG_USUARIO_OBLIGATORIO = "El usuario (correo o DNI) es obligatorio";
	private static final String MSG_TIPO_OBLIGATORIO = "El tipo de incidencia es obligatorio";
	private static final String MSG_DESCRIPCION_OBLIGATORIA = "La descripción es obligatoria";
	private static final String MSG_LOCALIZACION_OBLIGATORIA = "La localización es obligatoria";

	private Database db = new Database();

	/**
	 * Devuelve el id del ciudadano si existe y es de tipo CIUDADANO.
	 * Lanza excepción si no existe.
	 */
	public int getCiudadanoId(String usuario) {
		validateNotBlank(usuario, MSG_USUARIO_OBLIGATORIO);

		String sql = "SELECT id FROM persona WHERE usuario=? AND tipo='CIUDADANO'";
		List<Object[]> rows = db.executeQueryArray(sql, usuario.trim());

		if (rows.isEmpty())
			throw new ApplicationException("No existe un ciudadano identificado con ese usuario");

		// OJO: el tipo puede venir como Integer/Long dependiendo del driver
		Object value = rows.get(0)[0];
		if (value instanceof Number) return ((Number) value).intValue();
		return Integer.parseInt(value.toString());
	}

	/**
	 * Inserta una nueva incidencia (fecha y estado automáticos)
	 */
	public void registrarIncidencia(String usuario, String tipo, String descripcion, String localizacion) {

		validateNotBlank(usuario, MSG_USUARIO_OBLIGATORIO);
		validateNotBlank(tipo, MSG_TIPO_OBLIGATORIO);
		validateNotBlank(descripcion, MSG_DESCRIPCION_OBLIGATORIA);
		validateNotBlank(localizacion, MSG_LOCALIZACION_OBLIGATORIA);

		int ciudadanoId = getCiudadanoId(usuario);

		String sql = "INSERT INTO incidencia(tipo, descripcion, localizacion, fecha_hora_registro, estado, ciudadano_id) "
				+ "VALUES (?, ?, ?, datetime('now'), 'Nueva', ?)";

		db.executeUpdate(sql, tipo.trim(), descripcion.trim(), localizacion.trim(), ciudadanoId);
	}

	/**
	 * Devuelve la última incidencia registrada por ese ciudadano para mostrar confirmación
	 */
	public model.IncidenciaDisplayDTO getUltimaIncidencia(String usuario) {
		validateNotBlank(usuario, MSG_USUARIO_OBLIGATORIO);

		String sql =
			"SELECT i.id as id, i.tipo as tipo, i.descripcion as descripcion, i.localizacion as localizacion, "
		  + "       i.fecha_hora_registro as fechaHoraRegistro, i.estado as estado, p.usuario as usuarioCiudadano "
		  + "FROM incidencia i "
		  + "JOIN persona p ON p.id = i.ciudadano_id "
		  + "WHERE p.usuario=? "
		  + "ORDER BY i.id DESC "
		  + "LIMIT 1";

		List<model.IncidenciaDisplayDTO> incidencias =
				db.executeQueryPojo(model.IncidenciaDisplayDTO.class, sql, usuario.trim());

		if (incidencias.isEmpty())
			throw new ApplicationException("No se encontró la incidencia registrada");

		return incidencias.get(0);
	}

	/* ================= VALIDACIONES ================= */

	private void validateNotBlank(String value, String message) {
		if (value == null || value.trim().isEmpty())
			throw new ApplicationException(message);
	}
}