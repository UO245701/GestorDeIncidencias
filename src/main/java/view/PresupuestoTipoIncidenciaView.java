package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import com.toedter.calendar.JDateChooser;

public class PresupuestoTipoIncidenciaView extends JFrame {

    private JTextField txtEmailOperador = new JTextField(25);
    private JButton btnLogin = new JButton("Login / Cargar");

    private JComboBox<Object> cmbTipo = new JComboBox<>();
    private JTextField txtImporteMaximo = new JTextField(12);
    private JDateChooser dcFechaInicio = new JDateChooser();
    private JDateChooser dcFechaFin = new JDateChooser();
    private JCheckBox chkAnioNatural = new JCheckBox("Periodo predeterminado de un año desde la fecha inicial", true);
    private JButton btnCrear = new JButton("Crear presupuesto");

    private JTable tblPresupuestos = new JTable();

    public PresupuestoTipoIncidenciaView() {
        super("Operador - Presupuestos por tipo de incidencia");
        buildUI();
    }

    private void buildUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1050, 620);
        setLocationRelativeTo(null);

        JPanel pNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pNorth.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        pNorth.add(new JLabel("Email operador:"));
        pNorth.add(txtEmailOperador);
        pNorth.add(btnLogin);

        JPanel pForm = new JPanel(new GridBagLayout());
        pForm.setBorder(BorderFactory.createTitledBorder("Nuevo presupuesto"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbTipo.setEditable(true);
        configurarSelectorFecha(dcFechaInicio);
        configurarSelectorFecha(dcFechaFin);

        gbc.gridx = 0;
        gbc.gridy = 0;
        pForm.add(new JLabel("Tipo de incidencia:"), gbc);
        gbc.gridx = 1;
        pForm.add(cmbTipo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pForm.add(new JLabel("Importe maximo:"), gbc);
        gbc.gridx = 1;
        pForm.add(txtImporteMaximo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        pForm.add(new JLabel("Fecha inicial:"), gbc);
        gbc.gridx = 1;
        pForm.add(dcFechaInicio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        pForm.add(new JLabel("Fecha final:"), gbc);
        gbc.gridx = 1;
        pForm.add(dcFechaFin, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        pForm.add(chkAnioNatural, gbc);

        JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pButtons.add(btnCrear);

        JPanel pSouth = new JPanel(new BorderLayout());
        pSouth.add(pForm, BorderLayout.CENTER);
        pSouth.add(pButtons, BorderLayout.SOUTH);

        JPanel pCenter = new JPanel(new BorderLayout());
        pCenter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pCenter.add(new JLabel("Presupuestos configurados"), BorderLayout.NORTH);
        pCenter.add(new JScrollPane(tblPresupuestos), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(pNorth, BorderLayout.NORTH);
        add(pCenter, BorderLayout.CENTER);
        add(pSouth, BorderLayout.SOUTH);
    }

    private void configurarSelectorFecha(JDateChooser dateChooser) {
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setDate(new Date());
    }

    public String getEmailOperador() {
        return txtEmailOperador.getText();
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JButton getBtnCrear() {
        return btnCrear;
    }

    public JComboBox<Object> getCmbTipo() {
        return cmbTipo;
    }

    public Object getTipoSeleccionado() {
        return cmbTipo.getSelectedItem();
    }

    public String getImporteMaximo() {
        return txtImporteMaximo.getText();
    }

    public Date getFechaInicio() {
        return dcFechaInicio.getDate();
    }

    public Date getFechaFin() {
        return dcFechaFin.getDate();
    }

    public void setFechaFin(Date fechaFin) {
        dcFechaFin.setDate(fechaFin);
    }

    public JCheckBox getChkAnioNatural() {
        return chkAnioNatural;
    }

    public JDateChooser getDcFechaInicio() {
        return dcFechaInicio;
    }

    public JDateChooser getDcFechaFin() {
        return dcFechaFin;
    }

    public JTable getTblPresupuestos() {
        return tblPresupuestos;
    }

    public void setTablaPresupuestos(TableModel model) {
        tblPresupuestos.setModel(model);
        if (tblPresupuestos.getColumnCount() >= 8) {
            tblPresupuestos.getColumnModel().getColumn(0).setHeaderValue("ID");
            tblPresupuestos.getColumnModel().getColumn(1).setHeaderValue("Tipo");
            tblPresupuestos.getColumnModel().getColumn(2).setHeaderValue("Maximo");
            tblPresupuestos.getColumnModel().getColumn(3).setHeaderValue("Consumido");
            tblPresupuestos.getColumnModel().getColumn(4).setHeaderValue("Disponible");
            tblPresupuestos.getColumnModel().getColumn(5).setHeaderValue("Inicio");
            tblPresupuestos.getColumnModel().getColumn(6).setHeaderValue("Fin");
            tblPresupuestos.getColumnModel().getColumn(7).setHeaderValue("Activo");
        }
        tblPresupuestos.getTableHeader().repaint();
    }

    public int getPresupuestoSeleccionado() {
        int fila = tblPresupuestos.getSelectedRow();
        if (fila == -1) {
            return -1;
        }
        return Integer.parseInt(tblPresupuestos.getValueAt(fila, 0).toString());
    }

    public void limpiarFormulario() {
        txtImporteMaximo.setText("");
    }
}
