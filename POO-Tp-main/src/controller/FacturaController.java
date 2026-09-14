package controller;

import entity.Factura;
import service.FacturaService;
import java.util.List;

public class FacturaController {
    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    public List<Factura> listarFacturas() {
        return facturaService.listarTodos();
    }

    public void anularFactura(Long id) {
        facturaService.anularFactura(id);
    }
}