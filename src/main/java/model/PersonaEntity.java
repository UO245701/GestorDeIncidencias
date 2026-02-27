package model;

/**
 * Entidad que representa a un usuario en la base de datos (Tabla Persona).
 * Los nombres de los atributos deben coincidir con los alias usados en las queries 
 * para que apache commons-dbutils realice el mapeo automáticamente.
 */
public class PersonaEntity {
    private int id;
    private String usuario;
    private String contrasena;
    private String tipo; // CIUDADANO, TECNICO, OPERADOR
    private String nombre;
    private String apellidos;
    private String dni;
    private String email;

    // Constructor vacío (necesario para el mapeo de Database.java)
    public PersonaEntity() {
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}