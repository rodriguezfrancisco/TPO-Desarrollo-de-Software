package repository;

public class FacturaPdfAdapter implements IFacturaStorage {
    
    // El adaptador envuelve (compone) a la librería de terceros
    private final TercerosPdfGenerator generadorPdfExt;
    private final String carpetaDestino;

    public FacturaPdfAdapter(String carpetaDestino) {
        this.generadorPdfExt = new TercerosPdfGenerator();
        this.carpetaDestino = carpetaDestino;
    }

    @Override
    public String guardarComprobante(Long turnoId, String contenido) {
        // 1. Adaptamos los datos de entrada al formato que pide la librería externa
        String rutaBase = carpetaDestino + "/Factura_Turno_" + turnoId;
        
        // 2. Ejecutamos las rutinas obligatorias de la librería externa
        generadorPdfExt.configurarMetadatos("Comprobante Turno " + turnoId, "Sonrisa Feliz");
        
        // 3. Traducimos la salida de la librería al String que espera FacturaService
        String rutaGenerada = generadorPdfExt.exportarDocumentoFisico(rutaBase, contenido);
        
        return rutaGenerada;
    }
}