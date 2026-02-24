package Model;

public class IncidenciaDisplayDTO {
    private int id;
    private String tipo;
    private String descripcion;
    private String localizacion;
    private String fechaHoraRegistro;
    private String estado;
    private String usuarioCiudadano;

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
}