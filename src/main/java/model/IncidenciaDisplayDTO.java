package model;

public class IncidenciaDisplayDTO {

    private int id;
    private String tipo;
    private String descripcion;
    private String localizacion;
    private String fechaHoraRegistro;
    private String estado;
    private String usuarioCiudadano;

    private Integer horasPrevision;
    private String trabajosReparacion;

    public IncidenciaDisplayDTO() {
    }

    public IncidenciaDisplayDTO(
            int id,
            String tipo,
            String descripcion,
            String localizacion,
            String fechaHoraRegistro,
            String estado,
            String usuarioCiudadano,
            Integer horasPrevision,
            String trabajosReparacion
    ) {
        this.id = id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.localizacion = localizacion;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.estado = estado;
        this.usuarioCiudadano = usuarioCiudadano;
        this.horasPrevision = horasPrevision;
        this.trabajosReparacion = trabajosReparacion;
    }

    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public String getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public String getEstado() {
        return estado;
    }

    public String getUsuarioCiudadano() {
        return usuarioCiudadano;
    }

    public Integer getHorasPrevision() {
        return horasPrevision;
    }

    public String getTrabajosReparacion() {
        return trabajosReparacion;
    }

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

    public void setHorasPrevision(Integer horasPrevision) {
        this.horasPrevision = horasPrevision;
    }

    public void setTrabajosReparacion(String trabajosReparacion) {
        this.trabajosReparacion = trabajosReparacion;
    }
}