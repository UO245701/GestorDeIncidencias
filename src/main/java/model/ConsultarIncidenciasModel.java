package model;

import java.util.ArrayList;
import java.util.List;

import util.ApplicationException;
import util.Database;

public class ConsultarIncidenciasModel {

	private static final String MSG_IDENTIFICADOR_OBLIGATORIO = "El correo o DNI es obligatorio";

	private final Database db = new Database();

	/**
	 * Devuelve el id_persona del ciudadano si existe. Busca por usuario (correo) o DNI.
	 */
	public int getCiudadanoId(String identificador) {
		validateNotBlank(identificador, MSG_IDENTIFICADOR_OBLIGATORIO);

		String sql = ""
				+ "SELECT id_persona "
				+ "FROM Persona "
				+ "WHERE tipo='CIUDADANO' AND (email=? OR dni=?)";

		String id = identificador.trim();
		List<Object[]> rows = db.executeQueryArray(sql, id, id);

		if (rows.isEmpty()) {
			throw new ApplicationException("No existe un ciudadano identificado con ese correo/DNI");
		}

		Object value = rows.get(0)[0];
		if (value instanceof Number) return ((Number) value).intValue();
		return Integer.parseInt(value.toString());
	}

	/**
	 * Devuelve incidencias del ciudadano, opcionalmente filtradas por estado.
	 * Si estado es null o "TODAS", devuelve todas.
	 */
	public List<IncidenciaConsultaDTO> getIncidencias(int ciudadanoId, String estado) {

		boolean filtrar = estado != null && !estado.trim().isEmpty() && !"TODAS".equalsIgnoreCase(estado.trim());

		if (!filtrar) {
			String sql = ""
					+ "SELECT id_incidencia AS id, "
					+ "       tipo AS tipo, "
					+ "       descripcion AS descripcion, "
					+ "       fecha_hora AS fechaHora, "
					+ "       estado AS estado "
					+ "FROM Incidencia "
					+ "WHERE fk_ciudadano = ? "
					+ "ORDER BY fecha_hora DESC";

			return db.executeQueryPojo(IncidenciaConsultaDTO.class, sql, ciudadanoId);
		}

		String sql = ""
				+ "SELECT id_incidencia AS id, "
				+ "       tipo AS tipo, "
				+ "       descripcion AS descripcion, "
				+ "       fecha_hora AS fechaHora, "
				+ "       estado AS estado "
				+ "FROM Incidencia "
				+ "WHERE fk_ciudadano = ? AND estado = ? "
				+ "ORDER BY fecha_hora DESC";

		return db.executeQueryPojo(IncidenciaConsultaDTO.class, sql, ciudadanoId, estado.trim());
	}

	/**
	 * Devuelve lista de estados disponibles en la BD (para poblar el combo).
	 * Incluye "TODAS" al principio.
	 */
	public List<String> getEstadosDisponibles() {
		List<String> estados = new ArrayList<>();
		estados.add("TODAS");

		String sql = "SELECT DISTINCT estado FROM Incidencia ORDER BY estado";
		List<Object[]> rows = db.executeQueryArray(sql);

		for (Object[] r : rows) {
			if (r != null && r.length > 0 && r[0] != null) {
				estados.add(r[0].toString());
			}
		}
		return estados;
	}

	private void validateNotBlank(String value, String message) {
		if (value == null || value.trim().isEmpty()) {
			throw new ApplicationException(message);
		}
	}
}
