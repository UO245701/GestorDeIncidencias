package view;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

public class TecnicoView extends JFrame {
    private JTextField txtEmail = new JTextField(20);
    private JButton btnLogin = new JButton("Cargar mis Incidencias");
    private JTable tablaIncidencias = new JTable();
    private JTextField txtHoras = new JTextField(5);
    private JTextArea txtTrabajos = new JTextArea(3, 20);
    private JButton btnAnotar = new JButton("Confirmar Previsión y Empezar");

    public TecnicoView() {
        setTitle("Gestión de Incidencias - Técnico");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel Superior (Login)
        JPanel north = new JPanel();
        north.add(new JLabel("Email Técnico:"));
        north.add(txtEmail);
        north.add(btnLogin);
        add(north, BorderLayout.NORTH);

        // Centro (Tabla)
        add(new JScrollPane(tablaIncidencias), BorderLayout.CENTER);

        // Panel Inferior (Formulario de previsión)
        JPanel south = new JPanel(new GridLayout(3, 1));
        JPanel p1 = new JPanel(); p1.add(new JLabel("Horas estimadas:")); p1.add(txtHoras);
        JPanel p2 = new JPanel(); p2.add(new JLabel("Trabajos necesarios:")); p2.add(new JScrollPane(txtTrabajos));
        south.add(p1); south.add(p2); south.add(btnAnotar);
        add(south, BorderLayout.SOUTH);
    }

    // Getters para el controlador
    public String getEmail() { return txtEmail.getText(); }
    public JButton getBtnLogin() { return btnLogin; }
    public JButton getBtnAnotar() { return btnAnotar; }
    public JTable getTable() { return tablaIncidencias; }
    public String getHoras() { return txtHoras.getText(); }
    public String getTrabajos() { return txtTrabajos.getText(); }
    public void setTableModel(TableModel model) { tablaIncidencias.setModel(model); }
}