package gui;

import controller.*;
import entity.*;
import repository.*;
import service.*;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    
    private final PacienteController pacienteController;
    private final OdontologoController odontologoController;
    private final TurnoController turnoController;
    private final FacturaController facturaController;

    public VentanaPrincipal() {
        // 1. CAPA DE PERSISTENCIA
        // Instanciamos los repositorios concretos pero los tratamos por su abstracción
        IRepository<Paciente> pacienteRepo = new PacienteRepository();
        IRepository<Odontologo> odontologoRepo = new OdontologoRepository();
        IRepository<Turno> turnoRepo = new TurnoRepository();
        IRepository<Factura> facturaRepo = new FacturaRepository();

        // 2. CAPA DE NEGOCIO (SERVICIOS)
        // Inyectamos los repositorios en los servicios. 
        // Nota: PacienteService y OdontologoService se instancian con su clase concreta 
        // porque TurnoService requiere específicamente esas clases (o sus interfaces) en su constructor.
        PacienteService pacienteService = new PacienteService(pacienteRepo);
        OdontologoService odontologoService = new OdontologoService(odontologoRepo);
        
       // Creamos la nueva estrategia de almacenamiento usando el Adapter
        IFacturaStorage almacenamientoPdf = new FacturaPdfAdapter("facturas");
        
        // Inyectamos el repositorio, el nuevo adaptador PDF y el formateador base
        IFacturaService facturaService = new FacturaService(facturaRepo, almacenamientoPdf, new FacturaComprobanteFormatter());
        
        IService<Turno> turnoService = new TurnoService(turnoRepo, pacienteService, odontologoService);

        // 3. CAPA DE CONTROL
        // Los controladores reciben estrictamente las dependencias (interfaces donde corresponde)
        this.pacienteController = new PacienteController(pacienteService);
        this.odontologoController = new OdontologoController(odontologoService);
        this.turnoController = new TurnoController(turnoService, facturaService);
        this.facturaController = new FacturaController(facturaService);
        
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("Sistema de Gestión Clínica - Sonrisa Feliz");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        // Título Superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(41, 128, 185));
        
        JLabel lblTitulo = new JLabel("Clínica Odontológica 'Sonrisa Feliz'");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        
        panelSuperior.add(lblTitulo);
        add(panelSuperior, BorderLayout.NORTH);

        // Contenedor de Pestañas (JTabbedPane)
        JTabbedPane pestanas = new JTabbedPane();
        
        pestanas.addTab("Pacientes", new PanelPacientes(pacienteController));
        pestanas.addTab("Odontólogos", new PanelOdontologos(odontologoController));
        
        // PanelTurnos necesita 3 controladores según su constructor
        pestanas.addTab("Turnos", new PanelTurnos(turnoController, pacienteController, odontologoController));
        
        // La nueva pestaña de facturación
        pestanas.addTab("Facturas", new PanelFacturas(facturaController));

        add(pestanas, BorderLayout.CENTER);
    }
}