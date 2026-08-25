package service;

import repository.IRepository;
import java.util.List;
import java.util.Optional;

public abstract class BaseService<T> implements IService<T> {

    // El repositorio se inyecta desde la subclase
    protected final IRepository<T> repository;

    public BaseService(IRepository<T> repository) {
        this.repository = repository;
    }

    // Métodos abstractos que obligan a las subclases a definir su propio comportamiento
    protected abstract String getNombreEntidad();
    protected abstract String getCodigoErrorGenerico();
    protected abstract void validarReglasNegocioRegistro(T entidad);
    protected abstract void validarReglasNegocioActualizacion(T entidad);

    @Override
    public T registrar(T entidad) {
        try {
            Validador.validarObjeto(entidad, "Entidad", "Los datos a registrar no pueden ser nulos.");
            validarReglasNegocioRegistro(entidad);
            
            System.out.println("Validaciones superadas con éxito. Registrando " + getNombreEntidad() + "...");
            return repository.guardar(entidad);
            
        } catch (ClinicaException e) {
            throw e;
        } catch (Exception e) {
            throw new ClinicaException("Error inesperado al registrar " + getNombreEntidad() + ": " + e.getMessage(), getCodigoErrorGenerico());
        } finally {
            System.out.println("[Auditoría] Finalizó el intento de registro de " + getNombreEntidad() + ".");
        }
    }

    @Override
    public Optional<T> buscarPorId(Long id) {
        try {
            Validador.validarId(id, "ID " + getNombreEntidad());
            return repository.buscarPorId(id);
        } catch (Exception e) {
            throw new ClinicaException("Error al buscar " + getNombreEntidad() + ": " + e.getMessage(), getCodigoErrorGenerico());
        }
    }

    @Override
    public void eliminarPorId(Long id) {
        try {
            Validador.validarId(id, "ID " + getNombreEntidad());
            Optional<T> existente = repository.buscarPorId(id);
            if (existente.isEmpty()) {
                throw new ClinicaException("El " + getNombreEntidad() + " con ID " + id + " no fue encontrado.", getCodigoErrorGenerico());
            }
            repository.eliminar(id);
            System.out.println(getNombreEntidad() + " eliminado correctamente.");
        } catch (ClinicaException e) {
            throw e;
        } catch (Exception e) {
            throw new ClinicaException("Error al eliminar " + getNombreEntidad() + ": " + e.getMessage(), getCodigoErrorGenerico());
        }
    }

    @Override
    public T actualizar(T entidad) {
        try {
            Validador.validarObjeto(entidad, "Entidad", "Los datos a actualizar no pueden ser nulos.");
            validarReglasNegocioActualizacion(entidad); 
            
            T actualizado = repository.actualizar(entidad);
            if (actualizado == null) {
                throw new ClinicaException("No se encontró " + getNombreEntidad() + " para actualizar.", getCodigoErrorGenerico());
            }
            return actualizado;
        } catch (ClinicaException e) {
            throw e;
        } catch (Exception e) {
            throw new ClinicaException("Error al actualizar " + getNombreEntidad() + ": " + e.getMessage(), getCodigoErrorGenerico());
        }
    }

    @Override
    public List<T> listarTodos() {
        try {
            return repository.buscarTodos();
        } catch (Exception e) {
            throw new ClinicaException("Error al listar " + getNombreEntidad() + ": " + e.getMessage(), getCodigoErrorGenerico());
        }
    }
}