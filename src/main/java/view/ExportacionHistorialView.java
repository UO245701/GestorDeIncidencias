package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import model.IncidenciaExportacionDTO;

public class ExportacionHistorialView extends JFrame {

    public static final String SIN_FILTRO = "Sin filtro";

    private JTextField txtIdentificador = new JTextField(25);
    private JButton btnAcceder = new JButton("Acceder");

    private javax.swing.JComboBox<Object> cmbTipo = new javax.swing.JComboBox<>();
    private javax.swing.JComboBox<Object> cmbZona = new javax.swing.JComboBox<>();
    private JDateChooser dcFechaInicio = new JDateChooser();
    private JDateChooser dcFechaFin = new JDateChooser();
    private JButton btnBuscar = new JButton("Buscar incidencias");
    private JButton btnRestablecerFiltros = new JButton("Restablecer filtros");
    private JButton btnSeleccionarTodas = new JButton("Seleccionar todas");
    private JButton btnExportar = new JButton("Exportar CSV");

    private DefaultTableModel tableModel = new DefaultTableModel(
            new Object[] {
                    "Exportar",
                    "ID",
                    "Tipo",
                    "Zona",
                    "Descripcion",
                    "Estado",
                    "Fecha alta",
                    "Coste total",
                    "Tecnico"
            }, 0) {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 0 ? Boolean.class : Object.class;
        }
    };

    private JTable tblIncidencias = new JTable(tableModel);

    public ExportacionHistorialView() {
        super("Exportacion del historial de incidencias");
        buildUI();
    }

    private void buildUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1150, 620);
        setLocationRelativeTo(null);

        cmbTipo.setEditable(false);
        cmbZona.setEditable(false);
        dcFechaInicio.setDateFormatString("yyyy-MM-dd");
        dcFechaFin.setDateFormatString("yyyy-MM-dd");

        JPanel pAcceso = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pAcceso.setBorder(BorderFactory.createEmptyBorder(8, 10, 4, 10));
        pAcceso.add(new JLabel("Email o DNI operador/tecnico:"));
        pAcceso.add(txtIdentificador);
        pAcceso.add(btnAcceder);

        JPanel pFiltros = new JPanel(new GridBagLayout());
        pFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        pFiltros.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        pFiltros.add(cmbTipo, gbc);

        gbc.gridx = 2;
        pFiltros.add(new JLabel("Zona:"), gbc);
        gbc.gridx = 3;
        pFiltros.add(cmbZona, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pFiltros.add(new JLabel("Fecha inicio:"), gbc);
        gbc.gridx = 1;
        pFiltros.add(dcFechaInicio, gbc);

        gbc.gridx = 2;
        pFiltros.add(new JLabel("Fecha fin:"), gbc);
        gbc.gridx = 3;
        pFiltros.add(dcFechaFin, gbc);

        gbc.gridx = 4;
        pFiltros.add(btnBuscar, gbc);

        gbc.gridx = 5;
        pFiltros.add(btnRestablecerFiltros, gbc);

        JPanel pTop = new JPanel(new BorderLayout());
        pTop.add(pAcceso, BorderLayout.NORTH);
        pTop.add(pFiltros, BorderLayout.CENTER);

        JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pBottom.add(btnSeleccionarTodas);
        pBottom.add(btnExportar);

        tblIncidencias.setFillsViewportHeight(true);

        setLayout(new BorderLayout());
        add(pTop, BorderLayout.NORTH);
        add(new JScrollPane(tblIncidencias), BorderLayout.CENTER);
        add(pBottom, BorderLayout.SOUTH);
    }

    public JButton getBtnAcceder() {
        return btnAcceder;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnRestablecerFiltros() {
        return btnRestablecerFiltros;
    }

    public JButton getBtnSeleccionarTodas() {
        return btnSeleccionarTodas;
    }

    public JButton getBtnExportar() {
        return btnExportar;
    }

    public String getIdentificador() {
        return txtIdentificador.getText();
    }

    public Object getTipoSeleccionado() {
        return cmbTipo.getSelectedItem();
    }

    public Object getZonaSeleccionada() {
        return cmbZona.getSelectedItem();
    }

    public Date getFechaInicio() {
        return dcFechaInicio.getDate();
    }

    public Date getFechaFin() {
        return dcFechaFin.getDate();
    }

    public void setTipos(ComboBoxModel<Object> model) {
        cmbTipo.setModel(model);
        cmbTipo.insertItemAt(SIN_FILTRO, 0);
        cmbTipo.setSelectedIndex(0);
    }

    public void setZonas(ComboBoxModel<Object> model) {
        cmbZona.setModel(model);
        cmbZona.insertItemAt(SIN_FILTRO, 0);
        cmbZona.setSelectedIndex(0);
    }

    public void restablecerFiltros() {
        if (cmbTipo.getItemCount() > 0) {
            cmbTipo.setSelectedIndex(0);
        }
        if (cmbZona.getItemCount() > 0) {
            cmbZona.setSelectedIndex(0);
        }
        dcFechaInicio.setDate(null);
        dcFechaFin.setDate(null);
    }

    public void mostrarIncidencias(List<IncidenciaExportacionDTO> incidencias) {
        tableModel.setRowCount(0);
        for (IncidenciaExportacionDTO incidencia : incidencias) {
            tableModel.addRow(new Object[] {
                    Boolean.FALSE,
                    incidencia.getId(),
                    incidencia.getTipo(),
                    incidencia.getZona(),
                    incidencia.getDescripcion(),
                    incidencia.getEstado(),
                    incidencia.getFechaAlta(),
                    incidencia.getCosteTotal(),
                    incidencia.getTecnico()
            });
        }
    }

    public List<Integer> getIdsSeleccionados() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean seleccionado = (Boolean) tableModel.getValueAt(i, 0);
            if (Boolean.TRUE.equals(seleccionado)) {
                ids.add(Integer.parseInt(tableModel.getValueAt(i, 1).toString()));
            }
        }
        return ids;
    }

    public void seleccionarTodas() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(Boolean.TRUE, i, 0);
        }
    }

    public File seleccionarDestinoCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar historial exportado");
        chooser.setSelectedFile(new File("historial_incidencias.csv"));

        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new File(file.getParentFile(), file.getName() + ".csv");
        }
        return file;
    }
}
