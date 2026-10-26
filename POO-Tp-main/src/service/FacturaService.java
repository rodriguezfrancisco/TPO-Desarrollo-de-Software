package service;

import entity.Factura;
import entity.Odontologo;
import entity.Paciente;
import entity.PacienteObraSocial;
import entity.PacienteParticular;
import entity.Turno;
import entity.decorator.CoberturaObraSocialDecorator;
import entity.decorator.ConsultaBase;
import entity.decorator.Prestacion;
import entity.decorator.RecargoEspecialidadDecorator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import repository.FacturaRepository;

public class FacturaService extends BaseService<Factura> implements IFacturaService {

    public FacturaService() {
        super(new FacturaRepository());
    }

    @Override
    protected String getNombreEntidad() { 
        return "factura"; 
    }

    @Override
    protected String getCodigoErrorGenerico() { 
        return "ERR_FAC_000"; 
    }

    @Override
    protected void validarReglasNegocioRegistro(Factura factura) {
        Validador.validarObjeto(factura.getTurno(), "Turno", "El turno es obligatorio para facturar.");
        Validador.validarTextoObligatorio(factura.getRutaArchivo(), "Ruta Archivo", "La ruta física del archivo no puede estar vacía.");
    }

    @Override
    protected void validarReglasNegocioActualizacion(Factura factura) {
        Validador.validarId(factura.getId(), "ID Factura");
        validarReglasNegocioRegistro(factura);
    }

    /**
     * Construye y compone dinámicamente la prestación utilizando el patrón Decorator.
     */
    public Prestacion armarPrestacion(Turno turno) {
        Paciente paciente = turno.getPaciente();
        Odontologo odontologo = turno.getOdontologo();

        // 1. Componente Concreto (Tarifa Base de la consulta)
        double base = 0.0;
        if (paciente instanceof PacienteParticular particular) {
            base = particular.getTarifaBase() != null ? particular.getTarifaBase() : 0.0;
        } else {
            base = 1500.0; // Valor de referencia base para consultas bajo convenio
        }
        Prestacion prestacion = new ConsultaBase(base);

        // 2. Decorador Concreto: Recargo por especialidad odontológica
        double recargo = odontologo.getRecargo() != null ? odontologo.getRecargo() : 0.0;
        String especialidad = odontologo.getEspecialidad() != null ? odontologo.getEspecialidad().name() : "GENERAL";
        if (recargo > 0) {
            prestacion = new RecargoEspecialidadDecorator(prestacion, recargo, especialidad);
        }

        // 3. Decorador Concreto: Bonificación/Cobertura de Obra Social (100% de cobertura)
        if (paciente instanceof PacienteObraSocial obraSocial) {
            prestacion = new CoberturaObraSocialDecorator(prestacion, obraSocial.getNombreObraSocial(), 1.0);
        }

        return prestacion;
    }

    /**
     * Orquesta el cálculo del costo mediante Decorator, genera el comprobante .txt
     * y persiste la Factura en el repositorio.
     */
    public Factura emitirYRegistrarFactura(Turno turno) {
        if (turno == null || turno.getPaciente() == null || turno.getOdontologo() == null) {
            throw new DatoInvalidoException("Facturación", "Faltan datos en el turno para facturar.");
        }

        // Aplicación del Decorator
        Prestacion prestacion = armarPrestacion(turno);
        double totalAPagar = prestacion.calcularCosto();
        turno.setMontoFacturacion(totalAPagar);

        // Generar archivo físico
        String contenido = construirContenidoTexto(turno, prestacion);
        String rutaGenerada = guardarFacturaEnDisco(turno.getId(), contenido);

        // Crear la entidad Factura y persistirla
        Factura nuevaFactura = new Factura(turno, LocalDateTime.now(), totalAPagar, rutaGenerada);
        return this.registrar(nuevaFactura);
    }

    private String construirContenidoTexto(Turno turno, Prestacion prestacion) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaEmision = LocalDateTime.now().format(dtf);
        Paciente paciente = turno.getPaciente();
        Odontologo odontologo = turno.getOdontologo();
        String especialidad = odontologo.getEspecialidad() != null ? odontologo.getEspecialidad().name() : "General";

        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append("        CLÍNICA ODONTOLÓGICA 'SONRISA FELIZ'        \n");
        sb.append("               FACTURA / COMPROBANTE                \n");
        sb.append("====================================================\n");
        sb.append("N° Factura / Turno: ").append(turno.getId()).append("\n");
        sb.append("Fecha de Emisión:   ").append(fechaEmision).append("\n");
        sb.append("Fecha del Turno:    ").append(turno.getFecha()).append(" ").append(turno.getHora()).append("\n");
        sb.append("----------------------------------------------------\n");
        sb.append("DATOS DEL PACIENTE:\n");
        sb.append(" Nombre:    ").append(paciente.getContacto().getNombre()).append(" ").append(paciente.getContacto().getApellido()).append("\n");
        sb.append(" DNI:       ").append(paciente.getContacto().getDni()).append("\n");
        sb.append(" Teléfono:  ").append(paciente.getContacto().getTelefono() != null ? paciente.getContacto().getTelefono() : "N/D").append("\n");
        sb.append(" Cobertura: ").append(paciente.getDetalleCobertura()).append("\n");
        sb.append("----------------------------------------------------\n");
        sb.append("DATOS DEL PROFESIONAL:\n");
        sb.append(" Odontólogo:   Dr/a. ").append(odontologo.getNombre()).append(" ").append(odontologo.getApellido()).append("\n");
        sb.append(" Matrícula:    ").append(odontologo.getMatricula()).append("\n");
        sb.append(" Especialidad: ").append(especialidad).append("\n");
        sb.append("----------------------------------------------------\n");
        sb.append("DETALLE DE LIQUIDACIÓN (PATRÓN DECORATOR):\n");
        
        // El detalle se genera recursivamente a través de la cadena de decoradores
        sb.append(prestacion.generarDetalle());
        
        sb.append("----------------------------------------------------\n");
        sb.append(String.format(" TOTAL FINAL A ABONAR:          $%10.2f\n", prestacion.calcularCosto()));
        sb.append("====================================================\n");
        sb.append("          Gracias por confiar en nosotros!          \n");
        sb.append("====================================================\n");
        return sb.toString();
    }

    private String guardarFacturaEnDisco(Long turnoId, String contenidoFactura) {
        File carpetaFacturas = new File("facturas");
        if (!carpetaFacturas.exists()) {
            carpetaFacturas.mkdirs();
        }
        String nombreArchivo = "facturas" + File.separator + "Factura_Turno_" + turnoId + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write(contenidoFactura);
        } catch (IOException e) {
            throw new ClinicaException("Error al generar el archivo de factura: " + e.getMessage(), "ERR_FAC_001");
        }
        return nombreArchivo;
    }

    public void anularFactura(Long idFactura) {
        Factura factura = buscarPorId(idFactura)
                .orElseThrow(() -> new ClinicaException("Factura no encontrada.", "ERR_FAC_001"));
        if (factura.isAnulada()) {
            throw new ClinicaException("Esta factura ya se encuentra anulada.", "ERR_FAC_002");
        }
        factura.setAnulada(true);
        this.actualizar(factura);
    }
}

