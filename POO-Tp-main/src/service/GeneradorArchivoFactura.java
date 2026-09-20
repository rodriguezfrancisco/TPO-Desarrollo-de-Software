package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GeneradorArchivoFactura {
    
    // Método estático que se encarga exclusivamente de interactuar con el disco duro
    public static String guardarEnDisco(Long turnoId, String contenido) {
        File carpetaFacturas = new File("facturas");
        if (!carpetaFacturas.exists()) {
            carpetaFacturas.mkdirs();
        }

        String nombreArchivo = "facturas" + File.separator + "Factura_Turno_" + turnoId + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write(contenido);
        } catch (IOException e) {
            throw new ClinicaException("Error de sistema al guardar el archivo: " + e.getMessage(), "ERR_FAC_003");
        }

        return nombreArchivo;
    }
}