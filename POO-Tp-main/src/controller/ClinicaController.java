package controller;
import entity.*;
import service.*;
import java.util.List;
import java.util.Optional;

public class ClinicaController {
    private final PacienteService pacienteService;
    private final OdontologoService odontologoService;
    private final TurnoService turnoService;
    private final FacturaService facturaService;

    public ClinicaController() {
        this.pacienteService = new PacienteService();
        this.odontologoService = new OdontologoService();
        this.turnoService = new TurnoService(pacienteService, odontologoService);
        this.facturaService = new FacturaService();
    }

    // --- Métodos de Delegación Pacientes ---
    public Paciente registrarPaciente(Paciente p) { return pacienteService.registrar(p); }
    public Paciente actualizarPaciente(Paciente p){return pacienteService.actualizar(p);}
    public List<Paciente> listarPacientes() { return pacienteService.listarTodos(); }
    public void eliminarPacientePorId(Long id) { pacienteService.eliminarPorId(id);}

    // --- Métodos de Delegación Odontologos ---
    public Odontologo registrarOdontologo(Odontologo o) { return odontologoService.registrar(o); }
    public Odontologo actualizarOdontologo(Odontologo o) {return odontologoService.actualizar(o);}
    public List<Odontologo> listarOdontologos() { return odontologoService.listarTodos(); }
    public void eliminarOdontologoPorId(Long id) { odontologoService.eliminarPorId(id);}

    // --- Métodos de Delegación Turnos ---
    public Turno agendarTurno(Turno t) { return turnoService.registrar(t); }
    public Turno actualizarTurno(Turno t) {return turnoService.actualizar(t); }
    public List<Turno> listarTurnos() { return turnoService.listarTodos(); }
    public void eliminarTurnoPorId(Long id) { turnoService.eliminarPorId(id);}

    // --- Métodos de Delegación Buscar ---
    public Optional<Paciente> buscarPacienteId(Long id) { return pacienteService.buscarPorId(id); }
    public Optional<Odontologo> buscarOdontologoId(Long id) { return odontologoService.buscarPorId(id); }
    public Optional<Turno> buscarTurnoId(Long id) {return turnoService.buscarPorId(id);}

    // --- Métodos de Facturación ---
    public String emitirFactura(Turno turno) {
        return facturaService.generarFactura(turno);
    }
}

