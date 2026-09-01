package service;

import entity.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FacturaService {

    // 1. MÉTODO ORQUESTADOR (El único público)
    public String generarFactura(Turno turno) {
        validarTurno(turno);

        LiquidacionDTO liquidacion = calcularLiquidacion(turno);
        turno.setMontoFacturacion(liquidacion.totalAPagar());

        String contenidoFactura = generarTextoComprobante(turno, liquidacion);
        
        return guardarFacturaEnDisco(contenidoFactura, turno.getId());
    }

    // 2. VALIDACIÓN
    private void validarTurno(Turno turno) {
        if (turno == null) {
            throw new DatoInvalidoException("Facturación", "El turno no puede ser nulo.");
        }
        if (turno.getPaciente() == null || turno.getOdontologo() == null) {
            throw new DatoInvalidoException("Facturación", "El turno debe contar con Paciente y Odontólogo asignados.");
        }
    }

    // 3. LÓGICA DE NEGOCIO (Cálculos matemáticos y reglas)
    private LiquidacionDTO calcularLiquidacion(Turno turno) {
        Paciente paciente = turno.getPaciente();
        Odontologo odontologo = turno.getOdontologo();

        // Determinar Especialidad y Recargo
        String nombreEspecialidad = "General";
        double recargo = 0.0;

        if (odontologo.getEspecialidad() != null) {
            String especialidadEnum = odontologo.getEspecialidad().name();
            nombreEspecialidad = especialidadEnum.charAt(0) + especialidadEnum.substring(1).toLowerCase();
        }
        if (odontologo.getRecargo() != null) {
            recargo = odontologo.getRecargo();
        }

        // Determinar Tarifa Base y Cobertura
        double tarifaBase = 0.0;
        String detalleCobertura = "";
        double totalAPagar = 0.0;
        boolean esParticular = true;

        if (paciente instanceof PacienteParticular particular) {
            tarifaBase = particular.getTarifaBase() != null ? particular.getTarifaBase() : 0.0;
            totalAPagar = tarifaBase + recargo;
            detalleCobertura = "Particular (Sin cobertura de obra social)";
        } else if (paciente instanceof PacienteObraSocial os) {
            esParticular = false;
            tarifaBase = 0.0;
            totalAPagar = 0.0; // Cobertura al 100%
            detalleCobertura = "Obra Social: " + os.getNombreObraSocial() + " (Afiliado Nº: " + os.getNumAfiliado() + ")";
        }

        return new LiquidacionDTO(nombreEspecialidad, recargo, tarifaBase, detalleCobertura, totalAPagar, esParticular);
    }

    // 4. PRESENTACIÓN (Armado del String)
    private String generarTextoComprobante(Turno turno, LiquidacionDTO liquidacion) {
        Paciente paciente = turno.getPaciente();
        Odontologo odontologo = turno.getOdontologo();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaEmision = LocalDateTime.now().format(dtf);

        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append("        CLÍNICA ODONTOLÓGICA 'SONRISA FELIZ'        \n");
        sb.append("               FACTURA / COMPROBANTE                \n");
        sb.append("====================================================\n");
        sb.append("Nº Factura / Turno: ").append(turno.getId()).append("\n");
        sb.append("Fecha de Emisión:   ").append(fechaEmision).append("\n");
        sb.append("Fecha del Turno:    ").append(turno.getFecha()).append(" ").append(turno.getHora()).append("\n");
        sb.append("----------------------------------------------------\n");
        sb.append("DATOS DEL PACIENTE:\n");
        sb.append(" Nombre:    ").append(paciente.getNombre()).append(" ").append(paciente.getApellido()).append("\n");
        sb.append(" DNI:       ").append(paciente.getDni()).append("\n");
        sb.append(" Teléfono:  ").append(paciente.getTelefono() != null ? paciente.getTelefono() : "N/D").append("\n");
        sb.append(" Cobertura: ").append(liquidacion.detalleCobertura()).append("\n");
        sb.append("----------------------------------------------------\n");
        sb.append("DATOS DEL PROFESIONAL:\n");
        sb.append(" Odontólogo:   Dr/a. ").append(odontologo.getNombre()).append(" ").append(odontologo.getApellido()).append("\n");
        sb.append(" Matrícula:    ").append(odontologo.getMatricula()).append("\n");
        sb.append(" Especialidad: ").append(liquidacion.nombreEspecialidad()).append("\n");
        sb.append("----------------------------------------------------\n");
        sb.append("DETALLE DE LIQUIDACIÓN:\n");
        
        if (liquidacion.esParticular()) {
            sb.append(String.format(" Tarifa Base Consulta:          $%10.2f\n", liquidacion.tarifaBase()));
            sb.append(String.format(" Recargo por %-16s:  $%10.2f\n", liquidacion.nombreEspecialidad(), liquidacion.recargo()));
            sb.append("----------------------------------------------------\n");
            sb.append(String.format(" TOTAL A ABONAR EN CAJA:        $%10.2f\n", liquidacion.totalAPagar()));
        } else {
            sb.append(" Consulta y Procedimiento 100% Cubiertos por Obra Social.\n");
            sb.append(String.format(" Recargo Especialidad asumido:  $%10.2f\n", liquidacion.recargo()));
            sb.append("----------------------------------------------------\n");
            sb.append(" TOTAL A ABONAR:                $      0.00\n");
        }
        sb.append("====================================================\n");
        sb.append("         ¡Gracias por confiar en nosotros!          \n");
        sb.append("====================================================\n");

        return sb.toString();
    }

    // 5. INFRAESTRUCTURA (I/O)
    private String guardarFacturaEnDisco(String contenidoFactura, Long turnoId) {
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

    // --- CLASE AUXILIAR DE TRANSFERENCIA DE DATOS (DTO) ---
    private record LiquidacionDTO(
            String nombreEspecialidad,
            double recargo,
            double tarifaBase,
            String detalleCobertura,
            double totalAPagar,
            boolean esParticular
    ) {}
}