package controller;

import model.AsignarIncidenciaModel;
import model.IncidenciaDisplayDTO;
import model.PersonaEntity;
import util.SwingUtil;
import view.AsignarIncidenciaView;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AsignarIncidenciaController {

    private AsignarIncidenciaModel model;
    private AsignarIncidenciaView view;
    private PersonaEntity operadorActual;

    public AsignarIncidenciaController(AsignarIncidenciaModel model, AsignarIncidenciaView view) {
        this.model = model;
        this.view = view;
    }

    public void initController() {
        view.getBtnLogin().addActionListener(
                e -> SwingUtil.exceptionWrapper(() -> cargarDatos()));

        view.getBtnAsignar().addActionListener(
                e -> SwingUtil.exceptionWrapper(() -> ejecutarAsignacion()));

        view.getTablaIncidencias().getSelectionModel().addListSelectionListener(
                (ListSelectionEvent e) -> {
                    if (!e.getValueIsAdjusting()) {
                        SwingUtil.exceptionWrapper(() -> cargarTecnicosDeIncidenciaSeleccionada());
                    }
                });

        view.setVisible(true);
    }

    private void cargarDatos() {
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

        List<IncidenciaDisplayDTO> incidencias = model.getIncidenciasValidadas();
        view.setTablaIncidencias(SwingUtil.getTableModelFromPojos(
                incidencias,
                new String[] { "id", "tipo", "descripcion", "localizacion", "fechaHoraRegistro", "usuarioCiudadano" }
        ));

        view.setTablaTecnicos(SwingUtil.getTableModelFromPojos(
                Collections.emptyList(),
                new String[] { "id", "usuario", "nombre", "apellidos", "email" }
        ));
    }

    private void cargarTecnicosDeIncidenciaSeleccionada() {
        if (operadorActual == null) {
            return;
        }

        int rowInc = view.getTablaIncidencias().getSelectedRow();
        if (rowInc == -1) {
            return;
        }

        int idIncidencia = Integer.parseInt(
                view.getTablaIncidencias().getValueAt(rowInc, 0).toString());

        List<PersonaEntity> tecnicos = model.getTecnicosDisponiblesParaIncidencia(idIncidencia);

        view.setTablaTecnicos(SwingUtil.getTableModelFromPojos(
                tecnicos,
                new String[] { "id", "usuario", "nombre", "apellidos", "email" }
        ));
    }

    private void ejecutarAsignacion() {
        if (operadorActual == null) {
            JOptionPane.showMessageDialog(view, "Debe identificarse primero con su email");
            return;
        }

        int rowInc = view.getTablaIncidencias().getSelectedRow();
        if (rowInc == -1) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar una incidencia de la tabla");
            return;
        }

        int[] filasTecnicos = view.getTablaTecnicos().getSelectedRows();
        if (filasTecnicos == null || filasTecnicos.length == 0) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar uno o varios técnicos");
            return;
        }

        int idIncidencia = Integer.parseInt(
                view.getTablaIncidencias().getValueAt(rowInc, 0).toString());

        String tipoIncidencia = view.getTablaIncidencias().getValueAt(rowInc, 1).toString();
        String localizacion = view.getTablaIncidencias().getValueAt(rowInc, 3).toString();
        String nombreOperadora = operadorActual.getNombre();
        String horaAsignacion = LocalTime.now().withNano(0).toString();

        List<Integer> idsTecnicos = new ArrayList<>();
        List<String> nombresTecnicos = new ArrayList<>();

        for (int rowTec : filasTecnicos) {
            int idTecnico = Integer.parseInt(
                    view.getTablaTecnicos().getValueAt(rowTec, 0).toString());

            String nombreTecnico = view.getTablaTecnicos().getValueAt(rowTec, 2).toString();
            String apellidosTecnico = view.getTablaTecnicos().getValueAt(rowTec, 3).toString();

            idsTecnicos.add(idTecnico);
            nombresTecnicos.add(nombreTecnico + " " + apellidosTecnico);
        }

        model.asignarTecnicosAIncidencia(idIncidencia, idsTecnicos);

        for (String tecnico : nombresTecnicos) {
            String detalleHistorial = "La operadora " + nombreOperadora
                    + " asignó el trabajo a " + tecnico;
            model.registrarEnHistorial(idIncidencia, operadorActual.getId(), detalleHistorial);
        }

        JOptionPane.showMessageDialog(view,
                "Incidencia " + idIncidencia + " (" + tipoIncidencia + ") asignada correctamente.\n\n"
                        + "Técnicos: " + String.join(", ", nombresTecnicos) + "\n"
                        + "Localización: " + localizacion + "\n"
                        + "Operadora: " + nombreOperadora + "\n"
                        + "Hora de asignación: " + horaAsignacion,
                "Asignación realizada",
                JOptionPane.INFORMATION_MESSAGE);

        cargarDatos();
    }
}