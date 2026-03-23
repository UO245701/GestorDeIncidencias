package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import util.TextAreaRenderer;

import java.awt.BorderLayout;

public class RegistrarTrabajoView {

    private JFrame frame;
    private JTextField txtIdentificacion;
    private JButton btnCargar;
    private JTable tablaIncidencias;
    private DefaultTableModel tableModel;
    private JTable tablaTrabajos;
    private DefaultTableModel trabajosModel;
    private JTextArea txtNuevoTrabajo;
    private JButton btnAñadir;

    public RegistrarTrabajoView() {
        initialize();
    }

    private void initialize() {

        frame = new JFrame();
        frame.setTitle("Registrar trabajos");
        frame.setBounds(0, 0, 900, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // 🔹 PANEL PRINCIPAL VERTICAL
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // =======================
        //TOP (IDENTIFICACIÓN)
        // =======================
        JPanel top = new JPanel();

        txtIdentificacion = new JTextField(20);
        btnCargar = new JButton("Cargar incidencias");

        top.add(new JLabel("Email o DNI:"));
        top.add(txtIdentificacion);
        top.add(btnCargar);

        mainPanel.add(top);

        // =======================
        //INCIDENCIAS
        // =======================
        JPanel panelIncidencias = new JPanel(new BorderLayout());
        panelIncidencias.setBorder(BorderFactory.createTitledBorder("Incidencias en curso"));

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Tipo", "Descripción", "Estado"}, 0
        );

        tablaIncidencias = new JTable(tableModel);

        panelIncidencias.add(new JScrollPane(tablaIncidencias), BorderLayout.CENTER);
        panelIncidencias.setPreferredSize(new java.awt.Dimension(800, 200));

        mainPanel.add(panelIncidencias);

        // =======================
        //TRABAJOS PREVIOS
        // =======================
        JPanel panelTrabajos = new JPanel(new BorderLayout());
        panelTrabajos.setBorder(BorderFactory.createTitledBorder("Trabajos registrados"));

        trabajosModel = new DefaultTableModel(
                new Object[]{"Fecha", "Descripción"}, 0
        );

        tablaTrabajos = new JTable(trabajosModel);
        
        tablaTrabajos.getColumnModel()
        .getColumn(1) // columna descripción
        .setCellRenderer(new TextAreaRenderer());
        
        tablaTrabajos.getColumnModel().getColumn(0).setPreferredWidth(150);
        tablaTrabajos.getColumnModel().getColumn(1).setPreferredWidth(600);

        panelTrabajos.add(new JScrollPane(tablaTrabajos), BorderLayout.CENTER);
        panelTrabajos.setPreferredSize(new java.awt.Dimension(800, 150));

        mainPanel.add(panelTrabajos);

        // =======================
        //NUEVO TRABAJO
        // =======================
        JPanel panelNuevo = new JPanel(new BorderLayout());
        panelNuevo.setBorder(BorderFactory.createTitledBorder("Nuevo trabajo"));

        txtNuevoTrabajo = new JTextArea(3, 20);
        btnAñadir = new JButton("Añadir trabajo");

        panelNuevo.add(new JScrollPane(txtNuevoTrabajo), BorderLayout.CENTER);
        panelNuevo.add(btnAñadir, BorderLayout.SOUTH);

        mainPanel.add(panelNuevo);

        //SCROLL GENERAL
        frame.getContentPane().add(new JScrollPane(mainPanel), BorderLayout.CENTER);
    }

    public JFrame getFrame() { return frame; }
    public JTextField getTxtIdentificacion() { return txtIdentificacion; }
    public JButton getBtnCargar() { return btnCargar; }
    public JTable getTablaIncidencias() { return tablaIncidencias; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public DefaultTableModel getTrabajosModel() {return trabajosModel;}
    public JTextArea getTxtNuevoTrabajo() { return txtNuevoTrabajo; }
    public JButton getBtnAñadir() { return btnAñadir; }
    
    
}
