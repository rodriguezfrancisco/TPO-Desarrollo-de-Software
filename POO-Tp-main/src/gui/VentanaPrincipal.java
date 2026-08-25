package gui;

import controller.ClinicaController;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private final ClinicaController controller;

    public VentanaPrincipal() {
        this.controller = new ClinicaController();
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
        pestanas.addTab("Pacientes", new PanelPacientes(controller));
        pestanas.addTab("Odontólogos", new PanelOdontologos(controller));
        pestanas.addTab("Turnos", new PanelTurnos(controller));

        add(pestanas, BorderLayout.CENTER);
    }
}