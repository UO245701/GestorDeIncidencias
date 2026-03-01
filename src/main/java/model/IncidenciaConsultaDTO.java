package model;

public class IncidenciaConsultaDTO {
	
	private int id;
	private String tipo;
	private String descripcion;
	private String fechaHora;
	private String estado;

	public IncidenciaConsultaDTO() {
		// necesario para BeanListHandler
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getTipo() { return tipo; }
	public void setTipo(String tipo) { this.tipo = tipo; }

	public String getDescripcion() { return descripcion; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

	public String getFechaHora() { return fechaHora; }
	public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }

}
