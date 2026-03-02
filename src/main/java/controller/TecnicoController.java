package controller;

import model.*;
import view.TecnicoView;
import util.SwingUtil;
import javax.swing.JOptionPane;
import java.util.List;

public class TecnicoController {
    private TecnicoModel model;
    private TecnicoView view;
    private PersonaEntity tecnicoActual;

    public TecnicoController(TecnicoModel model, TecnicoView view) {
        this.model = model;
        this.view = view;
    }

    public void initController() {
        view.getBtnLogin().addActionListener(e -> SwingUtil.exceptionWrapper(() -> cargarIncidencias()));
        view.getBtnAnotar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> ejecutarPrevision()));
        view.setVisible(true);
    }

    private void cargarIncidencias() {
        tecnicoActual = model.getTecnicoByEmail(view.getEmail());
        if (tecnicoActual == null) {
            JOptionPane.showMessageDialog(view, "No se encontró un técnico con ese email");
            return;
        }
        List<IncidenciaDisplayDTO> lista = model.getIncidenciasAsignadas(tecnicoActual.getId());
        view.setTableModel(SwingUtil.getTableModelFromPojos(lista, 
                new String[] {"id", "tipo", "descripcion", "localizacion", "estado"}));
    }

    private void ejecutarPrevision() {
        if (tecnicoActual == null) {
            JOptionPane.showMessageDialog(view, "Identifíquese primero.");
            return;
        }

        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Seleccione una incidencia.");
            return;
        }

        // --- NUEVA VALIDACIÓN DE NÚMEROS ---
        int horas;
        try {
            String textoHoras = view.getHoras().trim();
            if (textoHoras.isEmpty()) {
                JOptionPane.showMessageDialog(view, "El campo de horas no puede estar vacío.");
                return;
            }
            horas = Integer.parseInt(textoHoras);
            
            if (horas <= 0) {
                JOptionPane.showMessageDialog(view, "La previsión de horas debe ser un número positivo.");
                return;
            }
        } catch (NumberFormatException e) {
            // Si el usuario puso letras o símbolos, entrará aquí
            JOptionPane.showMessageDialog(view, "Por favor, introduzca un número entero válido para las horas.");
            return; 
        }
        // -----------------------------------

        int idIncidencia = Integer.parseInt(view.getTable().getValueAt(row, 0).toString());
        String trabajos = view.getTrabajos();

        if (trabajos.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Debe describir los trabajos necesarios.");
            return;
        }

        // 1. Guardar en DB
        model.anotarPrevision(idIncidencia, horas, trabajos);
        
        // 2. Historial
        model.registrarHistorialTecnico(idIncidencia, tecnicoActual.getId(), 
                "Iniciada reparación. Previsión: " + horas + "h. Trabajos: " + trabajos);

        JOptionPane.showMessageDialog(view, "Cambios registrados. La incidencia ahora está EN CURSO.");
        cargarIncidencias(); 
    }
}