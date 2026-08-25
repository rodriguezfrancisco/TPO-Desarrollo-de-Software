package service;

public class OdontologoNoEncontradoException extends ClinicaException{

    public OdontologoNoEncontradoException(Long id) {
        super("El odontólogo con ID " + id + " no fue encontrado.", "ERR_ODO_001");
    }
}
