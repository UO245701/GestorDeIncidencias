package controller;

import model.*;
import view.ValidarIncidenciaView;
import util.SwingUtil;
import java.util.List;
import javax.swing.JOptionPane;

public class ValidarIncidenciaController {
    private ValidarIncidenciasModel model;
    private ValidarIncidenciaView view;
    private PersonaEntity operadorActual;

    public ValidarIncidenciaController(ValidarIncidenciasModel model, ValidarIncidenciaView view) {
        this.model = model;
        this.model = new ValidarIncidenciasModel(); // Aseguramos instancia
        this.view = view;
    }

    public void initController() {
        view.getBtnLogin().addActionListener(e -> SwingUtil.exceptionWrapper(() -> cargarIncidencias()));
        view.getBtnValidar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> ejecutarValidacion()));
        view.setVisible(true);
    }

    private void cargarIncidencias() {
        String email = view.getEmail();
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Por favor, introduzca un email");
            return;
        }
        
        operadorActual = model.getOperadorByEmail(email);
        
        if (operadorActual == null) {
            JOptionPane.showMessageDialog(view, "No se encontró un operador con ese email");
            return;
        }

        List<IncidenciaDisplayDTO> lista = model.getIncidenciasNuevas();
        // Usamos los alias definidos en la query del modelo
        view.setTableModel(SwingUtil.getTableModelFromPojos(lista, 
                new String[] {"id", "tipo", "descripcion", "localizacion", "usuarioCiudadano"}));
    }

    private void ejecutarValidacion() {
        // Verificamos que haya un operador identificado
        if (operadorActual == null) {
            JOptionPane.showMessageDialog(view, "Debe identificarse primero con su email");
            return;
        }

        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar una incidencia de la tabla");
            return;
        }

        // Convertimos el valor de la celda de forma segura
        Object value = view.getTable().getValueAt(row, 0);
        int idIncidencia = Integer.parseInt(value.toString());
        
        String nuevoTipo = view.getTipoSeleccionado();
        if (nuevoTipo == null || nuevoTipo.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Seleccione un tipo de incidencia");
            return;
        }

        // 1. Actualizar Incidencia (Cambia a VALIDADA)
        model.validarIncidencia(idIncidencia, nuevoTipo);
        
        // 2. Registrar en Historial (Usando el id del operador recuperado)
        model.registrarEnHistorial(idIncidencia, operadorActual.getId(), nuevoTipo, 
                "Incidencia validada y clasificada como: " + nuevoTipo);

        JOptionPane.showMessageDialog(view, "Incidencia " + idIncidencia + " validada correctamente");
        
        cargarIncidencias(); // Refrescar la tabla para que desaparezca la ya validada
    }
}