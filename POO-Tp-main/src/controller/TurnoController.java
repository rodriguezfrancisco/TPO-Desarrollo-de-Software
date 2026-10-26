package controller;

import entity.Turno;
import service.IService;
import service.IFacturaService; // Importamos la interfaz
import java.util.List;
import java.util.Optional;

public class TurnoController {
    private final IService<Turno> turnoService;
    private final IFacturaService facturaService; // 100% abstraído

    public TurnoController(IService<Turno> turnoService, IFacturaService facturaService) {
        this.turnoService = turnoService;
        this.facturaService = facturaService;
    }

    public Turno agendarTurno(Turno t) { return turnoService.registrar(t); }
    public Turno actualizarTurno(Turno t) { return turnoService.actualizar(t); }
    public List<Turno> listarTurnos() { return turnoService.listarTodos(); }
    public void eliminarTurnoPorId(Long id) { turnoService.eliminarPorId(id); }
    public Optional<Turno> buscarTurnoId(Long id) { return turnoService.buscarPorId(id); }

    public String emitirFactura(Turno turno, boolean esPdf) {
        // Intercambiamos el adaptador según la elección de la interfaz gráfica
        if (esPdf) {
            facturaService.setAlmacenamiento(new repository.FacturaPdfAdapter("facturas"));
        } else {
            facturaService.setAlmacenamiento(new repository.FacturaFileStorage("facturas"));
        }
        
        entity.Factura nuevaFactura = facturaService.emitirYRegistrarFactura(turno);
        turnoService.actualizar(turno);
        return nuevaFactura.getRutaArchivo();
    }
}