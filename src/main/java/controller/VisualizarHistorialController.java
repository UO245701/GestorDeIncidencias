package controller;

import java.util.List;

import model.VisualizarHistorialModel;
import model.HistorialDTO;
import model.IncidenciaListadoDTO;
import util.ApplicationException;
import view.VisualizarHistorialView;

public class VisualizarHistorialController {

    private final VisualizarHistorialModel model;
    private final VisualizarHistorialView view;

    public VisualizarHistorialController(VisualizarHistorialModel model,
                                         VisualizarHistorialView view) {
        this.model = model;
        this.view = view;
    }

    public void initController() {

        view.addAccederListener(() -> acceder());
        view.addIncidenciaSelectionListener(() -> cargarHistorial());

        view.setVisible(true);
    }

    private void acceder() {
        try {
            model.validarOperadorOTecnico(view.getIdentificador());
            List<IncidenciaListadoDTO> lista = model.getIncidencias();
            view.mostrarIncidencias(lista);
        } catch (ApplicationException ex) {
            view.showError(ex.getMessage());
        }
    }

    private void cargarHistorial() {
        int id = view.getIncidenciaSeleccionada();
        if (id == -1) return;

        List<HistorialDTO> lista = model.getHistorial(id);
        view.mostrarHistorial(lista);
    }
}
