package service;

public class ClinicaException extends RuntimeException{
    private final String codigoError;

    public ClinicaException(String mensaje, String codigoError) {
        super(mensaje);
        this.codigoError = codigoError;
    }

    public String getCodigoError() {
        return codigoError;
    }
}
