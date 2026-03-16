package controller;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import model.IncidenciaDisplayDTO;
import model.ResolverIncidenciaModel;
import util.ApplicationException;
import util.SwingUtil;
import view.ResolverIncidenciaView;

public class ResolverIncidenciaController {

    private ResolverIncidenciaModel model;
    private ResolverIncidenciaView view;
    private int idTecnicoActual;

    public ResolverIncidenciaController(ResolverIncidenciaModel model, ResolverIncidenciaView view) {
        this.model = model;
        this.view = view;
        this.idTecnicoActual = -1;
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

        idTecnicoActual = model.getIdTecnicoByCorreo(correo);

        List<IncidenciaDisplayDTO> incidencias = model.getIncidenciasEnCurso(idTecnicoActual);
        cargarTabla(incidencias);

        if (incidencias.isEmpty()) {
            JOptionPane.showMessageDialog(
                    view,
                    "El técnico identificado no tiene incidencias asignadas en estado EN CURSO.",
                    "Sin incidencias en curso",
                    JOptionPane.INFORMATION_MESSAGE
            );
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
                    i.getEstado(),
                    i.getHorasPrevision(),
                    i.getTrabajosReparacion()
            });
        }
    }

    private void resolverIncidencia() {
        if (idTecnicoActual <= 0) {
            throw new ApplicationException("Primero debe introducir el correo del técnico y cargar sus incidencias");
        }

        int idIncidencia = view.getIncidenciaSeleccionada();
        if (idIncidencia == -1) {
            throw new ApplicationException("Debe seleccionar una incidencia del listado");
        }

        String tiempoTexto = view.getTiempoReal();
        if (tiempoTexto == null || tiempoTexto.trim().isEmpty()) {
            throw new ApplicationException("El tiempo real empleado es obligatorio");
        }

        int tiempoReal;
        try {
            tiempoReal = Integer.parseInt(tiempoTexto.trim());
        } catch (NumberFormatException e) {
            throw new ApplicationException("El tiempo real empleado debe ser un número entero");
        }

        String trabajosRealizados = view.getTrabajosRealizados();
        if (trabajosRealizados == null || trabajosRealizados.trim().isEmpty()) {
            throw new ApplicationException("La descripción de los trabajos realizados es obligatoria");
        }

        Object[] fila = view.getFilaSeleccionada();

        model.resolverIncidencia(idIncidencia, idTecnicoActual, tiempoReal, trabajosRealizados);

        JOptionPane.showMessageDialog(
                view,
                "Incidencia resuelta correctamente.\n\n"
                        + "Datos de la incidencia:\n"
                        + "ID: " + fila[0] + "\n"
                        + "Tipo: " + fila[1] + "\n"
                        + "Descripción: " + fila[2] + "\n"
                        + "Localización: " + fila[3] + "\n"
                        + "Fecha de registro: " + fila[4] + "\n"
                        + "Estado anterior: " + fila[5] + "\n"
                        + "Horas previstas: " + fila[6] + "\n"
                        + "Trabajos de reparación previstos: " + fila[7] + "\n\n"
                        + "Datos registrados en la resolución:\n"
                        + "Tiempo real empleado: " + tiempoReal + " horas\n"
                        + "Trabajos realizados:\n" + trabajosRealizados + "\n\n"
                        + "El estado de la incidencia ha pasado a RESUELTA y el cambio ha sido registrado en el historial.",
                "Incidencia resuelta",
                JOptionPane.INFORMATION_MESSAGE
        );

        view.limpiarFormularioResolucion();

        List<IncidenciaDisplayDTO> incidenciasActualizadas = model.getIncidenciasEnCurso(idTecnicoActual);
        cargarTabla(incidenciasActualizadas);
    }
}