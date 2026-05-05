package model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import util.ApplicationException;
import util.Database;

public class ReaperturaIncidenciasModelTest {

    private ReaperturaIncidenciasModel model;
    private Database db;

    private static final int ID_CIUDADANO = 9901;
    private static final int ID_OTRO_CIUDADANO = 9902;
    private static final int ID_ZONA = 9901;

    private static final int INCIDENCIA_CERRADA = 9801;
    private static final int INCIDENCIA_RECHAZADA = 9802;
    private static final int INCIDENCIA_NUEVA = 9803;
    private static final int INCIDENCIA_RESUELTA = 9804;
    private static final int INCIDENCIA_OTRO_CIUDADANO = 9805;

    private static final String EMAIL_CIUDADANO = "ciudadano.prueba@test.com";
    private static final String DNI_CIUDADANO = "99999991A";
    private static final String EMAIL_OTRO_CIUDADANO = "otro.ciudadano@test.com";

    @Before
    public void setUp() {
        db = new Database();
        model = new ReaperturaIncidenciasModel();

        limpiarDatosPrueba();
        insertarDatosPrueba();
    }

    @Test
    public void puedeReabrirse_cerradaDelCiudadano_devuelveTrue() {
        assertTrue(model.puedeReabrirse(INCIDENCIA_CERRADA, EMAIL_CIUDADANO));
    }

    @Test
    public void puedeReabrirse_rechazadaDelCiudadano_devuelveTrue() {
        assertTrue(model.puedeReabrirse(INCIDENCIA_RECHAZADA, EMAIL_CIUDADANO));
    }

    @Test
    public void puedeReabrirse_estadoNoPermitido_devuelveFalse() {
        assertFalse(model.puedeReabrirse(INCIDENCIA_NUEVA, EMAIL_CIUDADANO));
        assertFalse(model.puedeReabrirse(INCIDENCIA_RESUELTA, EMAIL_CIUDADANO));
    }

    @Test
    public void puedeReabrirse_incidenciaDeOtroCiudadano_devuelveFalse() {
        assertFalse(model.puedeReabrirse(INCIDENCIA_OTRO_CIUDADANO, EMAIL_CIUDADANO));
    }

    @Test
    public void reabrirIncidencia_valida_cambiaEstadoAReabierta() {
        model.reabrirIncidencia(
                INCIDENCIA_CERRADA,
                EMAIL_CIUDADANO,
                "No está bien resuelta"
        );

        String estado = getEstadoIncidencia(INCIDENCIA_CERRADA);
        assertEquals("REABIERTA", estado);
    }

    @Test
    public void reabrirIncidencia_valida_registraHistorial() {
        model.reabrirIncidencia(
                INCIDENCIA_RECHAZADA,
                EMAIL_CIUDADANO,
                "Revisar de nuevo"
        );

        int entradas = contarHistorialReapertura(INCIDENCIA_RECHAZADA);
        assertEquals(1, entradas);
    }

    @Test(expected = ApplicationException.class)
    public void reabrirIncidencia_noReabrible_lanzaExcepcion() {
        model.reabrirIncidencia(
                INCIDENCIA_NUEVA,
                EMAIL_CIUDADANO,
                "Intento inválido"
        );
    }

    @Test(expected = ApplicationException.class)
    public void reabrirIncidencia_identificadorVacio_lanzaExcepcion() {
        model.reabrirIncidencia(
                INCIDENCIA_CERRADA,
                " ",
                "Motivo"
        );
    }

    @Test(expected = ApplicationException.class)
    public void reabrirIncidencia_motivoVacio_lanzaExcepcion() {
        model.reabrirIncidencia(
                INCIDENCIA_CERRADA,
                EMAIL_CIUDADANO,
                " "
        );
    }

    private void limpiarDatosPrueba() {
        db.executeUpdate("DELETE FROM Historial WHERE fk_incidencia BETWEEN 9801 AND 9805");
        db.executeUpdate("DELETE FROM IncidenciaTecnico WHERE fk_incidencia BETWEEN 9801 AND 9805");
        db.executeUpdate("DELETE FROM Incidencia WHERE id_incidencia BETWEEN 9801 AND 9805");
        db.executeUpdate("DELETE FROM Zona WHERE id_zona = ?", ID_ZONA);
        db.executeUpdate("DELETE FROM Persona WHERE id_persona IN (?, ?)", ID_CIUDADANO, ID_OTRO_CIUDADANO);
    }

    private void insertarDatosPrueba() {
        db.executeUpdate(
                "INSERT INTO Persona (id_persona, usuario, contrasena, tipo, tipo_responsable, nombre, apellidos, dni, email) "
                        + "VALUES (?, ?, ?, 'CIUDADANO', NULL, ?, ?, ?, ?)",
                ID_CIUDADANO,
                "ciudadano_test",
                "1234",
                "Nombre",
                "Apellido",
                DNI_CIUDADANO,
                EMAIL_CIUDADANO
        );

        db.executeUpdate(
                "INSERT INTO Persona (id_persona, usuario, contrasena, tipo, tipo_responsable, nombre, apellidos, dni, email) "
                        + "VALUES (?, ?, ?, 'CIUDADANO', NULL, ?, ?, ?, ?)",
                ID_OTRO_CIUDADANO,
                "otro_test",
                "1234",
                "Otro",
                "Ciudadano",
                "99999992B",
                EMAIL_OTRO_CIUDADANO
        );

        db.executeUpdate(
                "INSERT INTO Zona (id_zona, nombre) VALUES (?, ?)",
                ID_ZONA,
                "Zona test"
        );

        insertarIncidencia(INCIDENCIA_CERRADA, "CERRADA", ID_CIUDADANO);
        insertarIncidencia(INCIDENCIA_RECHAZADA, "RECHAZADA", ID_CIUDADANO);
        insertarIncidencia(INCIDENCIA_NUEVA, "NUEVA", ID_CIUDADANO);
        insertarIncidencia(INCIDENCIA_RESUELTA, "RESUELTA", ID_CIUDADANO);
        insertarIncidencia(INCIDENCIA_OTRO_CIUDADANO, "CERRADA", ID_OTRO_CIUDADANO);
    }

    private void insertarIncidencia(int id, String estado, int ciudadano) {
        db.executeUpdate(
                "INSERT INTO Incidencia (id_incidencia, tipo, descripcion, fecha_hora, estado, fk_ciudadano, fk_zona) "
                        + "VALUES (?, ?, ?, datetime('now','localtime'), ?, ?, ?)",
                id,
                "Tipo",
                "Desc",
                estado,
                ciudadano,
                ID_ZONA
        );
    }

    private String getEstadoIncidencia(int id) {
        List<Object[]> rows = db.executeQueryArray(
                "SELECT estado FROM Incidencia WHERE id_incidencia = ?",
                id
        );
        return rows.get(0)[0].toString();
    }

    private int contarHistorialReapertura(int id) {
        List<Object[]> rows = db.executeQueryArray(
                "SELECT COUNT(*) FROM Historial WHERE fk_incidencia = ? AND estado = 'REABIERTA' AND accion = 'REAPERTURA'",
                id
        );

        Object value = rows.get(0)[0];

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        return Integer.parseInt(value.toString());
    }
}