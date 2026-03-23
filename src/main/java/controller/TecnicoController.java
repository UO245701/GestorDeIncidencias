package controller;

import model.*;
import view.TecnicoView;
import util.ApplicationException;
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
        view.getBtnRechazar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> ejecutarRechazo()));
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
    
    private void ejecutarRechazo() {
        if (tecnicoActual == null) {
            throw new ApplicationException("Identifíquese primero.");
        }

        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            throw new ApplicationException("Seleccione la incidencia que desea rechazar.");
        }

        int idIncidencia = Integer.parseInt(view.getTable().getValueAt(row, 0).toString());
        // Extraemos un par de datos extra de la tabla para que el mensaje quede más completo
        String tipoIncidencia = view.getTable().getValueAt(row, 1).toString();
        String localizacion = view.getTable().getValueAt(row, 3).toString();

        // Pedimos el motivo con un InputDialog
        String motivo = JOptionPane.showInputDialog(view, 
                "Indique el motivo por el que rechaza esta incidencia (Obligatorio):", 
                "Rechazar Incidencia", 
                JOptionPane.WARNING_MESSAGE);

        // Si el técnico pulsa "Cancelar" o la 'X', el motivo es null y abortamos silenciosamente
        if (motivo == null) {
            return; 
        }

        // Validación de campo vacío lanzando excepción para que la capture el exceptionWrapper
        if (motivo.trim().isEmpty()) {
            throw new ApplicationException("Debe indicar obligatoriamente un motivo para rechazar la incidencia.");
        }

        // Llamamos al modelo
        model.rechazarIncidencia(idIncidencia, tecnicoActual.getId(), motivo);

        // NUEVO MENSAJE DE CONFIRMACIÓN MÁS DETALLADO
        String mensajeExito = "La incidencia ha sido rechazada y devuelta al sistema.\n\n" +
                              "Detalles de la operación:\n" +
                              "- ID Incidencia: " + idIncidencia + "\n" +
                              "- Tipo: " + tipoIncidencia + "\n" +
                              "- Localización: " + localizacion + "\n" +
                              "- Motivo del rechazo: " + motivo + "\n\n" +
                              "Se ha registrado el cambio en el historial de la incidencia.";

        JOptionPane.showMessageDialog(view, 
                mensajeExito, 
                "Rechazo completado", 
                JOptionPane.INFORMATION_MESSAGE);
        
        // Recargamos la tabla para que desaparezca
        cargarIncidencias(); 
    }
}