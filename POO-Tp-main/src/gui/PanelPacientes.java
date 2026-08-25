package gui;

import controller.ClinicaController;
import entity.*;
import service.ClinicaException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class PanelPacientes extends JPanel {
    private final ClinicaController controller;
    private JTextField txtNombre, txtApellido, txtDni, txtEmail, txtTelefono;
    private JTextField txtCalle, txtAltura, txtLocalidad, txtProvincia;
    private JComboBox<String> cmbCobertura;
    private JTextField txtDatoCobertura;
    private JLabel lblDatoCobertura;
    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;
    private Long idPacienteSeleccionado = null; // Almacena el ID en caso de edición

    public PanelPacientes(ClinicaController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        inicializarComponentes();
        cargarTabla();
    }

    private void inicializarComponentes() {
        JPanel pnlForm = new JPanel(new GridLayout(12, 2, 5, 5));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Registrar / Editar Paciente"));

        pnlForm.add(new JLabel("Nombre:")); txtNombre = new JTextField(); pnlForm.add(txtNombre);
        pnlForm.add(new JLabel("Apellido:")); txtApellido = new JTextField(); pnlForm.add(txtApellido);
        pnlForm.add(new JLabel("DNI:")); txtDni = new JTextField(); pnlForm.add(txtDni);
        pnlForm.add(new JLabel("Email:")); txtEmail = new JTextField(); pnlForm.add(txtEmail);
        pnlForm.add(new JLabel("Teléfono:")); txtTelefono = new JTextField(); pnlForm.add(txtTelefono);
        pnlForm.add(new JLabel("Calle:")); txtCalle = new JTextField(); pnlForm.add(txtCalle);
        pnlForm.add(new JLabel("Altura:")); txtAltura = new JTextField(); pnlForm.add(txtAltura);
        pnlForm.add(new JLabel("Localidad:")); txtLocalidad = new JTextField(); pnlForm.add(txtLocalidad);
        pnlForm.add(new JLabel("Provincia:")); txtProvincia = new JTextField(); pnlForm.add(txtProvincia);

        pnlForm.add(new JLabel("Cobertura:"));
        cmbCobertura = new JComboBox<>(new String[]{"Particular", "Obra Social"});
        pnlForm.add(cmbCobertura);

        lblDatoCobertura = new JLabel("Tarifa Base:"); pnlForm.add(lblDatoCobertura);
        txtDatoCobertura = new JTextField(); pnlForm.add(txtDatoCobertura);

        cmbCobertura.addActionListener(e -> {
            if (cmbCobertura.getSelectedIndex() == 0) {
                lblDatoCobertura.setText("Tarifa Base:");
            } else {
                lblDatoCobertura.setText("Obra Social (Nombre):");
            }
        });

        JButton btnGuardar = new JButton("Guardar Paciente");
        btnGuardar.addActionListener(e -> guardarPaciente());

        JButton btnModificar = new JButton("Modificar Paciente");
        btnModificar.addActionListener(e -> actualizarPaciente());

        JButton btnEliminar = new JButton("Eliminar Paciente");
        btnEliminar.addActionListener(e -> {
            if (idPacienteSeleccionado == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un paciente de la tabla."
                );
                return;
            }

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea eliminar el paciente seleccionado?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                controller.eliminarPacientePorId(idPacienteSeleccionado);
                cargarTabla();
                limpiarFormulario();

                JOptionPane.showMessageDialog(
                        this,
                        "Paciente eliminado correctamente.");
            }
        });

        JButton btnLimpiar = new JButton("Limpiar Formulario");
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        JPanel pnlBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        pnlBotones.add(btnGuardar); pnlBotones.add(btnLimpiar);
        pnlBotones.add(btnModificar); pnlBotones.add(btnEliminar);

        JPanel pnlIzquierdo = new JPanel(new BorderLayout());
        pnlIzquierdo.add(pnlForm, BorderLayout.CENTER);
        pnlIzquierdo.add(pnlBotones, BorderLayout.SOUTH);
        add(pnlIzquierdo, BorderLayout.WEST);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Apellido", "DNI"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaPacientes = new JTable(modeloTabla);

        // Listener de fila seleccionada para la edición
        tablaPacientes.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaPacientes.getSelectedRow();
            if (fila != -1 && !e.getValueIsAdjusting()) {
                Long id = (Long) tablaPacientes.getValueAt(fila, 0);
                controller.buscarPacienteId(id).ifPresent(this::cargarPacienteEnFormulario);
            }
        });

        add(new JScrollPane(tablaPacientes), BorderLayout.CENTER);
    }

    private void cargarPacienteEnFormulario(Paciente p) {
        idPacienteSeleccionado = p.getId();
        txtNombre.setText(p.getNombre());
        txtApellido.setText(p.getApellido());
        txtDni.setText(p.getDni());
        txtEmail.setText(p.getEmail());
        txtTelefono.setText(p.getTelefono());

        if (p.getDomicilio() != null) {
            txtCalle.setText(p.getDomicilio().getCalle());
            txtAltura.setText(p.getDomicilio().getAltura());
            txtLocalidad.setText(p.getDomicilio().getLocalidad());
            txtProvincia.setText(p.getDomicilio().getProvincia());
        }

        if (p instanceof PacienteParticular pp) {
            cmbCobertura.setSelectedIndex(0);
            txtDatoCobertura.setText(String.valueOf(pp.getTarifaBase()));
        } else if (p instanceof PacienteObraSocial pos) {
            cmbCobertura.setSelectedIndex(1);
            txtDatoCobertura.setText(pos.getNombreObraSocial());
        }
    }

    private void guardarPaciente() {
        try {
            Domicilio dom = new Domicilio(txtCalle.getText(), txtAltura.getText(), txtLocalidad.getText(), txtProvincia.getText());
            Paciente p;

            if (cmbCobertura.getSelectedIndex() == 0) {
                PacienteParticular particular = new PacienteParticular();
                particular.setTarifaBase(Double.parseDouble(txtDatoCobertura.getText()));
                p = particular;
            } else {
                PacienteObraSocial os = new PacienteObraSocial();
                os.setNombreObraSocial(txtDatoCobertura.getText());
                os.setNumAfiliado("AF-" + txtDni.getText());
                p = os;
            }

            p.setNombre(txtNombre.getText());
            p.setApellido(txtApellido.getText());
            p.setDni(txtDni.getText());
            p.setEmail(txtEmail.getText());
            p.setTelefono(txtTelefono.getText());
            p.setFechaIngreso(LocalDate.now());
            p.setDomicilio(dom);

            controller.registrarPaciente(p);
            JOptionPane.showMessageDialog(this, "Paciente guardado con éxito.");
            limpiarFormulario();
            cargarTabla();
        } catch (ClinicaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Negocio", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Verifique los datos ingresados: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarPaciente() {
        if (idPacienteSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un paciente de la tabla.",
                    "Atención",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            Domicilio dom = new Domicilio(
                    txtCalle.getText(),
                    txtAltura.getText(),
                    txtLocalidad.getText(),
                    txtProvincia.getText()
            );

            Paciente original = controller.buscarPacienteId(idPacienteSeleccionado).orElseThrow();

            Paciente p;


            if (cmbCobertura.getSelectedIndex() == 0) {
                PacienteParticular particular = new PacienteParticular();
                particular.setTarifaBase(
                        Double.parseDouble(txtDatoCobertura.getText())
                );
                p = particular;
            } else {
                PacienteObraSocial os = new PacienteObraSocial();
                os.setNombreObraSocial(txtDatoCobertura.getText());
                os.setNumAfiliado("AF-" + txtDni.getText());
                p = os;
            }

            p.setId(idPacienteSeleccionado);
            p.setNombre(txtNombre.getText());
            p.setApellido(txtApellido.getText());
            p.setDni(txtDni.getText());
            p.setEmail(txtEmail.getText());
            p.setTelefono(txtTelefono.getText());
            p.setDomicilio(dom);
            p.setFechaIngreso(original.getFechaIngreso());

            controller.actualizarPaciente(p);

            JOptionPane.showMessageDialog(
                    this,
                    "Paciente actualizado correctamente."
            );

            limpiarFormulario();
            cargarTabla();

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
                    "Verifique los datos ingresados: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Paciente p : controller.listarPacientes()) {
            modeloTabla.addRow(new Object[]{p.getId(), p.getNombre(), p.getApellido(), p.getDni()});
        }
    }

    private void limpiarFormulario() {
        idPacienteSeleccionado = null;
        txtNombre.setText(""); txtApellido.setText(""); txtDni.setText("");
        txtEmail.setText(""); txtTelefono.setText(""); txtCalle.setText("");
        txtAltura.setText(""); txtLocalidad.setText(""); txtProvincia.setText("");
        txtDatoCobertura.setText("");
        cmbCobertura.setSelectedIndex(0);
        tablaPacientes.clearSelection();
    }
}