package util;

import java.awt.EventQueue;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

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
import model.ListadoIncidenciasPeriodoModel;
import view.ListadoIncidenciasPeriodoView;

import controller.CerrarIncidenciasController;
import model.CerrarIncidenciasModel;
import view.CerrarIncidenciasView;

import controller.RegistrarTrabajoController;
import model.RegistrarTrabajoModel;
import view.RegistrarTrabajoView;

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
		frame.setTitle("Main");
		frame.setBounds(0, 0, 400, 250);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		JButton btnRegistrarIncidencia = new JButton("Registrar incidencia");
		btnRegistrarIncidencia.addActionListener(new ActionListener() { //NOSONAR
			public void actionPerformed(ActionEvent e) {
				RegistrarIncidenciaController controller =
						new RegistrarIncidenciaController(new RegistrarIncidenciasModel(), new RegistrarIncidenciaView());
				controller.initController();
			}
		});
		
		JButton btnConsultarMisIncidencias = new JButton("Consultar mis incidencias");
		btnConsultarMisIncidencias.addActionListener(new ActionListener() { //NOSONAR
			public void actionPerformed(ActionEvent e) {
				ConsultarIncidenciasController controller =
						new ConsultarIncidenciasController(new ConsultarIncidenciasModel(), new ConsultarIncidenciasView());
				controller.initController();
			}
		});
		
		JButton btnHistorial = new JButton("Visualizar historial");
		btnHistorial.addActionListener(e -> {
		    VisualizarHistorialController controller =
		        new VisualizarHistorialController(
		            new VisualizarHistorialModel(),
		            new VisualizarHistorialView());
		    controller.initController();
		});
		frame.getContentPane().add(btnHistorial);
		
		frame.getContentPane().add(btnConsultarMisIncidencias);
		frame.getContentPane().add(btnRegistrarIncidencia);

		JButton btnInicializarBaseDeDatos = new JButton("Inicializar Base de Datos en Blanco");
		btnInicializarBaseDeDatos.addActionListener(new ActionListener() { //NOSONAR
			public void actionPerformed(ActionEvent e) {
				Database db = new Database();
				db.createDatabase(false);
			}
		});
		frame.getContentPane().add(btnInicializarBaseDeDatos);

		JButton btnCargarDatosIniciales = new JButton("Cargar Datos Iniciales para Pruebas");
		btnCargarDatosIniciales.addActionListener(new ActionListener() { //NOSONAR
			public void actionPerformed(ActionEvent e) {
				Database db = new Database();
				db.createDatabase(false);
				db.loadDatabase();
			}
		});
		frame.getContentPane().add(btnCargarDatosIniciales);
		
		JButton btnValidar = new JButton("Validar Incidencias (Operador)");
		btnValidar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        new ValidarIncidenciaController(new ValidarIncidenciasModel(), new ValidarIncidenciaView()).initController();
		    }
		});
		frame.getContentPane().add(btnValidar);
		
		JButton btnAsignarIncidencia = new JButton("Asignar incidencia");
		btnAsignarIncidencia.addActionListener(new ActionListener() { //NOSONAR
			public void actionPerformed(ActionEvent e) {
				AsignarIncidenciaController controller =
						new AsignarIncidenciaController(new AsignarIncidenciaModel(), new AsignarIncidenciaView());
				controller.initController();
			}
		});
		frame.getContentPane().add(btnAsignarIncidencia);

		JButton btnTecnico = new JButton("Abrir Panel de Técnico");
		btnTecnico.addActionListener(e -> {
		    TecnicoView v = new TecnicoView();
		    TecnicoModel m = new TecnicoModel();
		    new TecnicoController(m, v).initController();
		});
		frame.getContentPane().add(btnTecnico);
		
		JButton btnResolverIncidencia = new JButton("Resolver incidencia");
		btnResolverIncidencia.addActionListener(e -> {
		    ResolverIncidenciaView v = new ResolverIncidenciaView();
		    ResolverIncidenciaModel m = new ResolverIncidenciaModel();
		    new ResolverIncidenciaController(m, v).initController();
		});
		frame.getContentPane().add(btnResolverIncidencia);
		
		JButton btnCerrarIncidencias = new JButton("Cerrar incidencias");

		btnCerrarIncidencias.addActionListener(e -> {
		    CerrarIncidenciasView v = new CerrarIncidenciasView();
		    CerrarIncidenciasModel m = new CerrarIncidenciasModel();
		    new CerrarIncidenciasController(m, v).initController();
		});

		frame.getContentPane().add(btnCerrarIncidencias);
		
		JButton btnRegistrarTrabajo = new JButton("Registrar trabajos");

		btnRegistrarTrabajo.addActionListener(e -> {

		    RegistrarTrabajoView v = new RegistrarTrabajoView();
		    RegistrarTrabajoModel m = new RegistrarTrabajoModel();

		    RegistrarTrabajoController c = new RegistrarTrabajoController(m, v);
		    c.initController();

		    v.getFrame().setVisible(true);
		});

		frame.getContentPane().add(btnRegistrarTrabajo);
		
		JButton btnListadoPeriodo = new JButton("Listado incidencias por periodo");

		btnListadoPeriodo.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {

		        ListadoIncidenciasPeriodoController controller =
		                new ListadoIncidenciasPeriodoController(
		                        new ListadoIncidenciasPeriodoModel(),
		                        new ListadoIncidenciasPeriodoView()
		                );

		        controller.initController();
		    }
		});

		frame.getContentPane().add(btnListadoPeriodo);
		
		// Añadir donde creas los demás botones en SwingMain.java
		JButton btnInformeMensual = new JButton("Informe Mensual (Responsable)");
		btnInformeMensual.addActionListener(e -> {
		    InformeMensualView v = new InformeMensualView();
		    InformeMensualModel m = new InformeMensualModel();
		    new InformeMensualController(m, v).initController();
		});
		frame.getContentPane().add(btnInformeMensual);
	}

	public JFrame getFrame() { return this.frame; }
}