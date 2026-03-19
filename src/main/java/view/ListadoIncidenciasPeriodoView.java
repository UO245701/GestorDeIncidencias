package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

public class ListadoIncidenciasPeriodoView extends JFrame {

    private JTextField txtUsuario = new JTextField(25);

    // ✅ CAMBIO: usar JDateChooser
    private JDateChooser dcFechaInicio = new JDateChooser();
    private JDateChooser dcFechaFin = new JDateChooser();

    private JButton btnValidarUsuario = new JButton("Validar");
    private JButton btnBuscar = new JButton("Buscar incidencias");

    private JLabel lblMensajeUsuario = new JLabel(" ");

    private DefaultTableModel tableModel = new DefaultTableModel(
            new Object[] { "ID", "Fecha/Hora", "Tipo", "Localización", "Estado", "Descripción", "Usuario" }, 0
    ) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JTable tabla = new JTable(tableModel);

    public ListadoIncidenciasPeriodoView() {
        super("Gestor de incidencias - Listado por periodo");
        buildUI();
    }

    private void buildUI() {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);

        tabla.setFillsViewportHeight(true);

        // formato fecha
        dcFechaInicio.setDateFormatString("yyyy-MM-dd");
        dcFechaFin.setDateFormatString("yyyy-MM-dd");

        JPanel pnlFiltro = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.insets = new Insets(5, 5, 5, 5);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        // Usuario
        gc.gridx = 0;
        gc.gridy = 0;
        pnlFiltro.add(new JLabel("Usuario (email o DNI):"), gc);

        gc.gridx = 1;
        gc.weightx = 1.0;
        pnlFiltro.add(txtUsuario, gc);

        gc.gridx = 2;
        gc.weightx = 0;
        pnlFiltro.add(btnValidarUsuario, gc);

        // mensaje
        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = 2;
        pnlFiltro.add(lblMensajeUsuario, gc);
        gc.gridwidth = 1;

        // Fecha inicio
        gc.gridx = 0;
        gc.gridy = 2;
        pnlFiltro.add(new JLabel("Fecha inicio:"), gc);

        gc.gridx = 1;
        pnlFiltro.add(dcFechaInicio, gc);

        // Fecha fin
        gc.gridx = 0;
        gc.gridy = 3;
        pnlFiltro.add(new JLabel("Fecha fin:"), gc);

        gc.gridx = 1;
        pnlFiltro.add(dcFechaFin, gc);

        gc.gridx = 2;
        pnlFiltro.add(btnBuscar, gc);

        JScrollPane spTabla = new JScrollPane(tabla);
        spTabla.setPreferredSize(new Dimension(950, 320));

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        main.add(pnlFiltro, BorderLayout.NORTH);
        main.add(spTabla, BorderLayout.CENTER);

        setContentPane(main);
    }

    // ===== getters =====

    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public JDateChooser getDcFechaInicio() {
        return dcFechaInicio;
    }

    public JDateChooser getDcFechaFin() {
        return dcFechaFin;
    }

    public JButton getBtnValidarUsuario() {
        return btnValidarUsuario;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public void setMensajeUsuario(String texto) {
        lblMensajeUsuario.setText(texto);
    }

    public void limpiarTabla() {
        tableModel.setRowCount(0);
    }

    public void addFila(Object[] fila) {
        tableModel.addRow(fila);
    }

    public JTable getTabla() {
        return tabla;
    }
}