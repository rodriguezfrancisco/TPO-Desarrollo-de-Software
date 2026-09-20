package controller;

import entity.Factura;
import service.IFacturaService; 
import java.util.List;

public class FacturaController {
    // Dependemos de la interfaz, no de la clase concreta
    private final IFacturaService facturaService;

    public FacturaController(IFacturaService facturaService) {
        this.facturaService = facturaService;
    }

    public List<Factura> listarFacturas() {
        return facturaService.listarTodos(); // Lo hereda de IService
    }

    public void anularFactura(Long id) {
        facturaService.anularFactura(id); // Lo saca de IFacturaService
    }
}