package entity;

import java.time.LocalDate;
import java.io.Serializable;

public class PacienteParticular extends Paciente implements Serializable{
    private static final long serialVersionUID = 1L;

    private Double tarifaBase;

    public PacienteParticular() {}

    public PacienteParticular(Long id, String nombre, String apellido, String dni, String email, String telefono, LocalDate fechaIngreso, Domicilio domicilio, Double tarifaBase) {
        super(id, nombre, apellido, dni, email, telefono, fechaIngreso, domicilio);
        this.tarifaBase = tarifaBase;
    }

    public Double getTarifaBase() { return tarifaBase; }
    public void setTarifaBase(Double tarifaBase) { this.tarifaBase = tarifaBase; }
}
