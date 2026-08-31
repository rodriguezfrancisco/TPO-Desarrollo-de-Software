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

    public TurnoController(PacienteService pacienteService, OdontologoService odontologoService) {
        this.turnoService = new TurnoService(pacienteService, odontologoService);
        this.facturaService = new FacturaService();
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

    // El método de facturación se queda aquí, asociado a la entidad que le da origen
    public String emitirFactura(Turno turno) {
        return facturaService.generarFactura(turno);
    }
}