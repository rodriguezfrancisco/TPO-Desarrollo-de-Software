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

    @Override
    public double calcularCostoConsulta(double recargoOdontologo) {
        // Usa la tarifa base propia de esta clase[cite: 19]
        double base = (this.tarifaBase != null) ? this.tarifaBase : 0.0;
        return base + recargoOdontologo; 
    }

    @Override
    public String getDetalleCobertura() {
        return "Particular (Sin cobertura de obra social)";
    }

    @Override
    public String generarDetalleLiquidacion(double recargo, String especialidad) {
        double base = (this.tarifaBase != null) ? this.tarifaBase : 0.0;
        return String.format(" Tarifa Base Consulta:          $%10.2f\n", base) +
               String.format(" Recargo por %-16s:  $%10.2f\n", especialidad, recargo) +
               "----------------------------------------------------\n" +
               String.format(" TOTAL A ABONAR EN CAJA:        $%10.2f\n", calcularCostoConsulta(recargo));
    }
}
