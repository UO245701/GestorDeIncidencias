package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.HistorialDTO;
import model.IncidenciaListadoDTO;
import util.TableColumnAdjuster;

public class VisualizarHistorialView extends JFrame {

    private JTextField txtIdentificador = new JTextField(20);
    private JButton btnAcceder = new JButton("Acceder");

    private DefaultTableModel modelIncidencias = new DefaultTableModel(
            new String[]{"ID", "Tipo", "Descripción", "Fecha/Hora", "Estado"}, 0);

    private DefaultTableModel modelHistorial = new DefaultTableModel(
            new String[]{"Fecha/Hora", "ID Cambio", "Estado"}, 0);

    private JTable tableIncidencias = new JTable(modelIncidencias);
    private JTable tableHistorial = new JTable(modelHistorial);

    public VisualizarHistorialView() {
        super("Visualizar historial de incidencia");
        buildUI();
    }

    private void buildUI() {

        JPanel top = new JPanel();
        top.add(new JLabel("Correo o DNI:"));
        top.add(txtIdentificador);
        top.add(btnAcceder);

        JPanel center = new JPanel(new GridLayout(2, 1));
        center.add(new JScrollPane(tableIncidencias));
        center.add(new JScrollPane(tableHistorial));

        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public String getIdentificador() {
        return txtIdentificador.getText();
    }

    public void addAccederListener(Runnable action) {
        btnAcceder.addActionListener(e -> action.run());
    }

    public void addIncidenciaSelectionListener(Runnable action) {
        tableIncidencias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) action.run();
        });
    }

    public int getIncidenciaSeleccionada() {
        int row = tableIncidencias.getSelectedRow();
        if (row == -1) return -1;
        return (int) modelIncidencias.getValueAt(row, 0);
    }

    public void mostrarIncidencias(List<IncidenciaListadoDTO> lista) {
        modelIncidencias.setRowCount(0);
        for (IncidenciaListadoDTO dto : lista) {
            modelIncidencias.addRow(new Object[]{
                    dto.getId(),
                    dto.getTipo(),
                    dto.getDescripcion(),
                    dto.getFechaHora(),
                    dto.getEstado()
            });
        }
        new TableColumnAdjuster(tableIncidencias).adjustColumns();
    }

    public void mostrarHistorial(List<HistorialDTO> lista) {
        modelHistorial.setRowCount(0);
        for (HistorialDTO dto : lista) {
            modelHistorial.addRow(new Object[]{
                    dto.getFechaHora(),
                    dto.getId(),
                    dto.getEstado()
            });
        }
        new TableColumnAdjuster(tableHistorial).adjustColumns();
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
