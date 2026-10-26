package facade;

import entity.Factura;
import entity.Odontologo;
import entity.Paciente;
import entity.Turno;
import service.FacturaService;
import service.OdontologoService;
import service.PacienteService;
import service.TurnoService;

import java.util.List;
import java.util.Optional;

public class ClinicaFacade {
    private final PacienteService pacienteService;
    private final OdontologoService odontologoService;
    private final TurnoService turnoService;
    private final FacturaService facturaService;

    public ClinicaFacade(PacienteService pacienteService,
                         OdontologoService odontologoService,
                         TurnoService turnoService,
                         FacturaService facturaService) {
        this.pacienteService = pacienteService;
        this.odontologoService = odontologoService;
        this.turnoService = turnoService;
        this.facturaService = facturaService;
    }

    // --- OPERACIONES SUBSISTEMA PACIENTES ---
    public Paciente registrarPaciente(Paciente p) { return pacienteService.registrar(p); }
    public Paciente actualizarPaciente(Paciente p) { return pacienteService.actualizar(p); }
    public void eliminarPaciente(Long id) { pacienteService.eliminarPorId(id); }
    public Optional<Paciente> buscarPacientePorId(Long id) { return pacienteService.buscarPorId(id); }
    public List<Paciente> listarPacientes() { return pacienteService.listarTodos(); }

    // --- OPERACIONES SUBSISTEMA ODONTÓLOGOS ---
    public Odontologo registrarOdontologo(Odontologo o) { return odontologoService.registrar(o); }
    public Odontologo actualizarOdontologo(Odontologo o) { return odontologoService.actualizar(o); }
    public void eliminarOdontologo(Long id) { odontologoService.eliminarPorId(id); }
    public Optional<Odontologo> buscarOdontologoPorId(Long id) { return odontologoService.buscarPorId(id); }
    public List<Odontologo> listarOdontologos() { return odontologoService.listarTodos(); }

    // --- OPERACIONES SUBSISTEMA TURNOS ---
    public Turno agendarTurno(Turno t) { return turnoService.registrar(t); }
    public Turno actualizarTurno(Turno t) { return turnoService.actualizar(t); }
    public void eliminarTurno(Long id) { turnoService.eliminarPorId(id); }
    public Optional<Turno> buscarTurnoPorId(Long id) { return turnoService.buscarPorId(id); }
    public List<Turno> listarTurnos() { return turnoService.listarTodos(); }

    // --- OPERACIONES INTEGRADAS / FACTURACIÓN ---
    public String emitirFacturaDeTurno(Long idTurno) {
        Turno turno = turnoService.buscarPorId(idTurno)
                .orElseThrow(() -> new service.ClinicaException("Turno no encontrado.", "ERR_TUR_001"));
        Factura f = facturaService.emitirYRegistrarFactura(turno);
        turnoService.actualizar(turno);
        return f.getRutaArchivo();
    }

    public List<Factura> listarFacturas() { return facturaService.listarTodos(); }
    public void anularFactura(Long id) { facturaService.anularFactura(id); }
}