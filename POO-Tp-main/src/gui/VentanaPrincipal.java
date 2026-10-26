package gui;

import facade.ClinicaFacade;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private final ClinicaFacade clinicaFacade;

    public VentanaPrincipal() {
        this.clinicaFacade = new ClinicaFacade(null, null, null, null); // Fachada unificada
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("Sistema de Gestión Clínica - Sonrisa Feliz");
        setSize(950, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(41, 128, 185));
        JLabel lblTitulo = new JLabel("Clínica Odontológica 'Sonrisa Feliz'");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        panelSuperior.add(lblTitulo);
        add(panelSuperior, BorderLayout.NORTH);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Pacientes", new PanelPacientes(clinicaFacade.getPacienteController()));
        pestanas.addTab("Odontólogos", new PanelOdontologos(clinicaFacade.getOdontologoController()));
        pestanas.addTab("Turnos", new PanelTurnos(
            clinicaFacade.getTurnoController(),
            clinicaFacade.getPacienteController(),
            clinicaFacade.getOdontologoController()));
        pestanas.addTab("Facturas", new PanelFacturas(clinicaFacade.getFacturaController()));
        add(pestanas, BorderLayout.CENTER);
    }
}