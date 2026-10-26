package repository;

// Simulamos una librería externa (ej. Apache PDFBox) que no conoce tu sistema
public class TercerosPdfGenerator {
    
    public void configurarMetadatos(String titulo, String autor) {
        System.out.println("Configurando metadatos del documento PDF...");
    }
    
    public String exportarDocumentoFisico(String nombreArchivoSinExtension, String textoFisico) {
        String rutaFinal = nombreArchivoSinExtension + ".pdf";
        // Aquí la librería procesaría internamente los bytes para crear un PDF real
        System.out.println("Generando archivo binario PDF en: " + rutaFinal);
        return rutaFinal;
    }
}