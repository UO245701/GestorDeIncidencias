package view;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

public class ValidarIncidenciaView extends JDialog {
	private JTable tableIncidencias = new JTable();
	private JComboBox<String> cbTipos = new JComboBox<>(new String[] {"Bache", "Farola", "Limpieza", "Mobiliario"});
	private JButton btnValidar = new JButton("Validar Incidencia");
	private JTextField txtEmail = new JTextField(20);
	private JButton btnLogin = new JButton("Cargar Incidencias");

	public ValidarIncidenciaView() {
		setTitle("Validación de Incidencias - Operador");
		setSize(800, 400);
		getContentPane().setLayout(new BorderLayout());

		// Panel superior: Login por email
		JPanel panelLogin = new JPanel();
		panelLogin.add(new JLabel("Email Operador:"));
		panelLogin.add(txtEmail);
		panelLogin.add(btnLogin);
		getContentPane().add(panelLogin, BorderLayout.NORTH);

		// Centro: Tabla
		getContentPane().add(new JScrollPane(tableIncidencias), BorderLayout.CENTER);

		// Sur: Controles de validación
		JPanel panelAcciones = new JPanel();
		panelAcciones.add(new JLabel("Tipo final:"));
		panelAcciones.add(cbTipos);
		panelAcciones.add(btnValidar);
		getContentPane().add(panelAcciones, BorderLayout.SOUTH);
	}

	// Getters para el controlador
	public String getEmail() { return txtEmail.getText(); }
	public JTable getTable() { return tableIncidencias; }
	public String getTipoSeleccionado() { return cbTipos.getSelectedItem().toString(); }
	public JButton getBtnLogin() { return btnLogin; }
	public JButton getBtnValidar() { return btnValidar; }
	
	public void setTableModel(TableModel model) {
		tableIncidencias.setModel(model);
	}
}