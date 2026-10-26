package entity.decorator;

public abstract class PrestacionDecorator implements Prestacion {
    protected final Prestacion prestacionEnvuelta;

    public PrestacionDecorator(Prestacion prestacionEnvuelta) {
        this.prestacionEnvuelta = prestacionEnvuelta;
    }

    @Override
    public double calcularCosto() {
        return prestacionEnvuelta.calcularCosto();
    }

    @Override
    public String generarDetalle() {
        return prestacionEnvuelta.generarDetalle();
    }
}