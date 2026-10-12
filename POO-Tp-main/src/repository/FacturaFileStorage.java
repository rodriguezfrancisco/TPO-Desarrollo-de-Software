package repository;

import service.ClinicaException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FacturaFileStorage implements IFacturaStorage {
    private final String carpetaDestino;

    public FacturaFileStorage() {
        this("facturas");
    }

    public FacturaFileStorage(String carpetaDestino) {
        this.carpetaDestino = carpetaDestino;
    }

    @Override
    public String guardarComprobante(Long turnoId, String contenido) {
        File carpeta = new File(carpetaDestino);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        String nombreArchivo = carpetaDestino + File.separator + "Factura_Turno_" + turnoId + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write(contenido);
        } catch (IOException e) {
            throw new ClinicaException("Error al generar el archivo de factura: " + e.getMessage(), "ERR_FAC_001");
        }
        return nombreArchivo;
    }
}