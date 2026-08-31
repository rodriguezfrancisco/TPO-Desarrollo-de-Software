package gui;

import controller.OdontologoController;
import controller.PacienteController;
import controller.TurnoController;
import service.OdontologoService;
import service.PacienteService;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private final PacienteController pacienteController;
    private final OdontologoController odontologoController;
    private final TurnoController turnoController;
    public VentanaPrincipal() {
        PacienteService pacienteServiceCompartido = new PacienteService();
        OdontologoService odontologoServiceCompartido = new OdontologoService();
        
        // 2. Inyectamos las mismas instancias en todos los controladores
        this.pacienteController = new PacienteController(pacienteServiceCompartido);
        this.odontologoController = new OdontologoController(odontologoServiceCompartido);
        this.turnoController = new TurnoController(pacienteServiceCompartido, odontologoServiceCompartido);
        
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
        pestanas.addTab("Turnos", new PanelTurnos(turnoController, pacienteController, odontologoController));

        add(pestanas, BorderLayout.CENTER);
    }
}