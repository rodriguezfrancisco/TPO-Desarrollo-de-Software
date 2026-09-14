package controller;

import entity.Turno;
import service.FacturaService;
import service.TurnoService;
import service.PacienteService;
import service.OdontologoService;
import java.util.List;
import java.util.Optional;

public class TurnoController {
    private final TurnoService turnoService;
    private final FacturaService facturaService;

    public TurnoController(PacienteService pacienteService, OdontologoService odontologoService, FacturaService facturaService) {
        this.turnoService = new TurnoService(pacienteService, odontologoService);
        this.facturaService = facturaService;
    }

    public Turno agendarTurno(Turno t) { 
        return turnoService.registrar(t); 
    }
    
    public Turno actualizarTurno(Turno t) {
        return turnoService.actualizar(t); 
    }
    
    public List<Turno> listarTurnos() { 
        return turnoService.listarTodos(); 
    }
    
    public void eliminarTurnoPorId(Long id) { 
        turnoService.eliminarPorId(id);
    }
    
    public Optional<Turno> buscarTurnoId(Long id) {
        return turnoService.buscarPorId(id);
    }

    
    public String emitirFactura(Turno turno) {
        entity.Factura nuevaFactura = facturaService.emitirYRegistrarFactura(turno);
        turnoService.actualizar(turno);
        // Retornamos solo la ruta física para que el PanelTurnos la muestre
        return nuevaFactura.getRutaArchivo(); 
    }
}