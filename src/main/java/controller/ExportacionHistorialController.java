package controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import model.ExportacionHistorialModel;
import model.IncidenciaExportacionDTO;
import model.PersonaEntity;
import util.ApplicationException;
import util.SwingUtil;
import view.ExportacionHistorialView;

public class ExportacionHistorialController {

    private final ExportacionHistorialModel model;
    private final ExportacionHistorialView view;
    private PersonaEntity usuarioActual;

    public ExportacionHistorialController(ExportacionHistorialModel model, ExportacionHistorialView view) {
        this.model = model;
        this.view = view;
    }

    public void initController() {
        view.getBtnAcceder().addActionListener(e -> SwingUtil.exceptionWrapper(() -> acceder()));
        view.getBtnBuscar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> buscar()));
        view.getBtnRestablecerFiltros().addActionListener(e -> SwingUtil.exceptionWrapper(() -> view.restablecerFiltros()));
        view.getBtnSeleccionarTodas().addActionListener(e -> SwingUtil.exceptionWrapper(() -> view.seleccionarTodas()));
        view.getBtnExportar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> exportar()));
        view.setVisible(true);
    }

    private void acceder() {
        usuarioActual = model.validarOperadorOTecnico(view.getIdentificador());
        view.setTipos(SwingUtil.getComboModelFromList(model.getTiposIncidencia()));
        view.setZonas(SwingUtil.getComboModelFromList(model.getZonas()));

        JOptionPane.showMessageDialog(
                view,
                "Usuario autorizado: " + usuarioActual.getNombre() + " (" + usuarioActual.getTipo() + ")",
                "Acceso autorizado",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscar() {
        asegurarAcceso();

        String tipo = getFiltro(view.getTipoSeleccionado());
        String zona = getFiltro(view.getZonaSeleccionada());
        String fechaInicio = formatFecha(view.getFechaInicio());
        String fechaFin = formatFecha(view.getFechaFin());

        validarRangoFechas(fechaInicio, fechaFin);

        List<IncidenciaExportacionDTO> incidencias =
                model.buscarIncidencias(tipo, zona, fechaInicio, fechaFin);
        view.mostrarIncidencias(incidencias);

        JOptionPane.showMessageDialog(
                view,
                "Se han encontrado " + incidencias.size() + " incidencias.",
                "Busqueda finalizada",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportar() {
        asegurarAcceso();

        File destino = view.seleccionarDestinoCsv();
        if (destino == null) {
            return;
        }

        int exportadas = model.exportarHistorialCsv(view.getIdsSeleccionados(), destino.toPath());

        JOptionPane.showMessageDialog(
                view,
                "Exportacion completada.\nIncidencias exportadas: " + exportadas,
                "Exportacion finalizada",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void asegurarAcceso() {
        if (usuarioActual == null) {
            throw new ApplicationException("Debe acceder con un operador o tecnico antes de continuar");
        }
    }

    private String getFiltro(Object value) {
        if (value == null) {
            return "";
        }
        String filtro = value.toString().trim();
        return ExportacionHistorialView.SIN_FILTRO.equals(filtro) ? "" : filtro;
    }

    private String formatFecha(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private void validarRangoFechas(String fechaInicio, String fechaFin) {
        if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
            return;
        }

        if (LocalDate.parse(fechaInicio).isAfter(LocalDate.parse(fechaFin))) {
            throw new ApplicationException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
    }
}
