package service;

import entity.Factura;
import entity.Odontologo;
import entity.Paciente;
import entity.Turno;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import repository.FacturaRepository;

public class FacturaService extends BaseService<Factura> {

    public FacturaService() {
        super(new FacturaRepository());
    }

    @Override
    protected String getNombreEntidad() { return "factura"; }

    @Override
    protected String getCodigoErrorGenerico() { return "ERR_FAC_000"; }

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
     * Orquesta la creación del .txt y el registro en Base de Datos
     */
    public Factura emitirYRegistrarFactura(Turno turno) {
        if (turno == null || turno.getPaciente() == null || turno.getOdontologo() == null) {
            throw new DatoInvalidoException("Facturación", "Faltan datos en el turno para facturar.");
        }

        Paciente paciente = turno.getPaciente();
        Odontologo odontologo = turno.getOdontologo();

        String nombreEspecialidad = odontologo.getEspecialidad() != null ? 
            odontologo.getEspecialidad().name() : "General";
        double recargoEspecialidad = odontologo.getRecargo() != null ? 
            odontologo.getRecargo() : 0.0;

        double totalAPagar = paciente.calcularCostoConsulta(recargoEspecialidad);
        turno.setMontoFacturacion(totalAPagar);

        // 1. Armar el texto del comprobante
        String contenido = construirContenidoTexto(turno, paciente, odontologo, recargoEspecialidad, nombreEspecialidad);
        
        // Delegamos la creación del archivo físico a nuestra clase de Fabricación Pura
        String rutaGenerada = GeneradorArchivoFactura.guardarEnDisco(turno.getId(), contenido);

        // 2. Crear la entidad y registrarla en el CRUD (Base de Datos)
        Factura nuevaFactura = new Factura(turno, LocalDateTime.now(), totalAPagar, rutaGenerada);
        
        return this.registrar(nuevaFactura); 
    }

    private String construirContenidoTexto(Turno turno, Paciente paciente, Odontologo odontologo, double recargo, String especialidad) {
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
        sb.append(" Nombre:    ").append(paciente.getContacto().getNombre()).append(" ").append(paciente.getContacto().getApellido()).append("\n");
        sb.append(" DNI:       ").append(paciente.getContacto().getDni()).append("\n");
        sb.append(" Teléfono:  ").append(paciente.getContacto().getTelefono() != null ? paciente.getContacto().getTelefono() : "N/D").append("\n");
        // Polimorfismo: el paciente sabe cómo mostrar su cobertura
        sb.append(" Cobertura: ").append(paciente.getDetalleCobertura()).append("\n"); 
        sb.append("----------------------------------------------------\n");
        sb.append("DATOS DEL PROFESIONAL:\n");
        sb.append(" Odontólogo:   Dr/a. ").append(odontologo.getNombre()).append(" ").append(odontologo.getApellido()).append("\n");
        sb.append(" Matrícula:    ").append(odontologo.getMatricula()).append("\n");
        sb.append(" Especialidad: ").append(especialidad).append("\n");
        sb.append("----------------------------------------------------\n");
        sb.append("DETALLE DE LIQUIDACIÓN:\n");
        
        // Polimorfismo puro: reemplaza todo el if/else de la versión anterior
        sb.append(paciente.generarDetalleLiquidacion(recargo, especialidad));
        
        sb.append("====================================================\n");
        sb.append("         ¡Gracias por confiar en nosotros!          \n");
        sb.append("====================================================\n");

        return sb.toString();
    }

    public void anularFactura(Long idFactura) {
        // Usamos la excepción personalizada que ya tenés en tu arquitectura
        Factura factura = buscarPorId(idFactura)
            .orElseThrow(() -> new ClinicaException("Factura no encontrada.", "ERR_FAC_001"));

        if (factura.isAnulada()) {
            throw new ClinicaException("Esta factura ya se encuentra anulada.", "ERR_FAC_002");
        }

        factura.setAnulada(true);
        this.actualizar(factura); // Reutilizamos el método de BaseService
    }
}