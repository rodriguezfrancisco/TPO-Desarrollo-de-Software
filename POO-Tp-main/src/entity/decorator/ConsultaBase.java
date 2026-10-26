package entity.decorator;

public class ConsultaBase implements Prestacion {
    private final double tarifaBase;

    public ConsultaBase(double tarifaBase) {
        this.tarifaBase = Math.max(0.0, tarifaBase);
    }

    @Override
    public double calcularCosto() {
        return this.tarifaBase;
    }

    @Override
    public String generarDetalle() {
        return String.format(" Tarifa Base Consulta:          $%10.2f\n", this.tarifaBase);
    }
}