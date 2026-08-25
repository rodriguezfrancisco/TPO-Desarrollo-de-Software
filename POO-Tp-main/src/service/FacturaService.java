package service;

import entity.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FacturaService {

    public String generarFactura(Turno turno) {
        if (turno == null) {
            throw new DatoInvalidoException("Facturación", "El turno no puede ser nulo.");
        }

        Paciente paciente = turno.getPaciente();
        Odontologo odontologo = turno.getOdontologo();

        if (paciente == null || odontologo == null) {
            throw new DatoInvalidoException("Facturación", "El turno debe contar con Paciente y Odontólogo asignados.");
        }

        // 1. Determinar Especialidad y Recargo (Directo desde la entidad)
        String nombreEspecialidad = "General";
        double recargoEspecialidad = 0.0;

        if (odontologo.getEspecialidad() != null) {
            // Capitalizamos el nombre del Enum para que se vea bien en el texto (ej: ORTODONCIA -> Ortodoncia)
            String especialidadEnum = odontologo.getEspecialidad().name();
            nombreEspecialidad = especialidadEnum.charAt(0) + especialidadEnum.substring(1).toLowerCase();
        }
        
        if (odontologo.getRecargoEspecialidad() != null) {
            recargoEspecialidad = odontologo.getRecargoEspecialidad();
        }

        // 2. Determinar Tarifa Base y Cobertura
        double tarifaBase = 0.0;
        String detalleCobertura = "";
        double totalAPagar = 0.0;

        if (paciente instanceof PacienteParticular particular) {
            tarifaBase = particular.getTarifaBase() != null ? particular.getTarifaBase() : 0.0;
            totalAPagar = tarifaBase + recargoEspecialidad;
            detalleCobertura = "Particular (Sin cobertura de obra social)";
        } else if (paciente instanceof PacienteObraSocial os) {
            tarifaBase = 0.0;
            totalAPagar = 0.0; // Cobertura al 100%
            detalleCobertura = "Obra Social: " + os.getNombreObraSocial() + " (Afiliado Nº: " + os.getNumAfiliado() + ")";
        }

        // Actualizar el monto en el turno si no estaba seteado
        turno.setMontoFacturacion(totalAPagar);

        // 3. Crear carpeta de salida si no existe
        File carpetaFacturas = new File("facturas");
        if (!carpetaFacturas.exists()) {
            carpetaFacturas.mkdirs();
        }

        String nombreArchivo = "facturas" + File.separator + "Factura_Turno_" + turno.getId() + ".txt";

        // 4. Formatear y Escribir Archivo
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
        sb.append(" Cobertura: ").append(detalleCobertura).append("\n");
        sb.append("----------------------------------------------------\n");
        sb.append("DATOS DEL PROFESIONAL:\n");
        sb.append(" Odontólogo:   Dr/a. ").append(odontologo.getNombre()).append(" ").append(odontologo.getApellido()).append("\n");
        sb.append(" Matrícula:    ").append(odontologo.getMatricula()).append("\n");
        sb.append(" Especialidad: ").append(nombreEspecialidad).append("\n");
        sb.append("----------------------------------------------------\n");
        sb.append("DETALLE DE LIQUIDACIÓN:\n");
        if (paciente instanceof PacienteParticular) {
            sb.append(String.format(" Tarifa Base Consulta:          $%10.2f\n", tarifaBase));
            sb.append(String.format(" Recargo por %-16s:  $%10.2f\n", nombreEspecialidad, recargoEspecialidad));
            sb.append("----------------------------------------------------\n");
            sb.append(String.format(" TOTAL A ABONAR EN CAJA:        $%10.2f\n", totalAPagar));
        } else {
            sb.append(" Consulta y Procedimiento 100% Cubiertos por Obra Social.\n");
            sb.append(String.format(" Recargo Especialidad asumido:  $%10.2f\n", recargoEspecialidad));
            sb.append("----------------------------------------------------\n");
            sb.append(" TOTAL A ABONAR:                $      0.00\n");
        }
        sb.append("====================================================\n");
        sb.append("         ¡Gracias por confiar en nosotros!          \n");
        sb.append("====================================================\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new ClinicaException("Error al generar el archivo de factura: " + e.getMessage(), "ERR_FAC_001");
        }

        return nombreArchivo;
    }
}