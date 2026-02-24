package view;

import javax.swing.*;

import model.IncidenciaDisplayDTO;

import java.awt.*;

public class RegistrarIncidenciaView extends JFrame {

    private final JTextField txtUsuario = new JTextField(25);
    private final JComboBox<String> cbTipo = new JComboBox<>(new String[] {
            "", "alumbrado", "limpieza", "mobiliario urbano", "zonas verdes", "señalización", "calzada"
    });
    private final JTextArea txtDescripcion = new JTextArea(5, 30);
    private final JTextField txtLocalizacion = new JTextField(30);

    private final JButton btnRegistrar = new JButton("Registrar incidencia");

    private final JTextArea txtConfirmacion = new JTextArea(8, 45);

    public RegistrarIncidenciaView() {
        super("Gestor de incidencias - Registrar incidencia");
        buildUI();
    }

    private void buildUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        txtConfirmacion.setEditable(false);
        txtConfirmacion.setLineWrap(true);
        txtConfirmacion.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Usuario
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        form.add(new JLabel("Correo o DNI:"), gbc);
        gbc.gridx = 1; gbc.gridy = y; gbc.weightx = 1;
        form.add(txtUsuario, gbc);
        y++;

        // Tipo
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        form.add(new JLabel("Tipo incidencia:"), gbc);
        gbc.gridx = 1; gbc.gridy = y; gbc.weightx = 1;
        form.add(cbTipo, gbc);
        y++;

        // Descripción
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.gridy = y; gbc.weightx = 1;
        form.add(new JScrollPane(txtDescripcion), gbc);
        y++;

        // Localización
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        form.add(new JLabel("Localización:"), gbc);
        gbc.gridx = 1; gbc.gridy = y; gbc.weightx = 1;
        form.add(txtLocalizacion, gbc);
        y++;

        // Botón
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(btnRegistrar);

        // Confirmación
        JPanel confirm = new JPanel(new BorderLayout(6, 6));
        confirm.setBorder(BorderFactory.createTitledBorder("Confirmación"));
        confirm.add(new JScrollPane(txtConfirmacion), BorderLayout.CENTER);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        main.add(form, BorderLayout.NORTH);
        main.add(buttons, BorderLayout.CENTER);
        main.add(confirm, BorderLayout.SOUTH);

        setContentPane(main);
        pack();
        setLocationRelativeTo(null);
    }

    // Getters para el Controller
    public String getUsuario() { return txtUsuario.getText(); }
    public String getTipo() { return (String) cbTipo.getSelectedItem(); }
    public String getDescripcion() { return txtDescripcion.getText(); }
    public String getLocalizacion() { return txtLocalizacion.getText(); }

    public void addRegistrarListener(Runnable action) {
        btnRegistrar.addActionListener(e -> action.run());
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "OK", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showConfirmacion(IncidenciaDisplayDTO dto) {
        String text = ""
                + "Incidencia registrada correctamente.\n\n"
                + "ID: " + dto.getId() + "\n"
                + "Fecha/Hora: " + dto.getFechaHoraRegistro() + "\n"
                + "Estado: " + dto.getEstado() + "\n"
                + "Ciudadano: " + dto.getUsuarioCiudadano() + "\n"
                + "Tipo: " + dto.getTipo() + "\n"
                + "Descripción: " + dto.getDescripcion() + "\n"
                + "Localización: " + dto.getLocalizacion() + "\n";
        txtConfirmacion.setText(text);
    }
}
