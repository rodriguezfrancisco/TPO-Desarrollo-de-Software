package entity.decorator;

public class RecargoEspecialidadDecorator extends PrestacionDecorator {
    private final double recargo;
    private final String especialidad;

    public RecargoEspecialidadDecorator(Prestacion prestacion, double recargo, String especialidad) {
        super(prestacion);
        this.recargo = Math.max(0.0, recargo);
        this.especialidad = (especialidad != null && !especialidad.isBlank()) ? especialidad : "General";
    }

    @Override
    public double calcularCosto() {
        return super.calcularCosto() + this.recargo;
    }

    @Override
    public String generarDetalle() {
        return super.generarDetalle() + 
               String.format(" Recargo por %-16s:  $%10.2f\n", this.especialidad, this.recargo);
    }
}