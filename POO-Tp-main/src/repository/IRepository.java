package repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    T guardar(T entidad);
    Optional<T> buscarPorId(Long id);
    List<T> buscarTodos();
    void eliminar(Long id);
    T actualizar(T entidad);
}