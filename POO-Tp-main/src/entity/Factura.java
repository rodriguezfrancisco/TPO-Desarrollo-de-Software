package entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Factura implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Turno turno; // Relación con el turno que la generó
    private LocalDateTime fechaEmision;
    private Double montoTotal;
    private String rutaArchivo; // Dónde está guardado el .txt
    private boolean anulada = false;

    public Factura() {}

    public Factura(Turno turno, LocalDateTime fechaEmision, Double montoTotal, String rutaArchivo) {
        this.turno = turno;
        this.fechaEmision = fechaEmision;
        this.montoTotal = montoTotal;
        this.rutaArchivo = rutaArchivo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Turno getTurno() { return turno; }
    public void setTurno(Turno turno) { this.turno = turno; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
    public Double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(Double montoTotal) { this.montoTotal = montoTotal; }
    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }
    public boolean isAnulada() { return anulada;}
    public void setAnulada(boolean anulada) { this.anulada = anulada; }
}