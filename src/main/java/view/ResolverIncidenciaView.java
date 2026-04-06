package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ResolverIncidenciaView extends JFrame {

    private JTextField txtCorreoTecnico = new JTextField(20);
    private JButton btnCargar = new JButton("Cargar incidencias");

    private DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{
                    "ID",
                    "Tipo",
                    "Descripción",
                    "Localización",
                    "Fecha",
                    "Estado",
                    "Horas prev.",
                    "Trabajos prev."
            }, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JTable tblIncidencias = new JTable(tableModel);

    private JTextField txtTiempoReal = new JTextField(10);
    private JTextArea txtTrabajos = new JTextArea(5, 30);
    private JButton btnResolver = new JButton("Marcar como resuelta");
    
    private JTextField txtCosteMateriales = new JTextField("0.0", 10);
    private JTextArea txtDescMateriales = new JTextArea(2, 30);

    public ResolverIncidenciaView() {
        super("Gestor de incidencias - Resolver incidencia");
        buildUI();
    }

    private void buildUI() {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel pNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));

        pNorth.add(new JLabel("Correo técnico:"));
        pNorth.add(txtCorreoTecnico);
        pNorth.add(btnCargar);

        JScrollPane spTabla = new JScrollPane(tblIncidencias);

        // PANEL SUR
        // Formulario Resolucion
        JPanel pForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        pForm.add(new JLabel("Tiempo real empleado (horas):"), gbc);

        gbc.gridx = 1;
        pForm.add(txtTiempoReal, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pForm.add(new JLabel("Trabajos realizados:"), gbc);

        gbc.gridx = 1;
        txtTrabajos.setLineWrap(true);
        txtTrabajos.setWrapStyleWord(true);

        JScrollPane spTrabajos = new JScrollPane(txtTrabajos);
        pForm.add(spTrabajos, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        pForm.add(btnResolver, gbc);
        
     // Formulario Costes
        JPanel pCostes = new JPanel(new GridBagLayout());
        pCostes.setBorder(BorderFactory.createTitledBorder("Gestión de Costes (Solo para estado Resuelta)"));
        
        gbc.gridx = 0; gbc.gridy = 0;
        pCostes.add(new JLabel("Coste materiales/externos (€):"), gbc);
        gbc.gridx = 1;
        pCostes.add(txtCosteMateriales, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pCostes.add(new JLabel("Descripción de los cargos:"), gbc);
        gbc.gridx = 1;
        txtDescMateriales.setLineWrap(true);
        txtDescMateriales.setWrapStyleWord(true);
        pCostes.add(new JScrollPane(txtDescMateriales), gbc);

        // Unimos los dos sub-paneles
        JPanel pSurFinal = new JPanel(new BorderLayout(5, 5));
        pSurFinal.add(pForm, BorderLayout.NORTH);
        pSurFinal.add(pCostes, BorderLayout.CENTER);
        
        JPanel pBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pBoton.add(btnResolver);
        pSurFinal.add(pBoton, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout());

        add(pNorth, BorderLayout.NORTH);
        add(spTabla, BorderLayout.CENTER);
        add(pSurFinal, BorderLayout.SOUTH);
    }

    public JButton getBtnCargar() {
        return btnCargar;
    }

    public JButton getBtnResolver() {
        return btnResolver;
    }

    public String getCorreoTecnico() {
        return txtCorreoTecnico.getText();
    }

    public String getTiempoReal() {
        return txtTiempoReal.getText();
    }

    public String getTrabajosRealizados() {
        return txtTrabajos.getText();
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
    public String getCosteMateriales() { 
    	return txtCosteMateriales.getText(); 
    }
    
    public String getDescMateriales() { 
    	return txtDescMateriales.getText(); 
    }

    public int getIncidenciaSeleccionada() {

        int fila = tblIncidencias.getSelectedRow();

        if (fila == -1) {
            return -1;
        }

        return Integer.parseInt(
                tableModel.getValueAt(fila, 0).toString()
        );
    }

    public Object[] getFilaSeleccionada() {

        int fila = tblIncidencias.getSelectedRow();

        if (fila == -1) {
            return null;
        }

        return new Object[] {
                tableModel.getValueAt(fila, 0),
                tableModel.getValueAt(fila, 1),
                tableModel.getValueAt(fila, 2),
                tableModel.getValueAt(fila, 3),
                tableModel.getValueAt(fila, 4),
                tableModel.getValueAt(fila, 5),
                tableModel.getValueAt(fila, 6),
                tableModel.getValueAt(fila, 7)
        };
    }

    public void limpiarFormularioResolucion() {
        txtTiempoReal.setText("");
        txtTrabajos.setText("");
        txtCosteMateriales.setText("");
        txtDescMateriales.setText("");
    }
}