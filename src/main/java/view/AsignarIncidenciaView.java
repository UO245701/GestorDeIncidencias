package view;

import javax.swing.*;
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
        setSize(1100, 600);
        setLocationRelativeTo(null);

        // ====== TOP: Email + login ======
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Email operador:"));
        txtEmail = new JTextField(25);
        top.add(txtEmail);

        btnLogin = new JButton("Login / Cargar");
        top.add(btnLogin);

        // ====== CENTER: 2 tablas ======
        tableIncidencias = new JTable();
        tableTecnicos = new JTable();

        JPanel center = new JPanel(new GridLayout(1, 2, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        center.add(new JScrollPane(tableIncidencias));
        center.add(new JScrollPane(tableTecnicos));

        // ====== BOTTOM: botón asignar ======
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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
    }

    public void setTablaTecnicos(TableModel model) {
        tableTecnicos.setModel(model);
    }
}