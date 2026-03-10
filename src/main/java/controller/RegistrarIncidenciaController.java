package controller;

import java.util.List;

import model.IncidenciaDisplayDTO;
import model.RegistrarIncidenciasModel;
import util.ApplicationException;
import view.RegistrarIncidenciaView;

public class RegistrarIncidenciaController {

    private RegistrarIncidenciasModel model;
    private RegistrarIncidenciaView view;

    public RegistrarIncidenciaController(RegistrarIncidenciasModel model, RegistrarIncidenciaView view) {
        this.model = model;
        this.view = view;
    }

    public void initController() {
        view.addValidarUsuarioListener(() -> validarUsuario());
        view.addRegistrarListener(() -> registrarIncidencia());

        cargarZonas();
        view.deshabilitarFormularioIncidencia();
        view.setVisible(true);
    }

    private void cargarZonas() {
        try {
            List<String> zonas = model.getZonas();
            view.setZonas(zonas);
        } catch (Exception ex) {
            view.showError("Error cargando las zonas: " + ex.getMessage());
        }
    }

    private void validarUsuario() {
        try {
            String usuario = view.getUsuario();

            if (model.esCiudadanoRegistrado(usuario)) {
                view.habilitarFormularioIncidencia();
                view.showInfo("Ciudadano validado correctamente. Ya puede registrar la incidencia.");
            } else {
                view.deshabilitarFormularioIncidencia();
                view.limpiarFormularioIncidencia();
                view.showError("No existe un ciudadano registrado con ese correo o DNI.");
            }

        } catch (ApplicationException ex) {
            view.deshabilitarFormularioIncidencia();
            view.limpiarFormularioIncidencia();
            view.showError(ex.getMessage());
        } catch (Exception ex) {
            view.showError("Error inesperado validando el usuario: " + ex.getMessage());
        }
    }

    private void registrarIncidencia() {
        try {
            String usuario = view.getUsuario();
            String tipo = view.getTipo();
            String descripcion = view.getDescripcion();
            String localizacion = view.getLocalizacion();

            model.registrarIncidencia(usuario, tipo, descripcion, localizacion);

            IncidenciaDisplayDTO dto = model.getUltimaIncidencia(usuario);
            view.showConfirmacion(dto);

        } catch (ApplicationException ex) {
            view.showError(ex.getMessage());
        } catch (Exception ex) {
            view.showError("Error inesperado registrando la incidencia: " + ex.getMessage());
        }
    }
}