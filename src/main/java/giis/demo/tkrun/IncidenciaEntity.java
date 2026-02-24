package giis.demo.tkrun;

import java.time.LocalDateTime;

public class IncidenciaEntity {
	private Integer id;
    private String tipo;
    private String descripcion;
    private String localizacion;
    private LocalDateTime fechaHora;
    private String estado;
    private Double horasEstimadas;
    private Double coste;
    
    // IDs de las personas involucradas
    private Integer idCiudadano; 
    private Integer idTecnico;

    public IncidenciaEntity() {}

	public IncidenciaEntity(Integer id, String tipo, String descripcion, String localizacion, LocalDateTime fechaHora,
			String estado, Double horasEstimadas, Double coste, Integer idCiudadano, Integer idTecnico) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.localizacion = localizacion;
		this.fechaHora = fechaHora;
		this.estado = estado;
		this.horasEstimadas = horasEstimadas;
		this.coste = coste;
		this.idCiudadano = idCiudadano;
		this.idTecnico = idTecnico;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Double getHorasEstimadas() {
		return horasEstimadas;
	}

	public void setHorasEstimadas(Double horasEstimadas) {
		this.horasEstimadas = horasEstimadas;
	}

	public Double getCoste() {
		return coste;
	}

	public void setCoste(Double coste) {
		this.coste = coste;
	}
    
    
}
