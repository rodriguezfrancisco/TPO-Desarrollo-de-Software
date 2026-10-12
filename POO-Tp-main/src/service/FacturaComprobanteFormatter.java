package service;

import entity.Odontologo;
import entity.Paciente;
import entity.Turno;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FacturaComprobanteFormatter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public String generarTexto(Turno turno, Paciente paciente, Odontologo odontologo, double recargo, String especialidad) {
        String fechaEmision = LocalDateTime.now().format(FORMATTER);
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
        sb.append("DETALLE DE LIQUIDACIÓN:\n");
        sb.append(paciente.generarDetalleLiquidacion(recargo, especialidad));
        sb.append("====================================================\n");
        sb.append("          ¡Gracias por confiar en nosotros!         \n");
        sb.append("====================================================\n");
        return sb.toString();
    }
}