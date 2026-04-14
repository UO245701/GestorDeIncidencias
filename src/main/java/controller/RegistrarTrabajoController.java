package controller;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import model.RegistrarTrabajoModel;
import model.IncidenciaListadoDTO;
import model.HistorialDTO;
import view.RegistrarTrabajoView;
import util.ApplicationException;

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

        DefaultTableModel table = view.getTrabajosModel();
        table.setRowCount(0);

        for (HistorialDTO h : trabajos) {
            table.addRow(new Object[]{
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

            String texto = view.getTxtNuevoTrabajo().getText().trim();
            String fecha = view.getTxtFecha().getText().trim();

            // Validación descripción
            if (texto.isEmpty()) {
                throw new ApplicationException("La descripción del trabajo es obligatoria");
            }

            // Validación fecha REAL
            if (fecha.isEmpty()) {
                throw new ApplicationException("La fecha es obligatoria");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            sdf.setLenient(false); // clave

            Date fechaParseada;

            try {
                fechaParseada = sdf.parse(fecha);
            } catch (Exception e) {
                throw new ApplicationException(
                    "Fecha inválida. Formato correcto: yyyy-MM-dd HH:mm (ej: 2026-03-25 18:30)"
                );
            }

            // Guardar (usamos el string original ya validado)
            model.addTrabajo(idIncidencia, idTecnico, texto, fecha);

            view.getTxtNuevoTrabajo().setText("");
            view.getTxtFecha().setText("");

            mostrarTrabajos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
