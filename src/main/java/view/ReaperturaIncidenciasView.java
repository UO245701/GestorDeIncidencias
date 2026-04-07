package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class ReaperturaIncidenciasView extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtUsuario = new JTextField(15);
    private JButton btnValidarUsuario = new JButton("Validar usuario");
    private JButton btnCargarDetalle = new JButton("Ver detalle");
    private JButton btnReabrir = new JButton("Reabrir incidencia");

    private JTable tblIncidencias;
    private DefaultTableModel incidenciasTableModel;

    private JTable tblHistorial;
    private DefaultTableModel historialTableModel;

    private JTextArea txtMotivo = new JTextArea(4, 20);

    public ReaperturaIncidenciasView() {
        setTitle("Reapertura de incidencias");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel pnlSuperior = new JPanel();
        pnlSuperior.setBorder(BorderFactory.createTitledBorder("Validación ciudadano"));
        pnlSuperior.add(new JLabel("Usuario:"));
        pnlSuperior.add(txtUsuario);
        pnlSuperior.add(btnValidarUsuario);
        add(pnlSuperior, BorderLayout.NORTH);

        incidenciasTableModel = new DefaultTableModel(
                new Object[] { "ID", "Tipo", "Descripción", "Localización", "Fecha", "Estado" }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblIncidencias = new JTable(incidenciasTableModel);
        tblIncidencias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTablaIncidencias = new JScrollPane(tblIncidencias);
        scrollTablaIncidencias.setBorder(BorderFactory.createTitledBorder("Listado de incidencias"));

        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.add(scrollTablaIncidencias, BorderLayout.CENTER);

        JPanel pnlBotonDetalle = new JPanel();
        pnlBotonDetalle.add(btnCargarDetalle);
        pnlCentro.add(pnlBotonDetalle, BorderLayout.SOUTH);

        add(pnlCentro, BorderLayout.CENTER);

        historialTableModel = new DefaultTableModel(
                new Object[] { "Fecha/Hora", "Estado/Acción", "Usuario", "Comentario" }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblHistorial = new JTable(historialTableModel);
        tblHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollHistorial = new JScrollPane(tblHistorial);
        scrollHistorial.setBorder(BorderFactory.createTitledBorder("Historial de estados y comentarios"));
        scrollHistorial.setPreferredSize(new Dimension(900, 180));

        JPanel pnlInferior = new JPanel(new BorderLayout(10, 10));
        pnlInferior.setBorder(BorderFactory.createTitledBorder("Detalle de incidencia"));
        pnlInferior.add(scrollHistorial, BorderLayout.CENTER);

        JPanel pnlMotivo = new JPanel(new BorderLayout());
        JScrollPane scrollMotivo = new JScrollPane(txtMotivo);
        scrollMotivo.setBorder(BorderFactory.createTitledBorder("Motivo de reapertura"));
        pnlMotivo.add(scrollMotivo, BorderLayout.CENTER);

        JPanel pnlAcciones = new JPanel();
        btnReabrir.setEnabled(false);
        pnlAcciones.add(btnReabrir);
        pnlMotivo.add(pnlAcciones, BorderLayout.SOUTH);

        pnlInferior.add(pnlMotivo, BorderLayout.SOUTH);
        pnlInferior.setPreferredSize(new Dimension(1000, 280));

        add(pnlInferior, BorderLayout.SOUTH);
    }

    public void limpiarTabla() {
        incidenciasTableModel.setRowCount(0);
    }

    public void addIncidenciaRow(Object[] row) {
        incidenciasTableModel.addRow(row);
    }

    public int getSelectedIncidenciaId() {
        int row = tblIncidencias.getSelectedRow();
        if (row == -1) {
            return -1;
        }
        return Integer.parseInt(incidenciasTableModel.getValueAt(row, 0).toString());
    }

    public void limpiarHistorial() {
        historialTableModel.setRowCount(0);
    }

    public void addHistorialRow(Object[] row) {
        historialTableModel.addRow(row);
    }

    public void limpiarDetalle() {
        txtMotivo.setText("");
        btnReabrir.setEnabled(false);
    }

    public void setReabrirEnabled(boolean enabled) {
        btnReabrir.setEnabled(enabled);
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getMotivoReapertura() {
        return txtMotivo.getText().trim();
    }

    public JButton getBtnValidarUsuario() {
        return btnValidarUsuario;
    }

    public JButton getBtnCargarDetalle() {
        return btnCargarDetalle;
    }

    public JButton getBtnReabrir() {
        return btnReabrir;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}