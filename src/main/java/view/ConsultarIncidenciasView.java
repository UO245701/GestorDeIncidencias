package view;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.IncidenciaConsultaDTO;
import util.TableColumnAdjuster;

public class ConsultarIncidenciasView extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private final JTextField txtIdentificador = new JTextField(25);
	private final JButton btnBuscar = new JButton("Buscar");

	private final JComboBox<String> cbEstado = new JComboBox<>(new String[] { "TODAS" });

	private final DefaultTableModel tableModel = new DefaultTableModel(
			new String[] { "Identificador", "Tipo", "Descripción", "Fecha/Hora", "Estado" }, 0) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};

	private final JTable table = new JTable(tableModel);

	public ConsultarIncidenciasView() {
		super("Gestor de incidencias - Consultar mis incidencias");
		buildUI();
	}

	private void buildUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel form = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 6, 6, 6);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		int y = 0;

		// Identificador
		gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
		form.add(new JLabel("Correo o DNI:"), gbc);

		gbc.gridx = 1; gbc.gridy = y; gbc.weightx = 1;
		form.add(txtIdentificador, gbc);

		gbc.gridx = 2; gbc.gridy = y; gbc.weightx = 0;
		form.add(btnBuscar, gbc);
		y++;

		// Filtro estado
		gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
		form.add(new JLabel("Filtrar por estado:"), gbc);

		gbc.gridx = 1; gbc.gridy = y; gbc.weightx = 1;
		form.add(cbEstado, gbc);
		y++;

		JPanel top = new JPanel(new BorderLayout());
		top.setBorder(BorderFactory.createTitledBorder("Consulta"));
		top.add(form, BorderLayout.CENTER);

		JPanel center = new JPanel(new BorderLayout(6, 6));
		center.setBorder(BorderFactory.createTitledBorder("Incidencias"));
		center.add(new JScrollPane(table), BorderLayout.CENTER);

		JPanel main = new JPanel(new BorderLayout(10, 10));
		main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		main.add(top, BorderLayout.NORTH);
		main.add(center, BorderLayout.CENTER);

		setContentPane(main);
		setSize(900, 420);
		setLocationRelativeTo(null);
	}

	// ====== API para el Controller ======

	public String getIdentificador() {
		return txtIdentificador.getText();
	}

	public String getEstadoSeleccionado() {
		Object sel = cbEstado.getSelectedItem();
		return sel == null ? "TODAS" : sel.toString();
	}

	public void setEstados(List<String> estados) {
		cbEstado.removeAllItems();
		for (String e : estados) cbEstado.addItem(e);
		cbEstado.setSelectedItem("TODAS");
	}

	public void addBuscarListener(Runnable action) {
		btnBuscar.addActionListener(e -> action.run());
	}

	public void addEstadoChangedListener(Runnable action) {
		cbEstado.addActionListener(e -> action.run());
	}

	public void mostrarIncidencias(List<IncidenciaConsultaDTO> incidencias) {
		tableModel.setRowCount(0);

		for (IncidenciaConsultaDTO dto : incidencias) {
			tableModel.addRow(new Object[] {
					dto.getId(),
					dto.getTipo(),
					dto.getDescripcion(),
					dto.getFechaHora(),
					dto.getEstado()
			});
		}

		// Ajusta columnas para que no parezca hecho con odio
		new TableColumnAdjuster(table).adjustColumns();
	}

	public void showError(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void showInfo(String msg) {
		JOptionPane.showMessageDialog(this, msg, "OK", JOptionPane.INFORMATION_MESSAGE);
	}
}	
