package entity;
import java.io.Serializable;

public interface ICoberturaStrategy extends Serializable {
    double calcularCostoConsulta(double recargoOdontologo);
    String getDetalleCobertura();
    String generarDetalleLiquidacion(double recargoOdontologo, String nombreEspecialidad);
}