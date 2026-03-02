package controller;

import model.*;
import view.AsignarIncidenciaView;
import util.SwingUtil;

import java.util.List;
import javax.swing.JOptionPane;

public class AsignarIncidenciaController {

	private AsignarIncidenciaModel model;
	private AsignarIncidenciaView view;
	private PersonaEntity operadorActual;

	public AsignarIncidenciaController(AsignarIncidenciaModel model, AsignarIncidenciaView view) {
		this.model = model;
		this.view = view;
	}

	public void initController() {
		view.getBtnLogin().addActionListener(e -> SwingUtil.exceptionWrapper(() -> cargarDatos()));
		view.getBtnAsignar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> ejecutarAsignacion()));
		view.setVisible(true);
	}

	private void cargarDatos() {
		String email = view.getEmail();
		if (email == null || email.trim().isEmpty()) {
			JOptionPane.showMessageDialog(view, "Por favor, introduzca un email");
			return;
		}

		operadorActual = model.getOperadorByEmail(email);

		if (operadorActual == null) {
			JOptionPane.showMessageDialog(view, "No se encontró un operador con ese email");
			return;
		}

		List<IncidenciaDisplayDTO> incidencias = model.getIncidenciasValidadas();
		view.setTablaIncidencias(SwingUtil.getTableModelFromPojos(
				incidencias,
				new String[] { "id", "tipo", "descripcion", "localizacion", "fechaHoraRegistro", "usuarioCiudadano" }
		));

		List<PersonaEntity> tecnicos = model.getTecnicos();
		view.setTablaTecnicos(SwingUtil.getTableModelFromPojos(
				tecnicos,
				new String[] { "id", "usuario", "nombre", "apellidos", "email" }
		));
	}

	private void ejecutarAsignacion() {
		if (operadorActual == null) {
			JOptionPane.showMessageDialog(view, "Debe identificarse primero con su email");
			return;
		}

		int rowInc = view.getTablaIncidencias().getSelectedRow();
		if (rowInc == -1) {
			JOptionPane.showMessageDialog(view, "Debe seleccionar una incidencia de la tabla");
			return;
		}

		int rowTec = view.getTablaTecnicos().getSelectedRow();
		if (rowTec == -1) {
			JOptionPane.showMessageDialog(view, "Debe seleccionar un técnico");
			return;
		}

		int idIncidencia = Integer.parseInt(view.getTablaIncidencias().getValueAt(rowInc, 0).toString());
		int idTecnico = Integer.parseInt(view.getTablaTecnicos().getValueAt(rowTec, 0).toString());
		String nombreTecnico = view.getTablaTecnicos().getValueAt(rowTec, 2).toString();
		String nombreOperadora = operadorActual.getNombre();
		String detalleHistorial = "La operadora " + nombreOperadora +
		                           " asignó el trabajo a " + nombreTecnico;

		// 1) Asignar
		model.asignarIncidencia(idIncidencia, idTecnico);

		// 2) Historial (operadorActual.getId() existe en tu PersonaEntity)
		model.registrarEnHistorial(idIncidencia, operadorActual.getId(), detalleHistorial);

		JOptionPane.showMessageDialog(view,
				"Incidencia " + idIncidencia + " asignada correctamente al técnico " + nombreTecnico);

		// refrescar
		cargarDatos();
	}
}