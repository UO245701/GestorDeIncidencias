package model;

public class HistorialDTO {

    private String fechaHora;
    private int id;
    private String estado;

    public HistorialDTO() {}

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
