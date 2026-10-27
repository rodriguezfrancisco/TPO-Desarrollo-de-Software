package repository;

// Simulamos una librería externa (ej. Apache PDFBox) que no conoce tu sistema
public class TercerosPdfGenerator {
    
    public void configurarMetadatos(String titulo, String autor) {
        System.out.println("Configurando metadatos del documento PDF...");
    }
    
    public String exportarDocumentoFisico(String nombreArchivoSinExtension, String textoFisico) {
    String rutaFinal = nombreArchivoSinExtension + ".pdf";
    try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(rutaFinal))) {
        writer.write("--- SIMULACIÓN DE DOCUMENTO PDF ---\n\n" + textoFisico);
    } catch (java.io.IOException e) {
        System.err.println("Error al generar el archivo: " + e.getMessage());
    }
    return rutaFinal;
}
}