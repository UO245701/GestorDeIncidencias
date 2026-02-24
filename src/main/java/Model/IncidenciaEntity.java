package Model;

import java.time.LocalDateTime;

public class IncidenciaEntity {
    private Integer id; // null antes de insertar
    private String tipo;
    private String descripcion;
    private String localizacion;
    private LocalDateTime fechaHoraRegistro;
    private String estado;
    private Integer ciudadanoId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getLocalizacion() { return localizacion; }
    public void setLocalizacion(String localizacion) { this.localizacion = localizacion; }

    public LocalDateTime getFechaHoraRegistro() { return fechaHoraRegistro; }
    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) { this.fechaHoraRegistro = fechaHoraRegistro; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getCiudadanoId() { return ciudadanoId; }
    public void setCiudadanoId(Integer ciudadanoId) { this.ciudadanoId = ciudadanoId; }
}