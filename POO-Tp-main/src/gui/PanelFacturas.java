package gui;

import controller.FacturaController;
import entity.Factura;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import service.ClinicaException;

public class PanelFacturas extends JPanel {
    private final FacturaController controller;
    private JTable tablaFacturas;
    private DefaultTableModel modeloTabla;
    private Long idFacturaSeleccionada = null;

    public PanelFacturas(FacturaController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        inicializarComponentes();
        cargarTabla();
    }

    private void inicializarComponentes() {
        // Botones de acción
        JButton btnAnular = new JButton("Anular Factura");
        btnAnular.setBackground(new Color(231, 76, 60)); // Color rojizo
        btnAnular.setForeground(Color.WHITE);
        btnAnular.addActionListener(e -> anularFactura());

        JButton btnRefrescar = new JButton("Refrescar Lista");
        btnRefrescar.addActionListener(e -> cargarTabla());

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBotones.add(btnAnular);
        pnlBotones.add(btnRefrescar);
        add(pnlBotones, BorderLayout.NORTH);

        // Tabla
        modeloTabla = new DefaultTableModel(new Object[]{"ID Factura", "Fecha", "Paciente", "ID Turno", "Monto Total", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaFacturas = new JTable(modeloTabla);

        tablaFacturas.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaFacturas.getSelectedRow();
            if (fila != -1 && !e.getValueIsAdjusting()) {
                idFacturaSeleccionada = (Long) tablaFacturas.getValueAt(fila, 0);
            }
        });

        add(new JScrollPane(tablaFacturas), BorderLayout.CENTER);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Factura f : controller.listarFacturas()) {
            String estado = f.isAnulada() ? "ANULADA" : "VIGENTE";
            String paciente = f.getTurno().getPaciente().getContacto().getApellido();
            
            modeloTabla.addRow(new Object[]{
                    f.getId(),
                    f.getFechaEmision().toLocalDate(),
                    paciente,
                    f.getTurno().getId(),
                    "$" + f.getMontoTotal(),
                    estado
            });
        }
        idFacturaSeleccionada = null;
    }

    private void anularFactura() {
        if (idFacturaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una factura de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea ANULAR la factura #" + idFacturaSeleccionada + "?\nEsta acción no se puede deshacer.",
            "Confirmar Anulación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                controller.anularFactura(idFacturaSeleccionada);
                JOptionPane.showMessageDialog(this, "Factura anulada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
            } catch (ClinicaException ex) { // Atrapa las excepciones de tu arquitectura
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Negocio", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}