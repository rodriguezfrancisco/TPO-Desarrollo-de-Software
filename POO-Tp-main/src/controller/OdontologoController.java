package controller;

import entity.Odontologo;
import service.OdontologoService;
import java.util.List;
import java.util.Optional;

public class OdontologoController {
    private final OdontologoService odontologoService;

    public OdontologoController(OdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }

    public Odontologo registrarOdontologo(Odontologo o) { 
        return odontologoService.registrar(o); 
    }
    
    public Odontologo actualizarOdontologo(Odontologo o) {
        return odontologoService.actualizar(o);
    }
    
    public List<Odontologo> listarOdontologos() { 
        return odontologoService.listarTodos(); 
    }
    
    public void eliminarOdontologoPorId(Long id) { 
        odontologoService.eliminarPorId(id);
    }
    
    public Optional<Odontologo> buscarOdontologoId(Long id) { 
        return odontologoService.buscarPorId(id); 
    }
}