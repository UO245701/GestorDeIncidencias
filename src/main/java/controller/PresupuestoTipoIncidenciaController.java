package controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JOptionPane;

import model.PersonaEntity;
import model.PresupuestoTipoIncidenciaModel;
import util.ApplicationException;
import util.SwingUtil;
import view.PresupuestoTipoIncidenciaView;

public class PresupuestoTipoIncidenciaController {

    private final PresupuestoTipoIncidenciaModel model;
    private final PresupuestoTipoIncidenciaView view;
    private PersonaEntity operadorActual;
    private boolean actualizandoPeriodo;

    public PresupuestoTipoIncidenciaController(PresupuestoTipoIncidenciaModel model,
            PresupuestoTipoIncidenciaView view) {
        this.model = model;
        this.view = view;
    }

    public void initController() {
        view.getBtnLogin().addActionListener(e -> SwingUtil.exceptionWrapper(() -> cargarDatos()));
        view.getBtnCrear().addActionListener(e -> SwingUtil.exceptionWrapper(() -> crearPresupuesto()));
        view.getChkAnioNatural().addActionListener(e -> {
            if (!actualizandoPeriodo && view.getChkAnioNatural().isSelected()) {
                SwingUtil.exceptionWrapper(() -> aplicarFechaFinPredeterminada());
            }
        });
        view.getDcFechaInicio().addPropertyChangeListener("date",
                e -> SwingUtil.exceptionWrapper(() -> actualizarMarcaAnioNatural()));
        view.getDcFechaFin().addPropertyChangeListener("date",
                e -> SwingUtil.exceptionWrapper(() -> actualizarMarcaAnioNatural()));

        aplicarFechaFinPredeterminada();
        refrescarPresupuestos();
        view.setVisible(true);
    }

    private void cargarDatos() {
        String email = view.getEmailOperador();
        if (email == null || email.trim().isEmpty()) {
            throw new ApplicationException("Debe introducir el email del operador");
        }

        operadorActual = model.getOperadorByEmail(email);
        if (operadorActual == null) {
            throw new ApplicationException("No se encontro un operador con ese email");
        }

        view.getCmbTipo().setModel(SwingUtil.getComboModelFromList(model.getTiposIncidencia()));
        refrescarPresupuestos();

        JOptionPane.showMessageDialog(view,
                "Operador identificado: " + operadorActual.getNombre(),
                "Operador cargado",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void crearPresupuesto() {
        if (operadorActual == null) {
            throw new ApplicationException("Debe identificarse como operador antes de crear presupuestos");
        }

        String tipo = obtenerTipo();
        double importeMaximo = obtenerImporteMaximo();
        LocalDate fechaInicio = toLocalDate(view.getFechaInicio());
        LocalDate fechaFin = toLocalDate(view.getFechaFin());

        model.crearPresupuesto(tipo, importeMaximo, fechaInicio, fechaFin, operadorActual.getId());

        JOptionPane.showMessageDialog(view,
                "Presupuesto creado correctamente para " + tipo + ".",
                "Presupuesto creado",
                JOptionPane.INFORMATION_MESSAGE);

        view.limpiarFormulario();
        refrescarPresupuestos();
    }

    private void refrescarPresupuestos() {
        view.setTablaPresupuestos(SwingUtil.getTableModelFromPojos(
                model.getPresupuestos(),
                new String[] {
                    "id",
                    "tipo",
                    "importeMaximo",
                    "importeConsumido",
                    "importeDisponible",
                    "fechaInicio",
                    "fechaFin",
                    "activo"
                }));
    }

    private void aplicarFechaFinPredeterminada() {
        actualizandoPeriodo = true;
        try {
            LocalDate fechaInicio = toLocalDate(view.getFechaInicio());
            LocalDate fechaFin = fechaInicio.plusYears(1).minusDays(1);
            view.setFechaFin(Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            view.getChkAnioNatural().setSelected(true);
        } finally {
            actualizandoPeriodo = false;
        }
    }

    private void actualizarMarcaAnioNatural() {
        if (actualizandoPeriodo) {
            return;
        }

        Date fechaInicio = view.getFechaInicio();
        Date fechaFin = view.getFechaFin();
        if (fechaInicio == null || fechaFin == null) {
            setAnioNaturalSeleccionado(false);
            return;
        }

        LocalDate inicio = toLocalDate(fechaInicio);
        LocalDate fin = toLocalDate(fechaFin);
        setAnioNaturalSeleccionado(fin.equals(inicio.plusYears(1).minusDays(1)));
    }

    private void setAnioNaturalSeleccionado(boolean seleccionado) {
        actualizandoPeriodo = true;
        try {
            view.getChkAnioNatural().setSelected(seleccionado);
        } finally {
            actualizandoPeriodo = false;
        }
    }

    private String obtenerTipo() {
        Object tipoSeleccionado = view.getTipoSeleccionado();
        return tipoSeleccionado == null ? "" : tipoSeleccionado.toString().trim();
    }

    private double obtenerImporteMaximo() {
        String texto = view.getImporteMaximo();
        if (texto == null || texto.trim().isEmpty()) {
            throw new ApplicationException("El importe maximo es obligatorio");
        }

        try {
            return Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new ApplicationException("El importe maximo debe ser un numero valido");
        }
    }

    private LocalDate toLocalDate(Date date) {
        if (date == null) {
            throw new ApplicationException("Debe indicar fecha inicial y fecha final");
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
