package model;

public class IncidenciaExportacionDTO {
    private int id;
    private String tipo;
    private String zona;
    private String descripcion;
    private String estado;
    private String fechaAlta;
    private double costeEstimado;
    private double costeMateriales;
    private double costeTotal;
    private String tecnico;

    public IncidenciaExportacionDTO() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(String fechaAlta) { this.fechaAlta = fechaAlta; }

    public double getCosteEstimado() { return costeEstimado; }
    public void setCosteEstimado(double costeEstimado) { this.costeEstimado = costeEstimado; }

    public double getCosteMateriales() { return costeMateriales; }
    public void setCosteMateriales(double costeMateriales) { this.costeMateriales = costeMateriales; }

    public double getCosteTotal() { return costeTotal; }
    public void setCosteTotal(double costeTotal) { this.costeTotal = costeTotal; }

    public String getTecnico() { return tecnico; }
    public void setTecnico(String tecnico) { this.tecnico = tecnico; }
}
