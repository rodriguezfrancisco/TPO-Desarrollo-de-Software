package service;

import entity.Odontologo;
import repository.OdontologoRepository;

public class OdontologoService extends BaseService<Odontologo> {

    public OdontologoService() {
        // Inicializamos enviando el repositorio al constructor de BaseService
        super(new OdontologoRepository());
    }

    @Override
    protected String getNombreEntidad() { return "odontólogo"; }

    @Override
    protected String getCodigoErrorGenerico() { return "ERR_GEN_006"; }

    @Override
    protected void validarReglasNegocioRegistro(Odontologo odontologo) {
        Validador.validarTextoObligatorio(odontologo.getNombre(), "Nombre Odontólogo", "El nombre es obligatorio.");
        Validador.validarTextoObligatorio(odontologo.getApellido(), "Apellido Odontólogo", "El apellido es obligatorio.");
        Validador.validarTextoObligatorio(odontologo.getMatricula(), "Matrícula", "El número de matrícula es obligatorio.");
        
        // Validación con la nueva estructura del Enum
        Validador.validarObjeto(odontologo.getEspecialidad(), "Especialidad", "Debe asignar una especialidad.");
        Validador.validarNumeroPositivo(odontologo.getRecargoEspecialidad(), "Recargo Especialidad", "Debe ser un multiplicador mayor a 0.");

        // Validar Matrícula duplicada
        if (repository.buscarTodos().stream().anyMatch(o -> o.getMatricula().equalsIgnoreCase(odontologo.getMatricula()))) {
            throw new ClinicaException("Ya existe un odontólogo con la matrícula: " + odontologo.getMatricula(), "ERR_ODO_002");
        }
    }

    @Override
    protected void validarReglasNegocioActualizacion(Odontologo odontologo) {
        Validador.validarId(odontologo.getId(), "ID Odontólogo");
        Validador.validarTextoObligatorio(odontologo.getNombre(), "Nombre Odontólogo", "El nombre es obligatorio.");
        Validador.validarTextoObligatorio(odontologo.getApellido(), "Apellido Odontólogo", "El apellido es obligatorio.");
        Validador.validarTextoObligatorio(odontologo.getMatricula(), "Matrícula", "El número de matrícula es obligatorio.");
        Validador.validarObjeto(odontologo.getEspecialidad(), "Especialidad", "Debe asignar una especialidad.");
    }
}