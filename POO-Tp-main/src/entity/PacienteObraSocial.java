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

    @Override
    public double calcularCostoConsulta(double recargoOdontologo) {
        // La obra social cubre el 100% de la consulta y el recargo
        return 0.0; 
    }

    @Override
    public String getDetalleCobertura() {
        // Usa los atributos propios de la obra social[cite: 18]
        return "Obra Social: " + this.getNombreObraSocial() + " (Afiliado Nº: " + this.getNumAfiliado() + ")";
    }

    @Override
    public String generarDetalleLiquidacion(double recargo, String especialidad) {
        return " Consulta y Procedimiento 100% Cubiertos por Obra Social.\n" +
               String.format(" Recargo Especialidad asumido:  $%10.2f\n", recargo) +
               "----------------------------------------------------\n" +
               " TOTAL A ABONAR:                $      0.00\n";
    }
}