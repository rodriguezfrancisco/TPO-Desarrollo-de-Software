package entity;
import java.io.Serializable;

public class CoberturaParticular implements ICoberturaStrategy, Serializable {
    private static final long serialVersionUID = 1L;
    private Double tarifaBase;

    public CoberturaParticular() {}

    public CoberturaParticular(Double tarifaBase) {
        this.tarifaBase = tarifaBase;
    }

    public Double getTarifaBase() { return tarifaBase; }
    public void setTarifaBase(Double tarifaBase) { this.tarifaBase = tarifaBase; }

    @Override
    public double calcularCostoConsulta(double recargoOdontologo) {
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