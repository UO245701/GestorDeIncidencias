package controller;

import model.*;
import view.TecnicoView;
import util.ApplicationException;
import util.SwingUtil;
import javax.swing.JOptionPane;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

     // Extraemos datos de la tabla para el mensaje
        String tipoIncidencia = view.getTable().getValueAt(row, 1).toString();
        String localizacion = view.getTable().getValueAt(row, 3).toString();
        String fechaHoraActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        String mensajeExito = "Planificación registrada con éxito. La incidencia pasa a estado EN CURSO.\n\n" +
                "Detalles de la planificación:\n" +
                "- ID Incidencia: " + idIncidencia + "\n" +
                "- Tipo: " + tipoIncidencia + "\n" +
                "- Localización: " + localizacion + "\n" +
                "- Horas estimadas: " + horas + "h\n" +
                "- Trabajos previstos: " + trabajos + "\n\n" +
                "Modificación realizada el: " + fechaHoraActual; // <--- FECHA AÑADIDA

        JOptionPane.showMessageDialog(view, mensajeExito, "Planificación completada", JOptionPane.INFORMATION_MESSAGE);
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

        // 1. CREAMOS EL CUADRO DE TEXTO GRANDE
        JTextArea txtMotivo = new JTextArea(6, 35); // 6 filas de alto, 35 columnas de ancho
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtMotivo);

        // 2. LO MOSTRAMOS EN EL DIÁLOGO
        int opcion = JOptionPane.showConfirmDialog(view, scrollPane, 
                "Indique el motivo por el que rechaza esta incidencia (Obligatorio):", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (opcion != JOptionPane.OK_OPTION) {
            return; // El usuario pulsó cancelar o cerró la ventana
        }

        String motivo = txtMotivo.getText();

        if (motivo.trim().isEmpty()) {
            throw new ApplicationException("Debe indicar obligatoriamente un motivo para rechazar la incidencia.");
        }

        model.rechazarIncidencia(idIncidencia, tecnicoActual.getId(), motivo);

        // Generamos la fecha y hora actual
        String fechaHoraActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        String mensajeExito = "La incidencia ha sido rechazada y devuelta al sistema.\n\n" +
                              "Detalles de la operación:\n" +
                              "- ID Incidencia: " + idIncidencia + "\n" +
                              "- Tipo: " + tipoIncidencia + "\n" +
                              "- Localización: " + localizacion + "\n" +
                              "- Motivo del rechazo: " + motivo + "\n\n" +
                              "Modificación realizada el: " + fechaHoraActual; // <--- FECHA AÑADIDA

        JOptionPane.showMessageDialog(view, mensajeExito, "Rechazo completado", JOptionPane.INFORMATION_MESSAGE);
        cargarIncidencias();
    }
}