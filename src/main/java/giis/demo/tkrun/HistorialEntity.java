package giis.demo.tkrun;

import java.time.LocalDateTime;

public class HistorialEntity {
	private Integer id;
    private LocalDateTime fechaHora;
    private String estado;
    private String accion;
    private String detalle;
    private Integer fkIncidencia;
    private Integer fkPersona; // ID de quien ejecuta el cambio (Ciudadano, Operador o Técnico)

    public HistorialEntity() {}

	public HistorialEntity(Integer id, LocalDateTime fechaHora, String estado, String accion, String detalle,
			Integer fkIncidencia, Integer fkPersona) {
		super();
		this.id = id;
		this.fechaHora = fechaHora;
		this.estado = estado;
		this.accion = accion;
		this.detalle = detalle;
		this.fkIncidencia = fkIncidencia;
		this.fkPersona = fkPersona;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getFkIncidencia() {
		return fkIncidencia;
	}

	public void setFkIncidencia(Integer fkIncidencia) {
		this.fkIncidencia = fkIncidencia;
	}

	public Integer getFkPersona() {
		return fkPersona;
	}

	public void setFkPersona(Integer fkPersona) {
		this.fkPersona = fkPersona;
	}

    
    
}
