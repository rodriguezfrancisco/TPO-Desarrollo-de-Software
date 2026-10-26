package entity;
import java.io.Serializable;

public class CoberturaObraSocial implements ICoberturaStrategy, Serializable {
    private static final long serialVersionUID = 1L;
    private String nombreObraSocial;
    private String numAfiliado;

    public CoberturaObraSocial() {}

    public CoberturaObraSocial(String nombreObraSocial, String numAfiliado) {
        this.nombreObraSocial = nombreObraSocial;
        this.numAfiliado = numAfiliado;
    }

    public String getNombreObraSocial() { return nombreObraSocial; }
    public void setNombreObraSocial(String nombreObraSocial) { this.nombreObraSocial = nombreObraSocial; }
    public String getNumAfiliado() { return numAfiliado; }
    public void setNumAfiliado(String numAfiliado) { this.numAfiliado = numAfiliado; }

    @Override
    public double calcularCostoConsulta(double recargoOdontologo) {
        return 0.0; 
    }

    @Override
    public String getDetalleCobertura() {
        return "Obra Social: " + this.nombreObraSocial + " (Afiliado Nº: " + this.numAfiliado + ")";
    }

    @Override
    public String generarDetalleLiquidacion(double recargo, String especialidad) {
        return " Consulta y Procedimiento 100% Cubiertos por Obra Social.\n" +
               String.format(" Recargo Especialidad asumido:  $%10.2f\n", recargo) +
               "----------------------------------------------------\n" +
               " TOTAL A ABONAR:                $      0.00\n";
    }
}