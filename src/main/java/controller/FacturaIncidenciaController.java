package controller;

import java.util.List;

import model.FacturaDTO;
import model.FacturaIncidenciaDTO;
import model.FacturaIncidenciaModel;
import util.ApplicationException;
import view.FacturaIncidenciaView;

public class FacturaIncidenciaController {

    private final FacturaIncidenciaModel model;
    private final FacturaIncidenciaView view;

    private Integer idOperador;

    public FacturaIncidenciaController(FacturaIncidenciaModel model, FacturaIncidenciaView view) {
        this.model = model;
        this.view = view;
    }

    public void initController() {
        view.setAccederAction(e -> acceder());
        view.setGenerarAction(e -> generarFactura());
        view.setVisible(true);
    }

    private void acceder() {
        try {
            String identificador = view.getIdentificador().trim();
            if (identificador.isEmpty()) {
                view.showError("Introduce el email o DNI del operador");
                return;
            }

            idOperador = model.loginOperador(identificador);
            int incidenciasDisponibles = cargarIncidencias();
            if (incidenciasDisponibles == 0) {
                view.showMessage("Operador identificado correctamente.\nNo hay incidencias cerradas pendientes de facturar.");
            } else {
                view.showMessage("Operador identificado correctamente.\nIncidencias disponibles para facturar: "
                        + incidenciasDisponibles);
            }
        } catch (ApplicationException ex) {
            view.showError(ex.getMessage());
        } catch (Exception ex) {
            view.showError("No se pudieron cargar las incidencias: " + ex.getMessage());
        }
    }

    private int cargarIncidencias() {
        List<FacturaIncidenciaDTO> incidencias = model.getIncidenciasCerradasSinFactura();
        view.loadIncidencias(incidencias);
        return incidencias.size();
    }

    private void generarFactura() {
        try {
            if (idOperador == null) {
                view.showError("Debes identificarte como operador antes de emitir facturas");
                return;
            }

            Long idIncidencia = view.getSelectedId();
            if (idIncidencia == null) {
                view.showError("Selecciona una incidencia cerrada");
                return;
            }

            FacturaDTO factura = model.generarFactura(idIncidencia, idOperador);
            view.showFacturaGenerada(formatearMensaje(factura));
            cargarIncidencias();
        } catch (ApplicationException ex) {
            view.showError(ex.getMessage());
        } catch (Exception ex) {
            view.showError("No se pudo generar la factura: " + ex.getMessage());
        }
    }

    private String formatearMensaje(FacturaDTO factura) {
        return "La factura ha sido generada correctamente.\n\n"
                + "Numero de factura: " + factura.getNumeroFactura() + "\n"
                + "Incidencia: " + factura.getIdIncidencia() + "\n"
                + "Emisor: " + factura.getEmisor() + "\n"
                + "Fecha de emision: " + factura.getFechaEmision() + "\n"
                + "Coste total: " + factura.getCosteTotal() + " EUR\n\n"
                + "Detalle:\n" + factura.getDetalle();
    }
}
