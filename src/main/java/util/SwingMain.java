package util;

import java.awt.EventQueue;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

import controller.RegistrarIncidenciaController;
import controller.ValidarIncidenciaController;
import controller.VisualizarHistorialController;
import model.RegistrarIncidenciasModel;
import model.ValidarIncidenciasModel;
import model.VisualizarHistorialModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import view.RegistrarIncidenciaView;
import view.ValidarIncidenciaView;
import view.VisualizarHistorialView;

import controller.ConsultarIncidenciasController;
import model.ConsultarIncidenciasModel;
import view.ConsultarIncidenciasView;


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
		frame.setBounds(0, 0, 360, 220);
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
	}

	public JFrame getFrame() { return this.frame; }
}