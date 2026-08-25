package service;

public class DatoInvalidoException extends ClinicaException{
    public DatoInvalidoException(String campo, String motivo) {
        super("Error en [" + campo + "]: " + motivo, "ERR_DAT_003");
    }
}
