package Model;

import java.util.List;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

/**
 * Modelo para la HU_33818 - Registrar Incidencia
 * Sigue exactamente el mismo patrón que CarrerasModel
 */
public class IncidenciasModel {

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
		validateNotNull(usuario, MSG_USUARIO_OBLIGATORIO);

		String sql = "SELECT id FROM persona WHERE usuario=? AND tipo='CIUDADANO'";
		List<Object[]> rows = db.executeQueryArray(sql, usuario);

		if (rows.isEmpty())
			throw new ApplicationException("No existe un ciudadano identificado con ese usuario");

		return (int) rows.get(0)[0];
	}

	/**
	 * Inserta una nueva incidencia
	 */
	public void registrarIncidencia(String usuario, String tipo, String descripcion, String localizacion) {

		validateNotNull(usuario, MSG_USUARIO_OBLIGATORIO);
		validateNotNull(tipo, MSG_TIPO_OBLIGATORIO);
		validateNotNull(descripcion, MSG_DESCRIPCION_OBLIGATORIA);
		validateNotNull(localizacion, MSG_LOCALIZACION_OBLIGATORIA);

		int ciudadanoId = getCiudadanoId(usuario);

		String sql = "INSERT INTO incidencia(tipo, descripcion, localizacion, fecha_hora_registro, estado, ciudadano_id) "
				+ "VALUES (?, ?, ?, datetime('now'), 'Nueva', ?)";

		db.executeUpdate(sql, tipo, descripcion, localizacion, ciudadanoId);
	}

	/**
	 * Devuelve la última incidencia registrada por ese ciudadano
	 * (para mostrar confirmación)
	 */
	public IncidenciaDisplayDTO getUltimaIncidencia(String usuario) {

		String sql = """
			SELECT i.id, i.tipo, i.descripcion, i.localizacion, 
			       i.fecha_hora_registro as fechaHoraRegistro, 
			       i.estado, p.usuario as usuarioCiudadano
			FROM incidencia i
			JOIN persona p ON p.id = i.ciudadano_id
			WHERE p.usuario=?
			ORDER BY i.id DESC
			LIMIT 1
		""";

		List<IncidenciaDisplayDTO> incidencias =
				db.executeQueryPojo(IncidenciaDisplayDTO.class, sql, usuario);

		if (incidencias.isEmpty())
			throw new ApplicationException("No se encontró la incidencia registrada");

		return incidencias.get(0);
	}

	/* ================= VALIDACIONES ================= */

	private void validateNotNull(String value, String message) {
		if (value == null || value.trim().isEmpty())
			throw new ApplicationException(message);
	}
}