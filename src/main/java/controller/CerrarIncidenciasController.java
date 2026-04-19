package controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import model.CerrarIncidenciasModel;
import model.IncidenciaDisplayDTO;
import util.ApplicationException;
import view.CerrarIncidenciasView;

public class CerrarIncidenciasController {

    private CerrarIncidenciasModel model;
    private CerrarIncidenciasView view;

    private int idTecnico;
    private String tipoResponsable;

    public CerrarIncidenciasController(CerrarIncidenciasModel m, CerrarIncidenciasView v) {
        this.model = m;
        this.view = v;
    }

    public void initController() {

        view.setVisible(true);

        //LOGIN
        view.setAccederAction(e -> {
            try {
                String identificador = view.getIdentificador();

                idTecnico = model.loginTecnico(identificador);
                tipoResponsable = model.getTipoResponsable(idTecnico);

                cargarIncidencias();

            } catch (ApplicationException ex) {
                view.showMessage(ex.getMessage());
            }
        });

        // CIERRE
        view.setCerrarAction(e -> cerrar());
    }

    private void cargarIncidencias() {

        if (tipoResponsable == null) {
            return; // evita llamadas antes del login
        }

        List<IncidenciaDisplayDTO> lista =
                model.getIncidenciasResueltas(tipoResponsable);

        view.loadIncidencias(lista);
    }

    private void cerrar() {

        if (tipoResponsable == null) {
            view.showMessage("Debes iniciar sesión primero");
            return;
        }

        List<Long> ids = view.getSelectedIds();

        if (ids.isEmpty()) {
            view.showMessage("Selecciona al menos una incidencia");
            return;
        }

        if (ids.size() > 1) {
            view.showError("Por favor, selecciona solo UNA incidencia a la vez para poder imputar su coste exacto de forma individual.");
            return;
        }

        long idIncidencia = ids.get(0);
        String tipoIncidencia = view.getTipoSeleccionado();

        // 1. REGLA: Campo obligatorio
        String costeTxt = view.getCosteReal().replace(",", ".").trim();
        if (costeTxt.isEmpty()) {
            view.showError("El campo 'Coste Real' es OBLIGATORIO para cerrar la incidencia.");
            return;
        }

        // 2. REGLA: Formato numérico y positivo
        double costeReal = 0.0;
        try {
            costeReal = Double.parseDouble(costeTxt);
            if (costeReal < 0) {
                view.showError("El coste a imputar no puede ser negativo.");
                return;
            }
        } catch (NumberFormatException e) {
            view.showError("El 'Coste Real' debe ser un formato numérico válido (ej: 150.50).");
            return;
        }

        // 3. REGLA: Validar contra el presupuesto
        double presupuestoDisponible = model.getPresupuestoDisponible(tipoIncidencia);

        if (costeReal > presupuestoDisponible) {
            // BLOQUEO: Se pasa del presupuesto o no hay presupuesto configurado
            view.showError("OPERACIÓN DENEGADA.\n" +
                    "El coste introducido (" + costeReal + "€) supera el saldo disponible para la categoría '" + tipoIncidencia + "'.\n" +
                    "Presupuesto disponible actualmente: " + presupuestoDisponible + "€.");
            return;
        }

        // 4. ÉXITO: Llamamos al modelo para cerrar y descontar el dinero
        model.cerrarIncidenciaConCoste(idIncidencia, tipoIncidencia, costeReal, idTecnico);

        // 5. MENSAJE FINAL "TICKET"
        String fechaHoraActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String mensajeExito = "Incidencia ID: " + idIncidencia + " CERRADA definitivamente con éxito.\n\n" +
                              "Se ha imputado un gasto de " + costeReal + "€ a la categoría '" + tipoIncidencia + "'.\n" +
                              "Nuevo saldo disponible en esta categoría: " + (presupuestoDisponible - costeReal) + "€.\n\n" +
                              "Modificación realizada el: " + fechaHoraActual;

        view.showMessage(mensajeExito);

        view.limpiarCosteReal();
        cargarIncidencias();
    }
}