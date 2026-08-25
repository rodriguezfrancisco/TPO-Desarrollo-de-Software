package service;

public class TurnoYaReservadoException extends ClinicaException{

    public TurnoYaReservadoException(String detalle) {
        super("Horario ocupado: " + detalle, "ERR_TUR_002");
    }
}
