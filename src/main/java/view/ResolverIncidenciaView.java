package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ResolverIncidenciaView extends JFrame {

    private JTextField txtCorreoTecnico = new JTextField(20);
    private JButton btnCargar = new JButton("Cargar incidencias");

    private DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Tipo", "Descripción", "Localización", "Fecha", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JTable tblIncidencias = new JTable(tableModel);

    private JTextField txtTiempoReal = new JTextField(10);
    private JTextArea txtTrabajos = new JTextArea(5, 30);
    private JButton btnResolver = new JButton("Marcar como resuelta");

    public ResolverIncidenciaView() {
        super("Gestor de incidencias - Resolver incidencia");
        buildUI();
    }

    private void buildUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel pNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pNorth.add(new JLabel("Correo técnico:"));
        pNorth.add(txtCorreoTecnico);
        pNorth.add(btnCargar);

        JScrollPane spTabla = new JScrollPane(tblIncidencias);

        JPanel pForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        pForm.add(new JLabel("Tiempo real empleado (min):"), gbc);

        gbc.gridx = 1;
        pForm.add(txtTiempoReal, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pForm.add(new JLabel("Trabajos realizados:"), gbc);

        gbc.gridx = 1;
        txtTrabajos.setLineWrap(true);
        txtTrabajos.setWrapStyleWord(true);
        pForm.add(new JScrollPane(txtTrabajos), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        pForm.add(btnResolver, gbc);

        getContentPane().setLayout(new BorderLayout());
        add(pNorth, BorderLayout.NORTH);
        add(spTabla, BorderLayout.CENTER);
        add(pForm, BorderLayout.SOUTH);
    }

    public JButton getBtnCargar() {
        return btnCargar;
    }

    public JButton getBtnResolver() {
        return btnResolver;
    }

    public String getCorreoTecnico() {
        return txtCorreoTecnico.getText();
    }

    public String getTiempoReal() {
        return txtTiempoReal.getText();
    }

    public String getTrabajosRealizados() {
        return txtTrabajos.getText();
    }

    public JTable getTblIncidencias() {
        return tblIncidencias;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public int getIncidenciaSeleccionada() {
        int fila = tblIncidencias.getSelectedRow();
        if (fila == -1) {
            return -1;
        }
        return Integer.parseInt(tableModel.getValueAt(fila, 0).toString());
    }

    public void limpiarFormulario() {
        txtTiempoReal.setText("");
        txtTrabajos.setText("");
    }
}