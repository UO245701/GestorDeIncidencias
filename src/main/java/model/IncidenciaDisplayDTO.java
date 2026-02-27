package model;

public class IncidenciaDisplayDTO {
	private int id;
	private String tipo;
	private String descripcion;
	private String localizacion;
	private String fechaHora;
	private String estado;
	private String usuarioCiudadano;

	public IncidenciaDisplayDTO() {}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getTipo() { return tipo; }
	public void setTipo(String tipo) { this.tipo = tipo; }

	public String getDescripcion() { return descripcion; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

	public String getLocalizacion() { return localizacion; }
	public void setLocalizacion(String localizacion) { this.localizacion = localizacion; }

	public String getFechaHora() { return fechaHora; }
	public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }

	public String getUsuarioCiudadano() { return usuarioCiudadano; }
	public void setUsuarioCiudadano(String usuarioCiudadano) { this.usuarioCiudadano = usuarioCiudadano; }
}