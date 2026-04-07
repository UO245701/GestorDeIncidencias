package controller;

import java.util.List;

import javax.swing.JOptionPane;

import model.HistorialDTO;
import model.IncidenciaDisplayDTO;
import model.ReaperturaIncidenciasModel;
import util.SwingUtil;
import view.ReaperturaIncidenciasView;

public class ReaperturaIncidenciasController {

    private ReaperturaIncidenciasModel model;
    private ReaperturaIncidenciasView view;

    private String identificadorValidado = null;

    public ReaperturaIncidenciasController(ReaperturaIncidenciasModel model, ReaperturaIncidenciasView view) {
        this.model = model;
        this.view = view;
    }

    public void initController() {
        view.getBtnValidarUsuario().addActionListener(
                e -> SwingUtil.exceptionWrapper(() -> validarUsuario()));

        view.getBtnCargarDetalle().addActionListener(
                e -> SwingUtil.exceptionWrapper(() -> cargarDetalleIncidencia()));

        view.getBtnReabrir().addActionListener(
                e -> SwingUtil.exceptionWrapper(() -> reabrirIncidencia()));

        view.setVisible(true);
    }

    private void validarUsuario() {
        String identificador = view.getUsuario();

        if (identificador == null || identificador.trim().isEmpty()) {
            view.showError("Debe introducir un correo o DNI.");
            return;
        }

        if (!model.esCiudadanoRegistrado(identificador)) {
            view.showError("No existe un ciudadano registrado con ese correo o DNI.");
            return;
        }

        identificadorValidado = identificador.trim();
        cargarListadoIncidencias();
        view.showMessage("Usuario validado correctamente.");
    }

    private void cargarListadoIncidencias() {
        if (identificadorValidado == null) {
            view.showError("Primero debe validar un usuario.");
            return;
        }

        List<IncidenciaDisplayDTO> incidencias = model.getIncidenciasCiudadano(identificadorValidado);

        view.limpiarTabla();
        view.limpiarHistorial();
        view.limpiarDetalle();
        view.setReabrirEnabled(false);

        for (IncidenciaDisplayDTO i : incidencias) {
            view.addIncidenciaRow(new Object[] {
                    i.getId(),
                    i.getTipo(),
                    i.getDescripcion(),
                    i.getLocalizacion(),
                    i.getFechaHoraRegistro(),
                    i.getEstado()
            });
        }

        if (incidencias.isEmpty()) {
            view.showMessage("El ciudadano no tiene incidencias reabribles.");
        }
    }

    private void cargarDetalleIncidencia() {
        if (identificadorValidado == null) {
            view.showError("Primero debe validar un usuario.");
            return;
        }

        int idIncidencia = view.getSelectedIncidenciaId();
        if (idIncidencia == -1) {
            view.showError("Debe seleccionar una incidencia.");
            return;
        }

        IncidenciaDisplayDTO incidencia = model.getIncidenciaSeleccionada(idIncidencia, identificadorValidado);
        List<HistorialDTO> historial = model.getHistorialIncidencia(idIncidencia, identificadorValidado);

        view.limpiarHistorial();

        for (HistorialDTO h : historial) {
            view.addHistorialRow(new Object[] {
                    h.getFechaHora(),
                    h.getEstado(),
                    h.getId(),
                    h.getDetalle()
            });
        }

        boolean reabrible = "RECHAZADA".equalsIgnoreCase(incidencia.getEstado())
                || "CERRADA".equalsIgnoreCase(incidencia.getEstado());

        view.setReabrirEnabled(reabrible);
    }

    private void reabrirIncidencia() {
        if (identificadorValidado == null) {
            view.showError("Primero debe validar un usuario.");
            return;
        }

        int idIncidencia = view.getSelectedIncidenciaId();
        if (idIncidencia == -1) {
            view.showError("Debe seleccionar una incidencia.");
            return;
        }

        String motivo = view.getMotivoReapertura();
        if (motivo == null || motivo.trim().isEmpty()) {
            view.showError("Debe indicar el motivo de la reapertura.");
            return;
        }

        if (!model.puedeReabrirse(idIncidencia, identificadorValidado)) {
            view.showError("La incidencia no está en estado reabrible o no pertenece al ciudadano.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "¿Desea reabrir la incidencia seleccionada?",
                "Confirmar reapertura",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        model.reabrirIncidencia(idIncidencia, identificadorValidado, motivo);

        view.showMessage("La incidencia ha sido reabierta correctamente.");
        cargarListadoIncidencias();
    }
}