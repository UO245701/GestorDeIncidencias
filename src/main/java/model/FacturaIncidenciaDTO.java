package model;

public class FacturaIncidenciaDTO {

    private int id;
    private String tipo;
    private String descripcion;
    private String fechaHoraRegistro;
    private String estado;
    private String tecnico;
    private Integer tiempoReal;
    private Double precioHora;
    private Double costeMateriales;
    private String descripcionMateriales;
    private String trabajosRealizados;
    private Double costeTotal;

    public FacturaIncidenciaDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(String fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public Integer getTiempoReal() {
        return tiempoReal;
    }

    public void setTiempoReal(Integer tiempoReal) {
        this.tiempoReal = tiempoReal;
    }

    public Double getPrecioHora() {
        return precioHora;
    }

    public void setPrecioHora(Double precioHora) {
        this.precioHora = precioHora;
    }

    public Double getCosteMateriales() {
        return costeMateriales;
    }

    public void setCosteMateriales(Double costeMateriales) {
        this.costeMateriales = costeMateriales;
    }

    public String getDescripcionMateriales() {
        return descripcionMateriales;
    }

    public void setDescripcionMateriales(String descripcionMateriales) {
        this.descripcionMateriales = descripcionMateriales;
    }

    public String getTrabajosRealizados() {
        return trabajosRealizados;
    }

    public void setTrabajosRealizados(String trabajosRealizados) {
        this.trabajosRealizados = trabajosRealizados;
    }

    public Double getCosteTotal() {
        return costeTotal;
    }

    public void setCosteTotal(Double costeTotal) {
        this.costeTotal = costeTotal;
    }
}
