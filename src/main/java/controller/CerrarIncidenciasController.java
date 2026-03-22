package controller;

import java.util.List;

import model.CerrarIncidenciasModel;
import model.IncidenciaDisplayDTO;
import util.ApplicationException;
import view.CerrarIncidenciasView;

public class CerrarIncidenciasController {

    private CerrarIncidenciasModel model;
    private CerrarIncidenciasView view;

    private int idTecnico;
    private String tipoResponsable;

    public CerrarIncidenciasController(CerrarIncidenciasModel m, CerrarIncidenciasView v) {
        this.model = m;
        this.view = v;
    }

    public void initController() {

        try {
            String identificador = view.pedirIdentificador();

            idTecnico = model.loginTecnico(identificador);
            tipoResponsable = model.getTipoResponsable(idTecnico);

            view.setVisible(true);

            cargarIncidencias();

            view.setCerrarAction(e -> cerrar());

        } catch (ApplicationException e) {
            view.showMessage(e.getMessage());
        }
    }

    private void cargarIncidencias() {
        List<IncidenciaDisplayDTO> lista =
                model.getIncidenciasResueltas(tipoResponsable);

        view.loadIncidencias(lista);
    }

    private void cerrar() {

        List<Long> ids = view.getSelectedIds();

        if (ids.isEmpty()) {
            view.showMessage("Selecciona al menos una incidencia");
            return;
        }

        model.cerrarIncidencias(ids, idTecnico);

        view.showMessage("Incidencias cerradas correctamente");

        cargarIncidencias();
    }
}
