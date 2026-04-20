package model;

public class FacturaDTO {

    private long idFactura;
    private String numeroFactura;
    private String fechaEmision;
    private String emisor;
    private String detalle;
    private double costeTotal;
    private long idIncidencia;

    public FacturaDTO(long idFactura, String numeroFactura, String fechaEmision, String emisor,
            String detalle, double costeTotal, long idIncidencia) {
        this.idFactura = idFactura;
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.emisor = emisor;
        this.detalle = detalle;
        this.costeTotal = costeTotal;
        this.idIncidencia = idIncidencia;
    }

    public long getIdFactura() {
        return idFactura;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public String getEmisor() {
        return emisor;
    }

    public String getDetalle() {
        return detalle;
    }

    public double getCosteTotal() {
        return costeTotal;
    }

    public long getIdIncidencia() {
        return idIncidencia;
    }
}
