package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import util.ApplicationException;
import util.Database;

class FacturaIncidenciaModelTest {

    private Database db;
    private FacturaIncidenciaModel model;

    @BeforeEach
    void setUp() {
        db = new Database();
        db.createDatabase(false);
        cargarCasosPrueba();
        model = new FacturaIncidenciaModel();
    }

    private void cargarCasosPrueba() {
        db.executeUpdate(
                "INSERT INTO Persona "
                        + "(id_persona, usuario, contrasena, tipo, nombre, apellidos, dni, email) "
                        + "VALUES (1, 'op', 'admin', 'OPERADOR', 'Laura', 'Martinez', '11111111A', 'op@ayto.es')");

        db.executeUpdate(
                "INSERT INTO Persona "
                        + "(id_persona, usuario, contrasena, tipo, nombre, apellidos, dni, email, precio_hora) "
                        + "VALUES (2, 'tec', 'tech', 'TECNICO', 'Roberto', 'Sanchez', "
                        + "'22222222B', 'tec@ayto.es', 30.0)");

        db.executeUpdate(
                "INSERT INTO Persona "
                        + "(id_persona, usuario, contrasena, tipo, nombre, apellidos, dni, email) "
                        + "VALUES (3, 'ciu', '1234', 'CIUDADANO', 'Paco', 'Garcia', "
                        + "'33333333C', 'ciu@email.es')");

        db.executeUpdate("INSERT INTO Zona (id_zona, nombre) VALUES (1, 'Norte')");

        db.executeUpdate(
                "INSERT INTO Incidencia "
                        + "(id_incidencia, tipo, descripcion, estado, fk_ciudadano, fk_tecnico, fk_zona, "
                        + "tiempo_real, trabajos_realizados, coste_materiales, descripcion_materiales, coste_total) "
                        + "VALUES (10, 'Alumbrado', 'Farola reparada', 'CERRADA', 3, 2, 1, "
                        + "120, 'Cambio de luminaria', 15.0, 'Bombilla LED', 75.0)");

        db.executeUpdate(
                "INSERT INTO Incidencia "
                        + "(id_incidencia, tipo, descripcion, estado, fk_ciudadano, fk_tecnico, fk_zona) "
                        + "VALUES (11, 'Limpieza', 'Grafiti pendiente', 'RESUELTA', 3, 2, 1)");

        db.executeUpdate(
                "INSERT INTO Incidencia "
                        + "(id_incidencia, tipo, descripcion, estado, fk_ciudadano, fk_tecnico, fk_zona) "
                        + "VALUES (12, 'Calzada', 'Bache en reparacion', 'EN CURSO', 3, 2, 1)");

        db.executeUpdate(
                "INSERT INTO Incidencia "
                        + "(id_incidencia, tipo, descripcion, estado, fk_ciudadano, fk_tecnico, fk_zona, "
                        + "tiempo_real, trabajos_realizados, coste_materiales, coste_total) "
                        + "VALUES (13, 'Alumbrado', 'Farola ya facturada', 'CERRADA', 3, 2, 1, "
                        + "60, 'Revision', 0.0, 30.0)");

        db.executeUpdate(
                "INSERT INTO Incidencia "
                        + "(id_incidencia, tipo, descripcion, estado, fk_ciudadano, fk_tecnico, fk_zona, "
                        + "tiempo_real, trabajos_realizados, coste_materiales, descripcion_materiales, coste_total) "
                        + "VALUES (14, 'Limpieza', 'Limpieza sin coste de cierre', 'CERRADA', 3, 2, 1, "
                        + "90, 'Limpieza a presion', 10.0, 'Producto limpiador', 0.0)");

        db.executeUpdate(
                "INSERT INTO Incidencia "
                        + "(id_incidencia, tipo, descripcion, estado, fk_ciudadano, fk_tecnico, fk_zona, "
                        + "tiempo_real, trabajos_realizados, coste_materiales, descripcion_materiales, coste_total) "
                        + "VALUES (15, 'Mobiliario urbano', 'Revision sin coste', 'CERRADA', 3, 2, 1, "
                        + "0, 'Revision visual', 0.0, '', 0.0)");

        db.executeUpdate(
                "INSERT INTO Factura "
                        + "(numero_factura, emisor, detalle, coste_total, fk_incidencia, fk_operador) "
                        + "VALUES ('FAC-EXISTENTE-13', 'Laura Martinez', 'Factura previa', 30.0, 13, 1)");
    }

    @Test
    void testLoginOperadorValido() {
        assertEquals(1, model.loginOperador("op@ayto.es"));
    }

    @Test
    void testLoginConTecnicoNoPermitido() {
        assertThrows(ApplicationException.class, () -> model.loginOperador("tec@ayto.es"));
    }

    @Test
    void testLoginUsuarioInexistente() {
        assertThrows(ApplicationException.class, () -> model.loginOperador("nadie@ayto.es"));
    }

    @Test
    void testListarIncidenciasCerradasSinFactura() {
        assertTrue(model.getIncidenciasCerradasSinFactura()
                .stream()
                .anyMatch(i -> i.getId() == 10));
    }

    @Test
    void testNoListarIncidenciasNoCerradas() {
        assertFalse(model.getIncidenciasCerradasSinFactura()
                .stream()
                .anyMatch(i -> i.getId() == 11 || i.getId() == 12));
    }

    @Test
    void testNoListarIncidenciasConFacturaPrevia() {
        assertFalse(model.getIncidenciasCerradasSinFactura()
                .stream()
                .anyMatch(i -> i.getId() == 13));
    }

    @Test
    void testGenerarFacturaCorrectamente() {
        FacturaDTO factura = model.generarFactura(10, 1);

        assertTrue(factura.getNumeroFactura().startsWith("FAC-"));
        assertEquals(75.0, factura.getCosteTotal());
        assertEquals(10, factura.getIdIncidencia());
        assertTrue(factura.getDetalle().contains("Farola reparada"));
    }

    @Test
    void testNoGenerarFacturaDuplicada() {
        assertThrows(ApplicationException.class, () -> model.generarFactura(13, 1));
    }

    @Test
    void testNoGenerarFacturaIncidenciaNoCerrada() {
        assertThrows(ApplicationException.class, () -> model.generarFactura(11, 1));
        assertThrows(ApplicationException.class, () -> model.generarFactura(12, 1));
    }

    @Test
    void testNoGenerarFacturaIncidenciaInexistente() {
        assertThrows(ApplicationException.class, () -> model.generarFactura(999, 1));
    }

    @Test
    void testFacturaCalculaTotalSinCosteCierre() {
        FacturaDTO factura = model.generarFactura(14, 1);

        assertEquals(55.0, factura.getCosteTotal());
        assertTrue(factura.getDetalle().contains("90.0 minutos x 30.0 EUR/h"));
        assertTrue(factura.getDetalle().contains("Materiales: Producto limpiador = 10.0 EUR"));
    }

    @Test
    void testFacturaRegistraHistorial() {
        FacturaDTO factura = model.generarFactura(10, 1);

        String sql = "SELECT detalle FROM Historial "
                + "WHERE fk_incidencia = ? AND fk_persona = ? AND accion = 'FACTURACION'";
        assertTrue(db.executeQueryArray(sql, 10, 1)
                .stream()
                .anyMatch(row -> String.valueOf(row[0]).contains(factura.getNumeroFactura())));
    }

    @Test
    void testFacturaConCosteTotalCero() {
        FacturaDTO factura = model.generarFactura(15, 1);

        assertEquals(0.0, factura.getCosteTotal());
        assertTrue(factura.getDetalle().contains("Total facturado: 0.0 EUR"));
    }
}
