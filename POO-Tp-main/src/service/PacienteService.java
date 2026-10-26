package service;

import entity.CoberturaObraSocial;
import entity.CoberturaParticular;
import entity.Paciente;

public class PacienteService extends BaseService<Paciente> {

    public PacienteService(repository.IRepository<Paciente> repositorio) {
        super(repositorio); // Pasamos la dependencia al BaseService
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

        // Validaciones por estrategia de cobertura
        if (paciente.getCobertura() instanceof CoberturaObraSocial cos) {
            Validador.validarTextoObligatorio(cos.getNombreObraSocial(), "Obra Social", "El nombre de la obra social es obligatorio.");
            Validador.validarTextoObligatorio(cos.getNumAfiliado(), "Número Afiliado", "El número de afiliado es obligatorio.");
        } else if (paciente.getCobertura() instanceof CoberturaParticular cp) {
            Validador.validarNumeroPositivo(cp.getTarifaBase(), "Tarifa Base", "La tarifa base debe ser un monto mayor a 0.");
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