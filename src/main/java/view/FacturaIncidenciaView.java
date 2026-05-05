package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import model.FacturaIncidenciaDTO;

public class FacturaIncidenciaView extends JFrame {

    private final JTextField txtIdentificador = new JTextField(25);
    private final JButton btnAcceder = new JButton("Cargar incidencias");
    private final JButton btnGenerar = new JButton("Generar factura");
    private final DefaultTableModel tableModel;
    private final JTable table;

    public FacturaIncidenciaView() {
        setTitle("Generacion y emision de facturas");
        setSize(1000, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTop.add(new JLabel("Email o DNI operador:"));
        panelTop.add(txtIdentificador);
        panelTop.add(btnAcceder);
        add(panelTop, BorderLayout.NORTH);

        String[] columnas = {
                "ID", "Tipo", "Descripcion", "Fecha cierre/registro", "Tecnico",
                "Tiempo real", "Coste materiales", "Coste total"
        };

        tableModel = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(280);
        table.getColumnModel().getColumn(4).setPreferredWidth(160);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBottom.add(btnGenerar);
        add(panelBottom, BorderLayout.SOUTH);
    }

    public String getIdentificador() {
        return txtIdentificador.getText();
    }

    public void setAccederAction(ActionListener listener) {
        btnAcceder.addActionListener(listener);
    }

    public void setGenerarAction(ActionListener listener) {
        btnGenerar.addActionListener(listener);
    }

    public void loadIncidencias(List<FacturaIncidenciaDTO> incidencias) {
        tableModel.setRowCount(0);

        for (FacturaIncidenciaDTO incidencia : incidencias) {
            tableModel.addRow(new Object[] {
                    incidencia.getId(),
                    incidencia.getTipo(),
                    incidencia.getDescripcion(),
                    incidencia.getFechaHoraRegistro(),
                    incidencia.getTecnico(),
                    incidencia.getTiempoReal() == null ? 0 : incidencia.getTiempoReal(),
                    incidencia.getCosteMateriales() == null ? 0.0 : incidencia.getCosteMateriales(),
                    incidencia.getCosteTotal() == null ? 0.0 : incidencia.getCosteTotal()
            });
        }
    }

    public Long getSelectedId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            return null;
        }

        Object value = tableModel.getValueAt(row, 0);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return Long.parseLong(value.toString());
    }

    public void showFacturaGenerada(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Factura emitida", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showMessage(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Acceso confirmado", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
