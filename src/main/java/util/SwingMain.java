package util;

import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import controller.AsignarIncidenciaController;
import controller.RegistrarIncidenciaController;
import controller.ResolverIncidenciaController;
import controller.TecnicoController;
import controller.ValidarIncidenciaController;

import controller.VisualizarHistorialController;
import model.AsignarIncidenciaModel;
import model.RegistrarIncidenciasModel;
import model.ResolverIncidenciaModel;
import model.TecnicoModel;
import model.ValidarIncidenciasModel;
import model.VisualizarHistorialModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import view.AsignarIncidenciaView;
import view.RegistrarIncidenciaView;
import view.ResolverIncidenciaView;
import view.TecnicoView;
import view.ValidarIncidenciaView;
import view.VisualizarHistorialView;

import controller.ConsultarIncidenciasController;
import controller.InformeMensualController;
import model.ConsultarIncidenciasModel;
import model.InformeMensualModel;
import view.ConsultarIncidenciasView;
import view.InformeMensualView;
import controller.ListadoIncidenciasPeriodoController;
import controller.ReaperturaIncidenciasController;
import model.ListadoIncidenciasPeriodoModel;
import model.ReaperturaIncidenciasModel;
import view.ListadoIncidenciasPeriodoView;
import view.ReaperturaIncidenciasView;
import controller.CerrarIncidenciasController;
import model.CerrarIncidenciasModel;
import view.CerrarIncidenciasView;

import controller.RegistrarTrabajoController;
import model.RegistrarTrabajoModel;
import view.RegistrarTrabajoView;
import controller.PresupuestoTipoIncidenciaController;
import model.PresupuestoTipoIncidenciaModel;
import view.PresupuestoTipoIncidenciaView;
import controller.ExportacionHistorialController;
import model.ExportacionHistorialModel;
import view.ExportacionHistorialView;
import controller.FacturaIncidenciaController;
import model.FacturaIncidenciaModel;
import view.FacturaIncidenciaView;

public class SwingMain {

	private JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { //NOSONAR
			public void run() {
				try {
					SwingMain window = new SwingMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace(); //NOSONAR
				}
			}
		});
	}

	public SwingMain() {
		initialize();
	}

	private void initialize() {
        frame = new JFrame();
        frame.setTitle("Gestor de Incidencias - Menú Principal");
        // Hacemos la ventana más grande y la centramos
        frame.setSize(850, 600);
        frame.setLocationRelativeTo(null); 
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        // Usamos un GridLayout de 2 filas y 3 columnas con márgenes (Gaps) de 10px
        frame.getContentPane().setLayout(new GridLayout(2, 3, 10, 10));
        ((JPanel)frame.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ==========================================
        // PANEL 1: CIUDADANO
        // ==========================================
        JPanel panelCiudadano = crearPanelAgrupado("Acciones de Ciudadano");
        
        JButton btnRegistrarIncidencia = new JButton("Registrar incidencia");
        btnRegistrarIncidencia.addActionListener(e -> {
            new RegistrarIncidenciaController(new RegistrarIncidenciasModel(), new RegistrarIncidenciaView()).initController();
        });
        
        JButton btnConsultarMisIncidencias = new JButton("Consultar mis incidencias");
        btnConsultarMisIncidencias.addActionListener(e -> {
            new ConsultarIncidenciasController(new ConsultarIncidenciasModel(), new ConsultarIncidenciasView()).initController();
        });
        
        panelCiudadano.add(btnRegistrarIncidencia);
        panelCiudadano.add(btnConsultarMisIncidencias);
        frame.getContentPane().add(panelCiudadano);

        // ==========================================
        // PANEL 2: OPERADOR
        // ==========================================
        JPanel panelOperador = crearPanelAgrupado("Acciones de Operador");
        
        JButton btnValidar = new JButton("Validar Incidencias (Operador)");
        btnValidar.addActionListener(e -> {
            new ValidarIncidenciaController(new ValidarIncidenciasModel(), new ValidarIncidenciaView()).initController();
        });
        
        JButton btnAsignarIncidencia = new JButton("Asignar incidencia");
        btnAsignarIncidencia.addActionListener(e -> {
            new AsignarIncidenciaController(new AsignarIncidenciaModel(), new AsignarIncidenciaView()).initController();
        });

        JButton btnFacturas = new JButton("Generar factura de incidencia");
        btnFacturas.addActionListener(e -> {
            new FacturaIncidenciaController(new FacturaIncidenciaModel(), new FacturaIncidenciaView()).initController();
        });
        
        panelOperador.add(btnValidar);
        panelOperador.add(btnAsignarIncidencia);
        panelOperador.add(btnFacturas);
        frame.getContentPane().add(panelOperador);

        // ==========================================
        // PANEL 3: TÉCNICO
        // ==========================================
        JPanel panelTecnico = crearPanelAgrupado("Acciones de Técnico");
        
        JButton btnTecnico = new JButton("Abrir Panel de Técnico");
        btnTecnico.addActionListener(e -> {
            new TecnicoController(new TecnicoModel(), new TecnicoView()).initController();
        });
        
        JButton btnResolverIncidencia = new JButton("Resolver incidencia");
        btnResolverIncidencia.addActionListener(e -> {
            new ResolverIncidenciaController(new ResolverIncidenciaModel(), new ResolverIncidenciaView()).initController();
        });
        
        JButton btnRegistrarTrabajo = new JButton("Registrar trabajos");
        btnRegistrarTrabajo.addActionListener(e -> {
            RegistrarTrabajoView v = new RegistrarTrabajoView();
            new RegistrarTrabajoController(new RegistrarTrabajoModel(), v).initController();
            v.getFrame().setVisible(true);
        });
        
        panelTecnico.add(btnTecnico);
        panelTecnico.add(btnResolverIncidencia);
        panelTecnico.add(btnRegistrarTrabajo);
        frame.getContentPane().add(panelTecnico);

        // ==========================================
        // PANEL 4: GESTOR / RESPONSABLE
        // ==========================================
        JPanel panelGestor = crearPanelAgrupado("Gestión y Responsables");
        
        JButton btnPresupuestos = new JButton("Definir presupuestos por tipo");
        btnPresupuestos.addActionListener(e -> {
            new PresupuestoTipoIncidenciaController(new PresupuestoTipoIncidenciaModel(), new PresupuestoTipoIncidenciaView()).initController();
        });
        
        JButton btnCerrarIncidencias = new JButton("Cerrar incidencias");
        btnCerrarIncidencias.addActionListener(e -> {
            new CerrarIncidenciasController(new CerrarIncidenciasModel(), new CerrarIncidenciasView()).initController();
        });
        
        JButton btnReaperturaIncidencias = new JButton("Reabrir incidencias");
        btnReaperturaIncidencias.addActionListener(e -> {
            new ReaperturaIncidenciasController(new ReaperturaIncidenciasModel(), new ReaperturaIncidenciasView()).initController();
        });
        
        panelGestor.add(btnPresupuestos);
        panelGestor.add(btnCerrarIncidencias);
        panelGestor.add(btnReaperturaIncidencias);
        frame.getContentPane().add(panelGestor);

        // ==========================================
        // PANEL 5: INFORMES E HISTORIAL
        // ==========================================
        JPanel panelInformes = crearPanelAgrupado("Informes e Historial");
        
        JButton btnHistorial = new JButton("Visualizar historial");
        btnHistorial.addActionListener(e -> {
            new VisualizarHistorialController(new VisualizarHistorialModel(), new VisualizarHistorialView()).initController();
        });
        
        JButton btnListadoPeriodo = new JButton("Listado incidencias por periodo");
        btnListadoPeriodo.addActionListener(e -> {
            new ListadoIncidenciasPeriodoController(new ListadoIncidenciasPeriodoModel(), new ListadoIncidenciasPeriodoView()).initController();
        });
        
        JButton btnInformeMensual = new JButton("Informe Mensual (Responsable)");
        btnInformeMensual.addActionListener(e -> {
            new InformeMensualController(new InformeMensualModel(), new InformeMensualView()).initController();
        });
        
        JButton btnExportarHistorial = new JButton("Exportar historial de incidencias");
        btnExportarHistorial.addActionListener(e -> {
            new ExportacionHistorialController(new ExportacionHistorialModel(), new ExportacionHistorialView()).initController();
        });
        
        panelInformes.add(btnHistorial);
        panelInformes.add(btnListadoPeriodo);
        panelInformes.add(btnInformeMensual);
        panelInformes.add(btnExportarHistorial);
        frame.getContentPane().add(panelInformes);

        // ==========================================
        // PANEL 6: SISTEMA / BASE DE DATOS
        // ==========================================
        JPanel panelSistema = crearPanelAgrupado("Sistema / Base de Datos");
        
        JButton btnInicializarBaseDeDatos = new JButton("Inicializar BBDD en Blanco");
        btnInicializarBaseDeDatos.addActionListener(e -> {
            Database db = new Database();
            db.createDatabase(false);
        });
        
        JButton btnCargarDatosIniciales = new JButton("Cargar Datos para Pruebas");
        btnCargarDatosIniciales.addActionListener(e -> {
            Database db = new Database();
            db.createDatabase(false);
            db.loadDatabase();
        });
        
        panelSistema.add(btnInicializarBaseDeDatos);
        panelSistema.add(btnCargarDatosIniciales);
        frame.getContentPane().add(panelSistema);
    }
	
	/**
     * Método auxiliar para crear paneles con bordes titulados y layout vertical.
     */
    private JPanel crearPanelAgrupado(String titulo) {
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridLayout(0, 1, 5, 5)); // 1 columna, filas dinámicas
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), titulo, TitledBorder.LEFT, TitledBorder.TOP));
        return panel;
    }

	public JFrame getFrame() { return this.frame; }
}
