package service;

import entity.Paciente;
import entity.PacienteObraSocial;
import entity.PacienteParticular;
import repository.PacienteRepository;

public class PacienteService extends BaseService<Paciente> {

    public PacienteService() {
        // Inicializamos enviando el repositorio al constructor de BaseService
        super(new PacienteRepository());
    }

    @Override
    protected String getNombreEntidad() { return "paciente"; }

    @Override
    protected String getCodigoErrorGenerico() { return "ERR_GEN_001"; }

    @Override
    protected void validarReglasNegocioRegistro(Paciente paciente) {
        Validador.validarTextoObligatorio(paciente.getContacto().getNombre(), "Nombre", "El nombre es obligatorio.");
        Validador.validarTextoObligatorio(paciente.getContacto().getApellido(), "Apellido", "El apellido es obligatorio.");
        Validador.validarTextoObligatorio(paciente.getContacto().getDni(), "DNI", "El DNI es obligatorio.");

        // Validar DNI duplicado
        if (repository.buscarTodos().stream().anyMatch(p -> p.getContacto().getDni().equals(paciente.getContacto().getDni()))) {
            throw new ClinicaException("Ya existe un paciente con el DNI: " + paciente.getContacto().getDni(), "ERR_PAC_002");
        }

        // Validaciones por tipo de paciente
        if (paciente instanceof PacienteObraSocial os) {
            Validador.validarTextoObligatorio(os.getNombreObraSocial(), "Obra Social", "El nombre de la obra social es obligatorio.");
            Validador.validarTextoObligatorio(os.getNumAfiliado(), "Número Afiliado", "El número de afiliado es obligatorio.");
        } else if (paciente instanceof PacienteParticular particular) {
            Validador.validarNumeroPositivo(particular.getTarifaBase(), "Tarifa Base", "La tarifa base debe ser un monto mayor a 0.");
        }
    }

    @Override
    protected void validarReglasNegocioActualizacion(Paciente paciente) {
        Validador.validarId(paciente.getId(), "ID Paciente");
        Validador.validarTextoObligatorio(paciente.getContacto().getNombre(), "Nombre", "El nombre es obligatorio.");
        Validador.validarTextoObligatorio(paciente.getContacto().getApellido(), "Apellido", "El apellido es obligatorio.");
        Validador.validarTextoObligatorio(paciente.getContacto().getDni(), "DNI", "El DNI es obligatorio.");
    }
}