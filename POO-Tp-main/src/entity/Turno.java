package entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.io.Serializable;

public class Turno implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Paciente paciente;      // Asociación con Paciente
    private Odontologo odontologo;  // Asociación con Odontólogo
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoTurno estado;
    private Double montoFacturacion;

    public Turno() {
    }

    public Turno(Long id, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora, EstadoTurno estado) {
        this.id = id;
        this.paciente = paciente;
        this.odontologo = odontologo;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Odontologo getOdontologo() { return odontologo; }
    public void setOdontologo(Odontologo odontologo) { this.odontologo = odontologo; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public EstadoTurno getEstado() { return estado; }
    public void setEstado(EstadoTurno estado) { this.estado = estado; }

    public Double getMontoFacturacion() { return montoFacturacion; }
    public void setMontoFacturacion(Double montoFacturacion) { this.montoFacturacion = montoFacturacion; }

    @Override
    public String toString() {
        // Lógica de visualización
        String detalleCobro = (montoFacturacion != null && montoFacturacion > 0)
                ? "Abonar en caja: $" + montoFacturacion
                : "Cubierto por Obra Social";

        return "[ID Turno: " + id + "] Fecha: " + fecha + " Hora: " + hora +
                " | Paciente: " + paciente.getNombre() + " " + paciente.getApellido() +
                " | Médico: " + odontologo.getApellido() +
                " | Estado: " + estado +
                " | ===> " + detalleCobro;
    }
}
