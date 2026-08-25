package entity;

import java.time.LocalDate;
import java.io.Serializable;

public class PacienteObraSocial extends Paciente implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombreObraSocial;
    private String numAfiliado;

    public PacienteObraSocial() {}

    public PacienteObraSocial(Long id, String nombre, String apellido, String dni, String email, String telefono, LocalDate fechaIngreso, Domicilio domicilio, String nombreObraSocial, String numAfiliado) {
        super(id, nombre, apellido, dni, email, telefono, fechaIngreso, domicilio);
        this.nombreObraSocial = nombreObraSocial;
        this.numAfiliado = numAfiliado;
    }

    public String getNombreObraSocial() { return nombreObraSocial; }
    public void setNombreObraSocial(String nombreObraSocial) { this.nombreObraSocial = nombreObraSocial; }
    public String getNumAfiliado() { return numAfiliado; }
    public void setNumAfiliado(String numAfiliado) { this.numAfiliado = numAfiliado; }
}