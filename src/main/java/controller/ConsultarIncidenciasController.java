package controller;

import java.util.List;

import model.ConsultarIncidenciasModel;
import model.IncidenciaConsultaDTO;
import util.ApplicationException;
import view.ConsultarIncidenciasView;

public class ConsultarIncidenciasController {
	
	private final ConsultarIncidenciasModel model;
	private final ConsultarIncidenciasView view;

	private Integer ciudadanoId = null;

	public ConsultarIncidenciasController(ConsultarIncidenciasModel model, ConsultarIncidenciasView view) {
		this.model = model;
		this.view = view;
	}

	public void initController() {
		// cargar estados desde BD
		view.setEstados(model.getEstadosDisponibles());

		view.addBuscarListener(() -> buscar());
		view.addEstadoChangedListener(() -> recargarSiYaHayCiudadano());

		view.setVisible(true);
	}

	private void buscar() {
		try {
			String ident = view.getIdentificador();
			ciudadanoId = model.getCiudadanoId(ident);

			cargarTabla();

		} catch (ApplicationException ex) {
			view.showError(ex.getMessage());
		} catch (Exception ex) {
			view.showError("Error inesperado consultando incidencias: " + ex.getMessage());
		}
	}

	private void recargarSiYaHayCiudadano() {
		// Si aún no se ha buscado ciudadano, no hacemos nada
		if (ciudadanoId == null) return;

		try {
			cargarTabla();
		} catch (ApplicationException ex) {
			view.showError(ex.getMessage());
		} catch (Exception ex) {
			view.showError("Error inesperado consultando incidencias: " + ex.getMessage());
		}
	}

	private void cargarTabla() {
		String estado = view.getEstadoSeleccionado();
		List<IncidenciaConsultaDTO> lista = model.getIncidencias(ciudadanoId, estado);
		view.mostrarIncidencias(lista);
	}
}
