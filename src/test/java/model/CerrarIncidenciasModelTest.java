package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import util.Database;

public class CerrarIncidenciasModelTest {

    private CerrarIncidenciasModel model;
    private Database db;

    @Before
    public void setUp() {
        model = new CerrarIncidenciasModel();
        db = new Database();
        
        // 1. Limpiamos las tablas afectadas para tener un entorno aislado
        db.executeUpdate("DELETE FROM Historial");
        db.executeUpdate("DELETE FROM Incidencia");
        db.executeUpdate("DELETE FROM PresupuestoTipoIncidencia");

        // 2. Insertamos Datos de Prueba Controlados (Clases de Equivalencia)
        
        // CE1 y CE5: Presupuesto válido y con saldo (Máximo 5000, Consumido 1000 -> Disponible 4000)
        db.executeUpdate("INSERT INTO PresupuestoTipoIncidencia (tipo, importe_maximo, importe_consumido, fecha_inicio, fecha_fin, activo) " +
                         "VALUES ('Alumbrado', 5000.0, 1000.0, date('now', '-10 days'), date('now', '+10 days'), 1)");

        // CE1 y CE6: Presupuesto agotado (Máximo 2000, Consumido 2000 -> Disponible 0)
        db.executeUpdate("INSERT INTO PresupuestoTipoIncidencia (tipo, importe_maximo, importe_consumido, fecha_inicio, fecha_fin, activo) " +
                         "VALUES ('Limpieza', 2000.0, 2000.0, date('now', '-10 days'), date('now', '+10 days'), 1)");

        // CE2: Presupuesto caducado (fechas en el pasado)
        db.executeUpdate("INSERT INTO PresupuestoTipoIncidencia (tipo, importe_maximo, importe_consumido, fecha_inicio, fecha_fin, activo) " +
                         "VALUES ('Calzada', 3000.0, 0.0, date('now', '-20 days'), date('now', '-10 days'), 1)");

        // CE3: Presupuesto inactivo (activo = 0)
        db.executeUpdate("INSERT INTO PresupuestoTipoIncidencia (tipo, importe_maximo, importe_consumido, fecha_inicio, fecha_fin, activo) " +
                         "VALUES ('Zonas verdes', 4000.0, 0.0, date('now', '-10 days'), date('now', '+10 days'), 0)");
                         
        // Datos para prueba de cierre de incidencia
        // Insertamos una persona y una zona genérica para cumplir FKs
        db.executeUpdate("INSERT OR IGNORE INTO Zona (id_zona, nombre) VALUES (1, 'Zona Test')");
        db.executeUpdate("INSERT OR IGNORE INTO Persona (id_persona, usuario, contrasena, tipo) VALUES (99, 'test_user', '123', 'TECNICO')");
        db.executeUpdate("INSERT INTO Incidencia (id_incidencia, tipo, descripcion, estado, fk_zona, fk_ciudadano) " +
                         "VALUES (100, 'Alumbrado', 'Test', 'RESUELTA', 1, 99)");
    }

    @Test
    public void testPresupuestoActivoYEnFecha() {
        // CE1 y CE5: Alumbrado debería tener 4000.0 disponibles (5000 - 1000)
        double disponible = model.getPresupuestoDisponible("Alumbrado");
        assertEquals("El presupuesto disponible debe ser 4000.0", 4000.0, disponible, 0.01);
    }

    @Test
    public void testPresupuestoAgotado() {
        // CE1 y CE6: Limpieza debería tener 0.0
        double disponible = model.getPresupuestoDisponible("Limpieza");
        assertEquals("El presupuesto agotado debe devolver 0.0", 0.0, disponible, 0.01);
    }

    @Test
    public void testPresupuestoCaducado() {
        // CE2: Calzada está fuera de fecha, debe devolver 0.0
        double disponible = model.getPresupuestoDisponible("Calzada");
        assertEquals("Un presupuesto caducado debe devolver 0.0", 0.0, disponible, 0.01);
    }

    @Test
    public void testPresupuestoInactivo() {
        // CE3: Zonas verdes está inactivo, debe devolver 0.0
        double disponible = model.getPresupuestoDisponible("Zonas verdes");
        assertEquals("Un presupuesto inactivo debe devolver 0.0", 0.0, disponible, 0.01);
    }

    @Test
    public void testPresupuestoInexistente() {
        // CE4: Tipo no registrado en BBDD
        double disponible = model.getPresupuestoDisponible("Mobiliario inexistente");
        assertEquals("Un tipo sin presupuesto debe devolver 0.0", 0.0, disponible, 0.01);
    }

    @Test
    public void testCierreIncidenciaImputacionCorrecta() {
        // Ejecutamos el cierre imputando un coste de 500€ a 'Alumbrado'
        model.cerrarIncidenciaConCoste(100L, "Alumbrado", 500.0, 99);

        // Verificamos que la incidencia está CERRADA y el coste asignado
        java.util.List<Object[]> inc = db.executeQueryArray("SELECT estado, coste_total FROM Incidencia WHERE id_incidencia = 100");
        assertEquals("El estado debe ser CERRADA", "CERRADA", inc.get(0)[0].toString());
        assertEquals("El coste total debe ser 500.0", 500.0, Double.parseDouble(inc.get(0)[1].toString()), 0.01);

        // Verificamos que el presupuesto consumido ha subido de 1000 a 1500
        java.util.List<Object[]> pres = db.executeQueryArray("SELECT importe_consumido FROM PresupuestoTipoIncidencia WHERE tipo = 'Alumbrado'");
        assertEquals("El consumido debe actualizarse a 1500.0", 1500.0, Double.parseDouble(pres.get(0)[0].toString()), 0.01);
        
        // Verificamos que se ha generado historial
        java.util.List<Object[]> hist = db.executeQueryArray("SELECT accion FROM Historial WHERE fk_incidencia = 100 AND estado = 'CERRADA'");
        assertFalse("Debe existir un registro en el historial", hist.isEmpty());
    }
}