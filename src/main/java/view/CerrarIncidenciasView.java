package view;

import model.IncidenciaDisplayDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CerrarIncidenciasView extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnCerrar;

    // NUEVOS COMPONENTES LOGIN
    private JTextField txtIdentificador;
    private JButton btnAcceder;

    public CerrarIncidenciasView() {
        setTitle("Cerrar incidencias");
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        // PANEL SUPERIOR (LOGIN)
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelTop.add(new JLabel("Correo o DNI:"));

        txtIdentificador = new JTextField(25);
        panelTop.add(txtIdentificador);

        btnAcceder = new JButton("Acceder");
        panelTop.add(btnAcceder);

        add(panelTop, BorderLayout.NORTH);

        // TABLA
        String[] columnas = {"ID", "Tipo", "Descripción", "Zona", "Fecha", "Estado"};

        tableModel = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // PANEL INFERIOR
        JPanel panelBottom = new JPanel();
        btnCerrar = new JButton("Cerrar seleccionadas");
        panelBottom.add(btnCerrar);

        add(panelBottom, BorderLayout.SOUTH);
    }

    // OBTENER IDENTIFICADOR
    public String getIdentificador() {
        return txtIdentificador.getText();
    }

    // ACCIÓN BOTÓN ACCEDER
    public void setAccederAction(ActionListener l) {
        btnAcceder.addActionListener(l);
    }

    // TABLA
    public void loadIncidencias(List<IncidenciaDisplayDTO> lista) {

        tableModel.setRowCount(0);

        for (IncidenciaDisplayDTO i : lista) {
            tableModel.addRow(new Object[]{
                    i.getId(),
                    i.getTipo(),
                    i.getDescripcion(),
                    i.getLocalizacion(),
                    i.getFechaHoraRegistro(),
                    i.getEstado()
            });
        }
    }

    // SELECCIÓN
    public List<Long> getSelectedIds() {

        int[] filas = table.getSelectedRows();
        List<Long> ids = new ArrayList<>();

        for (int fila : filas) {
            Object val = tableModel.getValueAt(fila, 0);

            if (val instanceof Integer) {
                ids.add(((Integer) val).longValue());
            } else if (val instanceof Long) {
                ids.add((Long) val);
            }
        }

        return ids;
    }

    public void setCerrarAction(ActionListener l) {
        btnCerrar.addActionListener(l);
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}