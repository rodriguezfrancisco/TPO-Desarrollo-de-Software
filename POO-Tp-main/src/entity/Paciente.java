package entity;
import java.io.Serializable;
import java.time.LocalDate;

public class Paciente implements Serializable {
    private Long id;
    private DatosContacto contacto;
    private LocalDate fechaIngreso;
    private Domicilio domicilio;
    
    // Referencia al motor de comportamiento (Strategy)
    private ICoberturaStrategy cobertura;

    public Paciente() {}

    public Paciente(Long id, DatosContacto contacto, LocalDate fechaIngreso, Domicilio domicilio, ICoberturaStrategy cobertura) {
        this.id = id;
        this.contacto = contacto;
        this.fechaIngreso = fechaIngreso;
        this.domicilio = domicilio;
        this.cobertura = cobertura;
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

    public ICoberturaStrategy getCobertura() {
        return cobertura;
    }

    // Permite cambiar la estrategia de cobro al vuelo
    public void setCobertura(ICoberturaStrategy cobertura) {
        this.cobertura = cobertura;
    }

    // MÉTODOS DELEGADOS A LA ESTRATEGIA (Polimorfismo limpio)
    public double calcularCostoConsulta(double recargoOdontologo) {
        return cobertura.calcularCostoConsulta(recargoOdontologo);
    }

    public String getDetalleCobertura() {
        return cobertura.getDetalleCobertura();
    }

    public String generarDetalleLiquidacion(double recargoOdontologo, String nombreEspecialidad) {
        return cobertura.generarDetalleLiquidacion(recargoOdontologo, nombreEspecialidad);
    }

}