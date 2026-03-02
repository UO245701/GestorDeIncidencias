package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

public class AsignarIncidenciaView extends JFrame {

    private JTextField txtEmail;
    private JButton btnLogin;
    private JButton btnAsignar;

    private JTable tableIncidencias;
    private JTable tableTecnicos;

    public AsignarIncidenciaView() {
        setTitle("Operador - Asignar incidencia a técnico");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1150, 620);
        setLocationRelativeTo(null);

        // ===== TOP: Email + login =====
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        top.add(new JLabel("Email operador:"));
        txtEmail = new JTextField(25);
        top.add(txtEmail);

        btnLogin = new JButton("Login / Cargar");
        top.add(btnLogin);

        // ===== CENTER: tablas con títulos =====
        tableIncidencias = new JTable();
        tableTecnicos = new JTable();

        tableIncidencias.setRowHeight(22);
        tableTecnicos.setRowHeight(22);

        JPanel panelIncidencias = new JPanel(new BorderLayout());
        panelIncidencias.add(new JLabel("Incidencias VALIDADA (ordenadas por fecha, antiguas primero)"), BorderLayout.NORTH);
        panelIncidencias.add(new JScrollPane(tableIncidencias), BorderLayout.CENTER);

        JPanel panelTecnicos = new JPanel(new BorderLayout());
        panelTecnicos.add(new JLabel("Técnicos disponibles"), BorderLayout.NORTH);
        panelTecnicos.add(new JScrollPane(tableTecnicos), BorderLayout.CENTER);

        JPanel center = new JPanel(new GridLayout(1, 2, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        center.add(panelIncidencias);
        center.add(panelTecnicos);

        // ===== BOTTOM: botón asignar =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        btnAsignar = new JButton("Asignar");
        bottom.add(btnAsignar);

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    // ===== Métodos que usa el controller =====

    public String getEmail() {
        return txtEmail.getText();
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JButton getBtnAsignar() {
        return btnAsignar;
    }

    public JTable getTablaIncidencias() {
        return tableIncidencias;
    }

    public JTable getTablaTecnicos() {
        return tableTecnicos;
    }

    public void setTablaIncidencias(TableModel model) {
        tableIncidencias.setModel(model);

        // Cambiar títulos si existen esas columnas
        if (tableIncidencias.getColumnCount() >= 6) {
            tableIncidencias.getColumnModel().getColumn(0).setHeaderValue("ID");
            tableIncidencias.getColumnModel().getColumn(1).setHeaderValue("Tipo");
            tableIncidencias.getColumnModel().getColumn(2).setHeaderValue("Descripción");
            tableIncidencias.getColumnModel().getColumn(3).setHeaderValue("Localización");
            tableIncidencias.getColumnModel().getColumn(4).setHeaderValue("Fecha");
            tableIncidencias.getColumnModel().getColumn(5).setHeaderValue("Ciudadano");
        }

        // Centrar la columna ID
        if (tableIncidencias.getColumnCount() > 0) {
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            tableIncidencias.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        }

        tableIncidencias.getTableHeader().repaint();
        tableIncidencias.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    public void setTablaTecnicos(TableModel model) {
        tableTecnicos.setModel(model);

        if (tableTecnicos.getColumnCount() >= 5) {
            tableTecnicos.getColumnModel().getColumn(0).setHeaderValue("ID");
            tableTecnicos.getColumnModel().getColumn(1).setHeaderValue("Usuario");
            tableTecnicos.getColumnModel().getColumn(2).setHeaderValue("Nombre");
            tableTecnicos.getColumnModel().getColumn(3).setHeaderValue("Apellidos");
            tableTecnicos.getColumnModel().getColumn(4).setHeaderValue("Email");
        }

        // Centrar la columna ID
        if (tableTecnicos.getColumnCount() > 0) {
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            tableTecnicos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        }

        tableTecnicos.getTableHeader().repaint();
        tableTecnicos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
}