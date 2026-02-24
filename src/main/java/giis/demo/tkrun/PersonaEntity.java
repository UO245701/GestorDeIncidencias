package giis.demo.tkrun;

public class PersonaEntity {
	private Integer id;
    private String usuario;
    private String contrasena;
    private String tipo; // ciudadano, tecnico, operador
    private String nombre;
    private String apellidos;
    private String dni;

    public PersonaEntity() {}

	public PersonaEntity(Integer id, String usuario, String contrasena, String tipo, String nombre, String apellidos,
			String dni) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.tipo = tipo;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}
    
   

}
