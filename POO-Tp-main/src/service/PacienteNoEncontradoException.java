package service;

public class PacienteNoEncontradoException extends ClinicaException {

    public PacienteNoEncontradoException(Long id) {
        super("El paciente con ID " + id + " no fue encontrado.", "ERR_PAC_001");
    }
}
