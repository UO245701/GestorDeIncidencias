package controller;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import model.RegistrarTrabajoModel;
import model.IncidenciaListadoDTO;
import model.HistorialDTO;
import view.RegistrarTrabajoView;

public class RegistrarTrabajoController {

    private RegistrarTrabajoModel model;
    private RegistrarTrabajoView view;

    private long idTecnico;

    public RegistrarTrabajoController(RegistrarTrabajoModel m, RegistrarTrabajoView v) {
        this.model = m;
        this.view = v;
    }

    public void initController() {

        view.getBtnCargar().addActionListener(e -> cargarIncidencias());

        view.getTablaIncidencias().getSelectionModel()
            .addListSelectionListener(e -> mostrarTrabajos());

        view.getBtnAñadir().addActionListener(e -> añadirTrabajo());
    }

    private void cargarIncidencias() {
        try {
            String input = view.getTxtIdentificacion().getText();

            idTecnico = model.getIdTecnico(input);

            List<IncidenciaListadoDTO> lista = model.getIncidencias(idTecnico);

            DefaultTableModel table = view.getTableModel();
            table.setRowCount(0);

            for (IncidenciaListadoDTO i : lista) {
                table.addRow(new Object[]{
                        i.getId(),
                        i.getTipo(),
                        i.getDescripcion(),
                        i.getEstado()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void mostrarTrabajos() {

        int fila = view.getTablaIncidencias().getSelectedRow();
        if (fila == -1) return;

        long idIncidencia = Long.parseLong(
                view.getTableModel().getValueAt(fila, 0).toString()
        );

        List<HistorialDTO> trabajos = model.getTrabajos(idIncidencia);

        DefaultTableModel model = view.getTrabajosModel();
        model.setRowCount(0); // limpiar tabla

        for (HistorialDTO h : trabajos) {
            model.addRow(new Object[]{
                h.getFechaHora(),
                h.getDetalle()
            });
        }
    }

    private void añadirTrabajo() {
        try {
            int fila = view.getTablaIncidencias().getSelectedRow();
            if (fila == -1) return;

            long idIncidencia = Long.parseLong(
                    view.getTableModel().getValueAt(fila, 0).toString()
            );

            String texto = view.getTxtNuevoTrabajo().getText();

            model.addTrabajo(idIncidencia, idTecnico, texto);

            view.getTxtNuevoTrabajo().setText("");

            mostrarTrabajos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
