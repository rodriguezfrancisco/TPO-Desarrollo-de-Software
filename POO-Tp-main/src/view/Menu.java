package view;

import gui.VentanaPrincipal;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class Menu {

    public Menu() {
        // Configuramos el Look and Feel nativo para que la GUI no se vea vieja
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Usa el estilo por defecto de Java si falla
        }
    }

    /**
     * Levanta la interfaz gráfica en el hilo de despacho de eventos de Swing (EDT)
     */
    public void iniciar() {
        System.out.println("🚀 Iniciando la interfaz gráfica de 'Sonrisa Feliz'...");

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}