package controller;

import model.InformeMensualModel;
import model.InformeTecnicoDTO;
import view.InformeMensualView;
import util.SwingUtil;
import java.util.List;
import javax.swing.JOptionPane;

public class InformeMensualController {
    
    private InformeMensualModel model;
    private InformeMensualView view;

    public InformeMensualController(InformeMensualModel model, InformeMensualView view) {
        this.model = model;
        this.view = view;
    }

    public void initController() {
        view.getBtnGenerar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> generarInforme()));
        view.setVisible(true);
    }

    private void generarInforme() {
        List<InformeTecnicoDTO> informe = model.getInformeUltimoMes();
        
        if (informe.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No hay incidencias resueltas en el último mes.", "Sin datos", JOptionPane.INFORMATION_MESSAGE);
        }

        // Cargamos los datos mapeando los nombres exactos de los atributos del DTO
        view.setTableModel(SwingUtil.getTableModelFromPojos(informe, 
                new String[] {"nombreTecnico", "incidenciasResueltas", "tiempoTotal"}));
    }
}