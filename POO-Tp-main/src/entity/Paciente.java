package entity;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Paciente implements Serializable {
    private Long id;
    private DatosContacto contacto;
    private LocalDate fechaIngreso;
    private Domicilio domicilio;

    public Paciente() {
    }

    public Paciente(Long id, DatosContacto contacto, LocalDate fechaIngreso, Domicilio domicilio) {
        this.id = id;
        this.contacto = contacto;
        this.fechaIngreso = fechaIngreso;
        this.domicilio = domicilio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DatosContacto getContacto() {
        return contacto;
    }

    public void setContacto(DatosContacto contacto) {
        this.contacto = contacto;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }


    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + id +
                ", contacto=" + contacto +
                ", fechaIngreso=" + fechaIngreso +
                ", domicilio=" + domicilio +
                '}';
    }

    // --- MÉTODOS ABSTRACTOS PARA FACTURACIÓN (Polimorfismo) ---
    public abstract double calcularCostoConsulta(double recargoOdontologo);
    public abstract String getDetalleCobertura();
    public abstract String generarDetalleLiquidacion(double recargoOdontologo, String nombreEspecialidad);
}