package service;

import entity.OdOrtodoncia;
import entity.OdEndodoncia;
import entity.OdExtraccion;
import entity.Odontologo;
import repository.IRepository;
import repository.OdontologoRepository;

import java.util.List;
import java.util.Optional;

public class OdontologoService implements IService<Odontologo> {

    private IRepository<Odontologo> odontologoRepository;

    public OdontologoService() {
        this.odontologoRepository = new OdontologoRepository();
    }

    @Override
    public Odontologo registrar(Odontologo odontologo) {
        try {
            if (odontologo.getNombre() == null || odontologo.getNombre().trim().isEmpty()) {
                throw new DatoInvalidoException("Nombre Odontólogo", "El nombre es obligatorio.");
            }
            if (odontologo.getApellido() == null || odontologo.getApellido().trim().isEmpty()) {
                throw new DatoInvalidoException("Apellido Odontólogo", "El apellido es obligatorio.");
            }
            if (odontologo.getMatricula() == null || odontologo.getMatricula().trim().isEmpty()) {
                throw new DatoInvalidoException("Matrícula", "El número de matrícula es obligatorio.");
            }

            List<Odontologo> odontologosExistentes = odontologoRepository.buscarTodos();
            for (Odontologo o : odontologosExistentes) {
                if (o.getMatricula().equalsIgnoreCase(odontologo.getMatricula())) {
                    throw new ClinicaException("Ya existe un odontólogo con la matrícula: " + odontologo.getMatricula(), "ERR_ODO_002");
                }
            }

            Double recargo = null;
            if (odontologo instanceof OdOrtodoncia) {
                recargo = ((OdOrtodoncia) odontologo).getRecargoEspecialidad();
            } else if (odontologo instanceof OdEndodoncia) {
                recargo = ((OdEndodoncia) odontologo).getRecargoEspecialidad();
            } else if (odontologo instanceof OdExtraccion) {
                recargo = ((OdExtraccion) odontologo).getRecargoEspecialidad();
            }

            if (recargo == null || recargo <= 0) {
                throw new DatoInvalidoException("Recargo Especialidad", "Debe ser un multiplicador mayor a 0.");
            }

            System.out.println("Validaciones superadas con éxito. Registrando odontólogo...");
            return odontologoRepository.guardar(odontologo);

        } catch (ClinicaException e) {
            throw e;
        } catch (Exception e) {
            throw new ClinicaException("Error inesperado al registrar el odontólogo: " + e.getMessage(), "ERR_GEN_006");
        } finally {
            System.out.println("[Auditoría] Finalizó el intento de registro del odontólogo.");
        }
    }

    @Override
    public Optional<Odontologo> buscarPorId(Long id) {
        try {
            if (id == null || id <= 0) {
                throw new DatoInvalidoException("ID Odontólogo", "El ID provisto no es válido.");
            }
            return odontologoRepository.buscarPorId(id);
        } catch (Exception e) {
            throw new ClinicaException("Error al buscar odontólogo: " + e.getMessage(), "ERR_GEN_007");
        }
    }

    @Override
    public void eliminarPorId(Long id) {
        try {
            Optional<Odontologo> existente = odontologoRepository.buscarPorId(id);
            if (existente.isEmpty()) {
                throw new OdontologoNoEncontradoException(id);
            }
            odontologoRepository.eliminar(id);
            System.out.println("Odontólogo eliminado correctamente.");
        } catch (ClinicaException e) {
            throw e;
        } catch (Exception e) {
            throw new ClinicaException("Error al eliminar odontólogo: " + e.getMessage(), "ERR_GEN_008");
        }
    }

    @Override
    public Odontologo actualizar(Odontologo odontologo) {
        try {
            if (odontologo.getId() == null) {
                throw new DatoInvalidoException("ID Odontólogo", "Para actualizar, debe tener un ID asignado.");
            }
            Odontologo actualizado = odontologoRepository.actualizar(odontologo);
            if (actualizado == null) {
                throw new OdontologoNoEncontradoException(odontologo.getId());
            }
            return actualizado;
        } catch (ClinicaException e) {
            throw e;
        } catch (Exception e) {
            throw new ClinicaException("Error al actualizar odontólogo: " + e.getMessage(), "ERR_GEN_009");
        }
    }

    @Override
    public List<Odontologo> listarTodos() {
        try {
            return odontologoRepository.buscarTodos();
        } catch (Exception e) {
            throw new ClinicaException("Error al listar odontólogos: " + e.getMessage(), "ERR_GEN_010");
        }
    }
}