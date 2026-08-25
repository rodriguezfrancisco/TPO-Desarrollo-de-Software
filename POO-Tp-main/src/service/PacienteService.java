package service;

import entity.PacienteObraSocial;
import entity.PacienteParticular;
import entity.Paciente;
import repository.IRepository;
import repository.PacienteRepository;

import java.util.List;
import java.util.Optional;

public class PacienteService implements IService<Paciente> {

    private IRepository<Paciente> pacienteRepository;

    public PacienteService() {
        this.pacienteRepository = new PacienteRepository();
    }

    @Override
    public Paciente registrar(Paciente paciente) {
        try {
            // 1. Validaciones generales
            if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
                throw new DatoInvalidoException("Nombre", "El nombre del paciente es obligatorio.");
            }
            if (paciente.getApellido() == null || paciente.getApellido().trim().isEmpty()) {
                throw new DatoInvalidoException("Apellido", "El apellido del paciente es obligatorio.");
            }
            if (paciente.getDni() == null || paciente.getDni().trim().isEmpty()) {
                throw new DatoInvalidoException("DNI", "El DNI del paciente es obligatorio.");
            }

            // 2. Validación de Lógica de Negocio: No puede haber DNI duplicado
            List<Paciente> pacientesExistentes = pacienteRepository.buscarTodos();
            for (Paciente p : pacientesExistentes) {
                if (p.getDni().equals(paciente.getDni())) {
                    throw new ClinicaException("Ya existe un paciente registrado con el DNI: " + paciente.getDni(), "ERR_PAC_002");
                }
            }

            // 3. Validaciones específicas de subclase
            if (paciente instanceof PacienteObraSocial) {
                PacienteObraSocial os = (PacienteObraSocial) paciente;
                if (os.getNombreObraSocial() == null || os.getNombreObraSocial().trim().isEmpty()) {
                    throw new DatoInvalidoException("Obra Social", "El nombre de la obra social es obligatorio.");
                }
                if (os.getNumAfiliado() == null || os.getNumAfiliado().trim().isEmpty()) {
                    throw new DatoInvalidoException("Número Afiliado", "El número de afiliado es obligatorio.");
                }
            } else if (paciente instanceof PacienteParticular) {
                PacienteParticular particular = (PacienteParticular) paciente;
                if (particular.getTarifaBase() == null || particular.getTarifaBase() <= 0) {
                    throw new DatoInvalidoException("Tarifa Base", "La tarifa base debe ser un monto mayor a 0.");
                }
            }

            System.out.println("Validaciones superadas con éxito. Registrando paciente...");
            return pacienteRepository.guardar(paciente);

        } catch (ClinicaException e) {
            // Re-lanzamos nuestras excepciones de negocio para que la UI las capture
            throw e; 
        } catch (Exception e) {
            // Captura cualquier error inesperado (NullPointerException, etc.)
            throw new ClinicaException("Error inesperado al registrar el paciente: " + e.getMessage(), "ERR_GEN_001");
        } finally {
            System.out.println("[Auditoría] Finalizó el intento de registro del paciente.");
        }
    }

    @Override
    public Optional<Paciente> buscarPorId(Long id) {
        try {
            if (id == null || id <= 0) {
                throw new DatoInvalidoException("ID Paciente", "El ID provisto no es válido.");
            }
            return pacienteRepository.buscarPorId(id);
        } catch (Exception e) {
            throw new ClinicaException("Error al buscar paciente por ID: " + e.getMessage(), "ERR_GEN_002");
        }
    }

    @Override
    public void eliminarPorId(Long id) {
        try {
            Optional<Paciente> existente = pacienteRepository.buscarPorId(id);
            if (existente.isEmpty()) {
                throw new PacienteNoEncontradoException(id);
            }
            pacienteRepository.eliminar(id);
            System.out.println("Paciente eliminado correctamente.");
        } catch (ClinicaException e) {
            throw e;
        } catch (Exception e) {
            throw new ClinicaException("Error al eliminar el paciente: " + e.getMessage(), "ERR_GEN_003");
        }
    }

    @Override
    public Paciente actualizar(Paciente paciente) {
        try {
            if (paciente.getId() == null) {
                throw new DatoInvalidoException("ID Paciente", "Para actualizar, el paciente debe tener un ID asignado.");
            }
            Paciente actualizado = pacienteRepository.actualizar(paciente);
            if (actualizado == null) {
                throw new PacienteNoEncontradoException(paciente.getId());
            }
            return actualizado;
        } catch (ClinicaException e) {
            throw e;
        } catch (Exception e) {
            throw new ClinicaException("Error al actualizar el paciente: " + e.getMessage(), "ERR_GEN_004");
        }
    }

    @Override
    public List<Paciente> listarTodos() {
        try {
            return pacienteRepository.buscarTodos();
        } catch (Exception e) {
            throw new ClinicaException("Error al listar los pacientes: " + e.getMessage(), "ERR_GEN_005");
        }
    }
}