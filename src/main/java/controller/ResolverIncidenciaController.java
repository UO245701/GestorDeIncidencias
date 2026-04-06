package controller;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import model.IncidenciaDisplayDTO;
import model.ResolverIncidenciaModel;
import util.ApplicationException;
import util.SwingUtil;
import view.ResolverIncidenciaView;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResolverIncidenciaController {

    private ResolverIncidenciaModel model;
    private ResolverIncidenciaView view;
    private int idTecnicoActual;

    public ResolverIncidenciaController(ResolverIncidenciaModel model, ResolverIncidenciaView view) {
        this.model = model;
        this.view = view;
        this.idTecnicoActual = -1;
    }

    public void initController() {
        view.getBtnCargar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> cargarIncidencias()));
        view.getBtnResolver().addActionListener(e -> SwingUtil.exceptionWrapper(() -> resolverIncidencia()));
        view.setVisible(true);
    }

    private void cargarIncidencias() {
        String correo = view.getCorreoTecnico();

        if (correo == null || correo.trim().isEmpty()) {
            throw new ApplicationException("Debe introducir el correo del técnico");
        }

        idTecnicoActual = model.getIdTecnicoByCorreo(correo);

        List<IncidenciaDisplayDTO> incidencias = model.getIncidenciasEnCurso(idTecnicoActual);
        cargarTabla(incidencias);

        if (incidencias.isEmpty()) {
            JOptionPane.showMessageDialog(
                    view,
                    "El técnico identificado no tiene incidencias asignadas en estado EN CURSO.",
                    "Sin incidencias en curso",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void cargarTabla(List<IncidenciaDisplayDTO> incidencias) {
        DefaultTableModel tm = view.getTableModel();
        tm.setRowCount(0);

        for (IncidenciaDisplayDTO i : incidencias) {
            tm.addRow(new Object[] {
                    i.getId(),
                    i.getTipo(),
                    i.getDescripcion(),
                    i.getLocalizacion(),
                    i.getFechaHoraRegistro(),
                    i.getEstado(),
                    i.getHorasPrevision(),
                    i.getTrabajosReparacion()
            });
        }
    }

    private void resolverIncidencia() {
        if (idTecnicoActual <= 0) {
            throw new ApplicationException("Primero debe introducir el correo del técnico y cargar sus incidencias");
        }

        int idIncidencia = view.getIncidenciaSeleccionada();
        if (idIncidencia == -1) {
            throw new ApplicationException("Debe seleccionar una incidencia del listado");
        }

        String tiempoTexto = view.getTiempoReal();
        if (tiempoTexto == null || tiempoTexto.trim().isEmpty()) {
            throw new ApplicationException("El tiempo real empleado es obligatorio");
        }

        int tiempoReal;
        try {
            tiempoReal = Integer.parseInt(tiempoTexto.trim());
        } catch (NumberFormatException e) {
            throw new ApplicationException("El tiempo real empleado debe ser un número entero");
        }

        String trabajosRealizados = view.getTrabajosRealizados();
        if (trabajosRealizados == null || trabajosRealizados.trim().isEmpty()) {
            throw new ApplicationException("La descripción de los trabajos realizados es obligatoria");
        }
        
     // --- LÓGICA DE GESTIÓN DE COSTES ---
        double costeMateriales = 0.0;
        try {
            // Reemplazamos coma por punto para evitar errores de parseo si el usuario usa teclado numérico español
            String costeTxt = view.getCosteMateriales().replace(",", "."); 
            if (!costeTxt.isEmpty()) {
                costeMateriales = Double.parseDouble(costeTxt);
            }
        } catch (NumberFormatException e) {
            throw new ApplicationException("El coste de materiales debe ser un valor numérico válido.");
        }

        String descMateriales = view.getDescMateriales().trim();

        // VALIDACIÓN ESTRELLA: Si se imputa dinero, hay que explicar por qué
        if (costeMateriales > 0 && descMateriales.isEmpty()) {
            throw new ApplicationException("Si imputa gastos adicionales por materiales, debe explicar obligatoriamente el cargo detalladamente en su campo correspondiente.");
        }

        // CÁLCULOS MATEMÁTICOS
        double precioHoraTecnico = model.getPrecioHoraTecnico(idTecnicoActual);
        double costeHoras = tiempoReal * precioHoraTecnico;
        double costeTotal = costeHoras + costeMateriales;

     // Llamamos al modelo actualizado
        model.resolverIncidencia(idIncidencia, idTecnicoActual, tiempoReal, trabajosRealizados, costeMateriales, descMateriales, costeTotal);
        
        Object[] fila = view.getFilaSeleccionada();
        String fechaHoraActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        
     // Montamos el "Ticket" fusionando los datos anteriores con los nuevos costes
        String recibo = "Incidencia resuelta y bloqueada correctamente.\n\n"
                      + "Datos de la incidencia:\n"
                      + "- ID: " + fila[0] + "\n"
                      + "- Tipo: " + fila[1] + "\n"
                      + "- Descripción: " + fila[2] + "\n"
                      + "- Localización: " + fila[3] + "\n\n"
                      + "--- DESGLOSE DE COSTES ---\n"
                      + "- Horas trabajadas: " + tiempoReal + "h (x" + precioHoraTecnico + "€/h) = " + costeHoras + "€\n"
                      + "- Coste adicional (Materiales): " + costeMateriales + "€\n"
                      + "- Justificación materiales: " + (descMateriales.isEmpty() ? "Ninguno" : descMateriales) + "\n"
                      + "--------------------------------\n"
                      + "COSTE TOTAL: " + costeTotal + "€\n\n"
                      + "El estado de la incidencia ha pasado a RESUELTA y el cambio ha sido registrado permanentemente.\n\n"
                      + "Modificación realizada el: " + fechaHoraActual;

        // Usamos JTextArea para que el mensaje se vea grande y completo
        JTextArea txtRecibo = new JTextArea(15, 45);
        txtRecibo.setText(recibo);
        txtRecibo.setEditable(false);
        txtRecibo.setBackground(view.getBackground());

        JOptionPane.showMessageDialog(
                view, 
                new JScrollPane(txtRecibo), 
                "Resolución de Incidencia y Costes", 
                JOptionPane.INFORMATION_MESSAGE
        );

        view.limpiarFormularioResolucion();

        List<IncidenciaDisplayDTO> incidenciasActualizadas = model.getIncidenciasEnCurso(idTecnicoActual);
        cargarTabla(incidenciasActualizadas);
    }
}