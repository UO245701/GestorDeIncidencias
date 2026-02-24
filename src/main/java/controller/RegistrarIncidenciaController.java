package controller;

import model.IncidenciaDisplayDTO;
import model.RegistrarIncidenciasModel;
import util.ApplicationException;
import view.RegistrarIncidenciaView;

/**
 * Controller HU_33818 - Registrar Incidencia
 * Adaptado al modelo estilo CarrerasModel
 */
public class RegistrarIncidenciaController {

	private RegistrarIncidenciasModel model;
	private RegistrarIncidenciaView view;

	public RegistrarIncidenciaController(RegistrarIncidenciasModel model, RegistrarIncidenciaView view) {
		this.model = model;
		this.view = view;
		// NO inicializamos aquí; seguimos el patrón del ejemplo con initController()
	}

	public void initController() {
		view.addRegistrarListener(() -> registrarIncidencia());
		view.setVisible(true);
	}

	private void registrarIncidencia() {
		try {
			String usuario = view.getUsuario();
			String tipo = view.getTipo();
			String descripcion = view.getDescripcion();
			String localizacion = view.getLocalizacion();

			// Este método ya valida obligatorios y valida que sea ciudadano
			model.registrarIncidencia(usuario, tipo, descripcion, localizacion);

			// Recuperar lo registrado para confirmación
			IncidenciaDisplayDTO dto = model.getUltimaIncidencia(usuario);

			view.showConfirmacion(dto);

		} catch (ApplicationException ex) {
			view.showError(ex.getMessage());
		} catch (Exception ex) {
			view.showError("Error inesperado registrando la incidencia: " + ex.getMessage());
		}
	}
}