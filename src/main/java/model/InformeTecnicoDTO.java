package model;

public class InformeTecnicoDTO {
	
    private String nombreTecnico;
    private int incidenciasResueltas;
    private int tiempoTotal;

    public InformeTecnicoDTO() {}

    public String getNombreTecnico() { return nombreTecnico; }
    public void setNombreTecnico(String nombreTecnico) { this.nombreTecnico = nombreTecnico; }

    public int getIncidenciasResueltas() { return incidenciasResueltas; }
    public void setIncidenciasResueltas(int incidenciasResueltas) { this.incidenciasResueltas = incidenciasResueltas; }

    public int getTiempoTotal() { return tiempoTotal; }
    public void setTiempoTotal(int tiempoTotal) { this.tiempoTotal = tiempoTotal; }
}