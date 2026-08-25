package service;

import java.util.List;
import java.util.Optional;

public interface IService<T> {
    T registrar(T t);
    Optional<T> buscarPorId(Long id);
    void eliminarPorId(Long id);
    T actualizar(T t);
    List<T> listarTodos();
}

