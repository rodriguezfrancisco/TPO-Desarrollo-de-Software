package controller;

import entity.Paciente;
import service.IService; 
import java.util.List;
import java.util.Optional;

public class PacienteController {
    private final IService<Paciente> pacienteService;

    public PacienteController(IService<Paciente> pacienteService) {
        this.pacienteService = pacienteService;
    }

    public Paciente registrarPaciente(Paciente p) { 
        return pacienteService.registrar(p); 
    }
    
    public Paciente actualizarPaciente(Paciente p) {
        return pacienteService.actualizar(p);
    }
    
    public List<Paciente> listarPacientes() { 
        return pacienteService.listarTodos(); 
    }
    
    public void eliminarPacientePorId(Long id) { 
        pacienteService.eliminarPorId(id);
    }
    
    public Optional<Paciente> buscarPacienteId(Long id) { 
        return pacienteService.buscarPorId(id); 
    }
}