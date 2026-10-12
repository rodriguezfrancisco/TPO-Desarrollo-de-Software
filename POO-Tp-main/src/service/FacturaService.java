package service;

import entity.Factura;
import entity.Odontologo;
import entity.Paciente;
import entity.Turno;
import repository.FacturaFileStorage;
import repository.FacturaRepository;
import repository.IFacturaStorage;
import repository.IRepository;
import java.time.LocalDateTime;

public class FacturaService extends BaseService<Factura> implements IFacturaService {
    private final IFacturaStorage facturaStorage;
    private final FacturaComprobanteFormatter formatter;


    public FacturaService(IRepository<Factura> repository, IFacturaStorage facturaStorage, FacturaComprobanteFormatter formatter) {
        super(repository);
        this.facturaStorage = facturaStorage;
        this.formatter = formatter;
    }




    // Constructor de conveniencia manteniendo compatibilidad
    public FacturaService() {
        this(new FacturaRepository(), new FacturaFileStorage(), new FacturaComprobanteFormatter());
    }    
        

    public FacturaService(IRepository<Factura> facturaRepo) {
        this(facturaRepo, new FacturaFileStorage(), new FacturaComprobanteFormatter());
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

    public Factura emitirYRegistrarFactura(Turno turno) {
        if (turno == null || turno.getPaciente() == null || turno.getOdontologo() == null) {
            throw new DatoInvalidoException("Facturación", "Faltan datos en el turno para facturar.");
        }

        Paciente paciente = turno.getPaciente();
        Odontologo odontologo = turno.getOdontologo();
        String nombreEspecialidad = odontologo.getEspecialidad() != null ? odontologo.getEspecialidad().name() : "General";
        double recargoEspecialidad = odontologo.getRecargo() != null ? odontologo.getRecargo() : 0.0;
        double totalAPagar = paciente.calcularCostoConsulta(recargoEspecialidad);
        turno.setMontoFacturacion(totalAPagar);

        // Delegación de formato y guardado en almacenamiento externo
        String contenido = formatter.generarTexto(turno, paciente, odontologo, recargoEspecialidad, nombreEspecialidad);
        String rutaGenerada = facturaStorage.guardarComprobante(turno.getId(), contenido);

        Factura nuevaFactura = new Factura(turno, LocalDateTime.now(), totalAPagar, rutaGenerada);
        return this.registrar(nuevaFactura);
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