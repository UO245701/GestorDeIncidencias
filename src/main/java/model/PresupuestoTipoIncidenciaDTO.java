package model;

public class PresupuestoTipoIncidenciaDTO {
    private int id;
    private String tipo;
    private double importeMaximo;
    private double importeConsumido;
    private double importeDisponible;
    private String fechaInicio;
    private String fechaFin;
    private String activo;

    public PresupuestoTipoIncidenciaDTO() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getImporteMaximo() { return importeMaximo; }
    public void setImporteMaximo(double importeMaximo) { this.importeMaximo = importeMaximo; }

    public double getImporteConsumido() { return importeConsumido; }
    public void setImporteConsumido(double importeConsumido) { this.importeConsumido = importeConsumido; }

    public double getImporteDisponible() { return importeDisponible; }
    public void setImporteDisponible(double importeDisponible) { this.importeDisponible = importeDisponible; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public String getActivo() { return activo; }
    public void setActivo(String activo) { this.activo = activo; }
}
