package gui;

import controller.ClinicaController;
import entity.*;
import service.ClinicaException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelOdontologos extends JPanel {
    private final ClinicaController controller;
    private JTextField txtNombre, txtApellido, txtDni, txtMatricula, txtRecargo;
    private JComboBox<String> cmbEspecialidad;
    private JTable tablaOdontologos;
    private DefaultTableModel modeloTabla;
    private Long idOdontologoSeleccionado = null;

    public PanelOdontologos(ClinicaController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        inicializarComponentes();
        cargarTabla();
    }

    private void inicializarComponentes() {
        JPanel pnlForm = new JPanel(new GridLayout(7, 2, 5, 5));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Registrar / Editar Odontólogo"));

        pnlForm.add(new JLabel("Nombre:")); txtNombre = new JTextField(); pnlForm.add(txtNombre);
        pnlForm.add(new JLabel("Apellido:")); txtApellido = new JTextField(); pnlForm.add(txtApellido);
        pnlForm.add(new JLabel("DNI:")); txtDni = new JTextField(); pnlForm.add(txtDni);
        pnlForm.add(new JLabel("Matrícula:")); txtMatricula = new JTextField(); pnlForm.add(txtMatricula);

        pnlForm.add(new JLabel("Especialidad:"));
        cmbEspecialidad = new JComboBox<>(new String[]{"Ortodoncia", "Endodoncia", "Extracción"});
        pnlForm.add(cmbEspecialidad);

        pnlForm.add(new JLabel("Recargo Especialidad:")); txtRecargo = new JTextField(); pnlForm.add(txtRecargo);

        JButton btnGuardar = new JButton("Guardar Odontólogo");
        btnGuardar.addActionListener(e -> guardarOdontologo());

        JButton btnModificar = new JButton("Modificar Odontólog");
        btnModificar.addActionListener(e -> modificarOdontologo());

        JButton btnLimpiar = new JButton("Limpiar Formulario");
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        JButton btnEliminar = new JButton("Eliminar Odontólogo");
        btnEliminar.addActionListener(e -> eliminarOdontologo());

        JPanel pnlBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        pnlBotones.add(btnGuardar); pnlBotones.add(btnLimpiar);
        pnlBotones.add(btnModificar); pnlBotones.add(btnEliminar);

        JPanel pnlIzquierdo = new JPanel(new BorderLayout());
        pnlIzquierdo.add(pnlForm, BorderLayout.CENTER);
        pnlIzquierdo.add(pnlBotones, BorderLayout.SOUTH);
        add(pnlIzquierdo, BorderLayout.WEST);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Apellido", "Matrícula"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaOdontologos = new JTable(modeloTabla);

        tablaOdontologos.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaOdontologos.getSelectedRow();
            if (fila != -1 && !e.getValueIsAdjusting()) {
                Long id = (Long) tablaOdontologos.getValueAt(fila, 0);
                controller.listarOdontologos().stream()
                        .filter(o -> o.getId().equals(id))
                        .findFirst().ifPresent(this::cargarOdontologoEnFormulario);
            }
        });

        add(new JScrollPane(tablaOdontologos), BorderLayout.CENTER);
    }

    private void cargarOdontologoEnFormulario(Odontologo o) {
        idOdontologoSeleccionado = o.getId();
        txtNombre.setText(o.getNombre());
        txtApellido.setText(o.getApellido());
        txtDni.setText(o.getDni());
        txtMatricula.setText(o.getMatricula());

        if (o instanceof OdOrtodoncia) {
            cmbEspecialidad.setSelectedIndex(0);
            txtRecargo.setText(String.valueOf(((OdOrtodoncia) o).getRecargoEspecialidad()));
        } else if (o instanceof OdEndodoncia) {
            cmbEspecialidad.setSelectedIndex(1);
            txtRecargo.setText(String.valueOf(((OdEndodoncia) o).getRecargoEspecialidad()));
        } else if (o instanceof OdExtraccion) {
            cmbEspecialidad.setSelectedIndex(2);
            txtRecargo.setText(String.valueOf(((OdExtraccion) o).getRecargoEspecialidad()));
        }
    }

    private void guardarOdontologo() {

        if (idOdontologoSeleccionado != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Está editando un odontólogo. Use el botón Modificar."
            );
            return;
        }

        try {
            Double recargo = Double.parseDouble(txtRecargo.getText());
            Odontologo o = switch (cmbEspecialidad.getSelectedIndex()) {
                case 0 -> { OdOrtodoncia od = new OdOrtodoncia(); od.setRecargoEspecialidad(recargo); yield od; }
                case 1 -> { OdEndodoncia od = new OdEndodoncia(); od.setRecargoEspecialidad(recargo); yield od; }
                case 2 -> { OdExtraccion od = new OdExtraccion(); od.setRecargoEspecialidad(recargo); yield od; }
                default -> null;
            };

            if (o != null) {

                o.setNombre(txtNombre.getText());
                o.setApellido(txtApellido.getText());
                o.setDni(txtDni.getText());
                o.setMatricula(txtMatricula.getText());

                controller.registrarOdontologo(o);
                JOptionPane.showMessageDialog(this, "Odontólogo guardado con éxito.");
                limpiarFormulario();
                cargarTabla();
            }
        } catch (ClinicaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Negocio", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Datos numéricos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Odontologo o : controller.listarOdontologos()) {
            modeloTabla.addRow(new Object[]{o.getId(), o.getNombre(), o.getApellido(), o.getMatricula()});
        }
    }

    private void limpiarFormulario() {
        idOdontologoSeleccionado = null;
        txtNombre.setText(""); txtApellido.setText(""); txtDni.setText(""); txtMatricula.setText(""); txtRecargo.setText("");
        tablaOdontologos.clearSelection();
    }

    private void modificarOdontologo() {
        if (idOdontologoSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un odontólogo de la tabla.",
                    "Atención",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            Double recargo = Double.parseDouble(txtRecargo.getText());

            Odontologo o = switch (cmbEspecialidad.getSelectedIndex()) {
                case 0 -> {
                    OdOrtodoncia od = new OdOrtodoncia();
                    od.setRecargoEspecialidad(recargo);
                    yield od;
                }
                case 1 -> {
                    OdEndodoncia od = new OdEndodoncia();
                    od.setRecargoEspecialidad(recargo);
                    yield od;
                }
                case 2 -> {
                    OdExtraccion od = new OdExtraccion();
                    od.setRecargoEspecialidad(recargo);
                    yield od;
                }
                default -> null;
            };

            if (o != null) {
                o.setId(idOdontologoSeleccionado);
                o.setNombre(txtNombre.getText());
                o.setApellido(txtApellido.getText());
                o.setDni(txtDni.getText());
                o.setMatricula(txtMatricula.getText());

                controller.actualizarOdontologo(o);

                JOptionPane.showMessageDialog(
                        this,
                        "Odontólogo actualizado correctamente."
                );

                limpiarFormulario();
                cargarTabla();
            }

        } catch (ClinicaException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error de Negocio",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Datos numéricos inválidos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarOdontologo() {
        if (idOdontologoSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un odontólogo de la tabla.",
                    "Atención",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar el odontólogo seleccionado?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            controller.eliminarOdontologoPorId(idOdontologoSeleccionado);

            JOptionPane.showMessageDialog(
                    this,
                    "Odontólogo eliminado correctamente."
            );

            limpiarFormulario();
            cargarTabla();
        }
    }
}