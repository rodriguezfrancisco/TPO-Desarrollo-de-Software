package entity.decorator;

public class CoberturaObraSocialDecorator extends PrestacionDecorator {
    private final String nombreObraSocial;
    private final double porcentajeCobertura; // e.g. 1.0 para 100%

    public CoberturaObraSocialDecorator(Prestacion prestacion, String nombreObraSocial, double porcentajeCobertura) {
        super(prestacion);
        this.nombreObraSocial = nombreObraSocial;
        this.porcentajeCobertura = porcentajeCobertura;
    }

    @Override
    public double calcularCosto() {
        double subtotal = super.calcularCosto();
        double cubierto = subtotal * this.porcentajeCobertura;
        return Math.max(0.0, subtotal - cubierto);
    }

    @Override
    public String generarDetalle() {
        double subtotal = super.calcularCosto();
        double bonificacion = subtotal * this.porcentajeCobertura;
        return super.generarDetalle() +
               String.format(" Cobertura %s (%.0f%%):     -$%10.2f\n", 
                       this.nombreObraSocial, (this.porcentajeCobertura * 100), bonificacion);
    }
}