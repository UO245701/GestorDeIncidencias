package controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.JOptionPane;

import model.IncidenciaDisplayDTO;
import model.ListadoIncidenciasPeriodoModel;
import util.SwingUtil;
import view.ListadoIncidenciasPeriodoView;

public class ListadoIncidenciasPeriodoController {

    private ListadoIncidenciasPeriodoModel model;
    private ListadoIncidenciasPeriodoView view;

    private boolean usuarioValidado = false;

    public ListadoIncidenciasPeriodoController(ListadoIncidenciasPeriodoModel model,
            ListadoIncidenciasPeriodoView view) {
        this.model = model;
        this.view = view;
    }

    public void initController() {
        view.getBtnValidarUsuario().addActionListener(
                e -> SwingUtil.exceptionWrapper(() -> validarUsuario()));

        view.getBtnBuscar().addActionListener(
                e -> SwingUtil.exceptionWrapper(() -> buscarIncidencias()));

        view.setVisible(true);
    }

    private void validarUsuario() {
        String identificador = view.getTxtUsuario().getText().trim();

        if (identificador.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Debe introducir un email o un DNI");
            return;
        }

        boolean registrado = model.esUsuarioRegistrado(identificador);

        if (!registrado) {
            usuarioValidado = false;
            view.setMensajeUsuario("Usuario no registrado");
            return;
        }

        String usuario = model.getNombreUsuario(identificador);
        usuarioValidado = true;
        view.setMensajeUsuario("Usuario registrado: " + usuario);
    }

    private void buscarIncidencias() {
        if (!usuarioValidado) {
            JOptionPane.showMessageDialog(view, "Primero debe validar un usuario registrado");
            return;
        }

        String fechaInicio = view.getTxtFechaInicio().getText().trim();
        String fechaFin = view.getTxtFechaFin().getText().trim();

        if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Debe introducir la fecha de inicio y la fecha de fin");
            return;
        }

        if (!fechasValidas(fechaInicio, fechaFin)) {
            return;
        }

        List<IncidenciaDisplayDTO> incidencias =
                model.getIncidenciasRegistradasEntreFechas(fechaInicio, fechaFin);

        view.limpiarTabla();

        for (IncidenciaDisplayDTO i : incidencias) {
            view.addFila(new Object[] {
                    i.getId(),
                    i.getFechaHoraRegistro(),
                    i.getTipo(),
                    i.getLocalizacion(),
                    i.getEstado(),
                    i.getDescripcion(),
                    i.getUsuarioCiudadano()
            });
        }

        if (incidencias.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No existen incidencias registradas en ese periodo");
        } else {
            JOptionPane.showMessageDialog(view, "Se han encontrado " + incidencias.size() + " incidencias");
        }
    }

    private boolean fechasValidas(String fechaInicio, String fechaFin) {
        try {
            LocalDate inicio = LocalDate.parse(fechaInicio);
            LocalDate fin = LocalDate.parse(fechaFin);

            if (inicio.isAfter(fin)) {
                JOptionPane.showMessageDialog(view, "La fecha de inicio no puede ser posterior a la fecha de fin");
                return false;
            }

            return true;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(view, "Las fechas deben tener formato yyyy-MM-dd");
            return false;
        }
    }
}