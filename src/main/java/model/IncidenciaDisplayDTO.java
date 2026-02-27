package model;


public class IncidenciaDisplayDTO {
    private int id;
    private String tipo;
    private String descripcion;
    private String localizacion;
    private String fechaHoraRegistro;
    private String estado;
    private String usuarioCiudadano;

    
    
    public IncidenciaDisplayDTO() {

	}

	public IncidenciaDisplayDTO(int id, String tipo, String descripcion, String localizacion,
                                String fechaHoraRegistro, String estado, String usuarioCiudadano) {
        this.id = id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.localizacion = localizacion;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.estado = estado;
        this.usuarioCiudadano = usuarioCiudadano;
    }

    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public String getLocalizacion() { return localizacion; }
    public String getFechaHoraRegistro() { return fechaHoraRegistro; }
    public String getEstado() { return estado; }
    public String getUsuarioCiudadano() { return usuarioCiudadano; }

	public void setId(int id) {
		this.id = id;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public void setFechaHoraRegistro(String fechaHoraRegistro) {
		this.fechaHoraRegistro = fechaHoraRegistro;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setUsuarioCiudadano(String usuarioCiudadano) {
		this.usuarioCiudadano = usuarioCiudadano;
	}
    
    
}
