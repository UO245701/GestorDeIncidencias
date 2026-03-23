package view;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

public class InformeMensualView extends JFrame {
    
    private JButton btnGenerar = new JButton("Generar Informe Último Mes");
    private JTable tablaInforme = new JTable();

    public InformeMensualView() {
        setTitle("Responsable - Informe Mensual de Técnicos");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelTop = new JPanel();
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelTop.add(new JLabel("Visualizar rendimiento de técnicos: "));
        panelTop.add(btnGenerar);

        add(panelTop, BorderLayout.NORTH);
        
        // Tabla con un poco de margen
        JPanel panelCenter = new JPanel(new BorderLayout());
        panelCenter.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        panelCenter.add(new JScrollPane(tablaInforme), BorderLayout.CENTER);
        
        add(panelCenter, BorderLayout.CENTER);
    }

    public JButton getBtnGenerar() { return btnGenerar; }
    public JTable getTablaInforme() { return tablaInforme; }
    
    public void setTableModel(TableModel model) {
        tablaInforme.setModel(model);
        
        // Personalizamos las cabeceras una vez que el modelo está cargado
        if (tablaInforme.getColumnCount() == 3) {
            tablaInforme.getColumnModel().getColumn(0).setHeaderValue("Técnico");
            tablaInforme.getColumnModel().getColumn(1).setHeaderValue("Incidencias Resueltas");
            tablaInforme.getColumnModel().getColumn(2).setHeaderValue("Tiempo Total (horas)");
        }
        tablaInforme.getTableHeader().repaint();
    }
}