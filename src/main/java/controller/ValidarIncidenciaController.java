package controller;

import model.*;
import view.ValidarIncidenciaView;
import util.ApplicationException;
import util.SwingUtil;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        view.getBtnRechazar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> ejecutarRechazo()));
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

     // Extraemos datos de la tabla para un resumen completo
        String tipoOriginal = view.getTable().getValueAt(row, 1).toString();
        String localizacion = view.getTable().getValueAt(row, 3).toString();
        String ciudadano = view.getTable().getValueAt(row, 4).toString();

        // Generamos fecha y hora
        String fechaHoraActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        String mensajeExito = "La incidencia ha sido VALIDADA y clasificada correctamente.\n\n" +
                              "Resumen de la operación:\n" +
                              "- ID Incidencia: " + idIncidencia + "\n" +
                              "- Ciudadano reportador: " + ciudadano + "\n" +
                              "- Localización: " + localizacion + "\n" +
                              "- Tipo original: " + tipoOriginal + "\n" +
                              "- Tipo final asignado: " + nuevoTipo + "\n\n" +
                              "Modificación realizada el: " + fechaHoraActual; // <--- FECHA AÑADIDA

        JOptionPane.showMessageDialog(view, mensajeExito, "Validación completada", JOptionPane.INFORMATION_MESSAGE);
        cargarIncidencias(); // Refrescar la tabla para que desaparezca la ya validada
    }
    
 // NUEVA LÓGICA: Rechazar incidencia
    private void ejecutarRechazo() {
        if (operadorActual == null) {
            throw new ApplicationException("Debe identificarse primero con su email.");
        }

        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            throw new ApplicationException("Debe seleccionar una incidencia de la tabla para rechazarla.");
        }

        int idIncidencia = Integer.parseInt(view.getTable().getValueAt(row, 0).toString());
        String tipoOriginal = view.getTable().getValueAt(row, 1).toString();
        String ciudadano = view.getTable().getValueAt(row, 4).toString();

        // Pedimos el motivo obligatorio
     // 1. CREAMOS EL CUADRO DE TEXTO GRANDE
        JTextArea txtMotivo = new JTextArea(6, 35);
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtMotivo);

        // 2. LO MOSTRAMOS EN EL DIÁLOGO
        int opcion = JOptionPane.showConfirmDialog(view, scrollPane, 
                "Indique el motivo de rechazo para la incidencia " + idIncidencia + " (Obligatorio):", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (opcion != JOptionPane.OK_OPTION) {
            return; 
        }

        String motivo = txtMotivo.getText();

        if (motivo.trim().isEmpty()) {
            throw new ApplicationException("Es obligatorio indicar el motivo por el cual se rechaza la incidencia.");
        }

        model.rechazarIncidencia(idIncidencia, operadorActual.getId(), motivo);

        // Generamos fecha y hora
        String fechaHoraActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        String mensajeExito = "La incidencia ha sido RECHAZADA.\n\n" +
                              "Detalles:\n" +
                              "- ID: " + idIncidencia + "\n" +
                              "- Tipo reportado: " + tipoOriginal + "\n" +
                              "- Ciudadano: " + ciudadano + "\n" +
                              "- Motivo: " + motivo + "\n\n" +
                              "Modificación realizada el: " + fechaHoraActual; // <--- FECHA AÑADIDA

        JOptionPane.showMessageDialog(view, mensajeExito, "Operación completada", JOptionPane.INFORMATION_MESSAGE);
        cargarIncidencias();
    }
}