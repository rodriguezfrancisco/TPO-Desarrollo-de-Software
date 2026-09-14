package repository;

import entity.Factura;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FacturaRepository implements IRepository<Factura> {
    // Simulación de base de datos en memoria (A reemplazar por tu conexión SQL)
    private List<Factura> facturas = new ArrayList<>();
    private Long idAutoIncremental = 1L;

    @Override
    public Factura guardar(Factura factura) {
        factura.setId(idAutoIncremental++);
        facturas.add(factura);
        return factura;
    }

    @Override
    public Optional<Factura> buscarPorId(Long id) {
        return facturas.stream().filter(f -> f.getId().equals(id)).findFirst();
    }

    @Override
    public void eliminar(Long id) {
        facturas.removeIf(f -> f.getId().equals(id));
    }

    @Override
    public Factura actualizar(Factura factura) {
        eliminar(factura.getId());
        facturas.add(factura);
        return factura;
    }

    @Override
    public List<Factura> buscarTodos() {
        return facturas;
    }
}