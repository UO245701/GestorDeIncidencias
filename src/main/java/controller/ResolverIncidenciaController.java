package controller;

import model.IncidenciaDisplayDTO;
import model.PersonaEntity;
import model.ResolverIncidenciaModel;
import util.ApplicationException;
import util.SwingUtil;
import view.ResolverIncidenciaView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ResolverIncidenciaController {

    private ResolverIncidenciaModel model;
    private ResolverIncidenciaView view;
    private PersonaEntity tecnicoActual;

    public ResolverIncidenciaController(ResolverIncidenciaModel model, ResolverIncidenciaView view) {
        this.model = model;
        this.view = view;
    }

    public void initController() {
        view.getBtnCargar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> cargarIncidencias()));
        view.getBtnResolver().addActionListener(e -> SwingUtil.exceptionWrapper(() -> resolverIncidencia()));
        view.setVisible(true);
    }

    private void cargarIncidencias() {
        String correo = view.getCorreoTecnico();

        if (correo == null || correo.trim().isEmpty()) {
            throw new ApplicationException("Debe introducir el correo del técnico");
        }

        tecnicoActual = model.getTecnicoByCorreo(correo);

        if (tecnicoActual == null) {
            throw new ApplicationException("No existe un técnico con ese correo");
        }

        List<IncidenciaDisplayDTO> incidencias = model.getIncidenciasEnCurso(tecnicoActual.getId());
        cargarTabla(incidencias);

        if (incidencias.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No hay incidencias en curso asignadas a este técnico");
        }
    }

    private void cargarTabla(List<IncidenciaDisplayDTO> incidencias) {
        DefaultTableModel tm = view.getTableModel();
        tm.setRowCount(0);

        for (IncidenciaDisplayDTO i : incidencias) {
            tm.addRow(new Object[] {
                    i.getId(),
                    i.getTipo(),
                    i.getDescripcion(),
                    i.getLocalizacion(),
                    i.getFechaHoraRegistro(),
                    i.getEstado()
            });
        }
    }

    private void resolverIncidencia() {
        if (tecnicoActual == null) {
            throw new ApplicationException("Primero debe cargar las incidencias del técnico");
        }

        int idIncidencia = view.getIncidenciaSeleccionada();
        if (idIncidencia == -1) {
            throw new ApplicationException("Debe seleccionar una incidencia");
        }

        String tiempoTexto = view.getTiempoReal();
        int tiempoReal;
        try {
            tiempoReal = Integer.parseInt(tiempoTexto.trim());
        } catch (NumberFormatException e) {
            throw new ApplicationException("El tiempo real debe ser un número entero");
        }

        String trabajos = view.getTrabajosRealizados();

        model.marcarComoResuelta(idIncidencia, tiempoReal, trabajos);

        JOptionPane.showMessageDialog(view, "Incidencia marcada como resuelta correctamente");

        view.limpiarFormulario();

        List<IncidenciaDisplayDTO> incidencias = model.getIncidenciasEnCurso(tecnicoActual.getId());
        cargarTabla(incidencias);
    }
}