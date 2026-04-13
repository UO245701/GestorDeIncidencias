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

        view.setVisible(true);

        //LOGIN
        view.setAccederAction(e -> {
            try {
                String identificador = view.getIdentificador();

                idTecnico = model.loginTecnico(identificador);
                tipoResponsable = model.getTipoResponsable(idTecnico);

                cargarIncidencias();

            } catch (ApplicationException ex) {
                view.showMessage(ex.getMessage());
            }
        });

        // CIERRE
        view.setCerrarAction(e -> cerrar());
    }

    private void cargarIncidencias() {

        if (tipoResponsable == null) {
            return; // evita llamadas antes del login
        }

        List<IncidenciaDisplayDTO> lista =
                model.getIncidenciasResueltas(tipoResponsable);

        view.loadIncidencias(lista);
    }

    private void cerrar() {

        if (tipoResponsable == null) {
            view.showMessage("Debes iniciar sesión primero");
            return;
        }

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