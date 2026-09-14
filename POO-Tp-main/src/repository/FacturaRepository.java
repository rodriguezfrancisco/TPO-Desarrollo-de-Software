package repository;

import entity.Factura;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FacturaRepository implements IRepository<Factura> {

    private Map<Long, Factura> tablaFacturas = new HashMap<>();
    private Long generadorId = 1L;
    private String FILE_NAME; // Dinámico

    public FacturaRepository() {
        inicializarPersistencia();
        cargarDesdeArchivo();
    }

    private void inicializarPersistencia() {
        File dataFolder = encontrarCarpetaData();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        this.FILE_NAME = dataFolder.getAbsolutePath() + File.separator + "facturas.dat";
    }

    private File encontrarCarpetaData() {
        // 1. Intentar en la ruta directa de 'Codigo - Sonrisa Feliz'
        File f = new File("src" + File.separator + "data");
        if (f.exists()) return f;

        // 2. Respaldo por si abrieron desde 'POO-Tp'
        f = new File("Codigo - Sonrisa Feliz" + File.separator + "src" + File.separator + "data");
        if (f.exists()) return f;

        // 3. Fallback: crea la carpeta donde esté parado el IDE
        return new File("src" + File.separator + "data");
    }

    // --- MÉTODOS DE PERSISTENCIA ---

    private void guardarEnArchivo() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(tablaFacturas);
            out.writeObject(generadorId);
        } catch (IOException e) {
            System.err.println("Error al guardar facturas: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void cargarDesdeArchivo() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                tablaFacturas = (Map<Long, Factura>) in.readObject();
                generadorId = (Long) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar facturas: " + e.getMessage());
            }
        }
    }

    // --- MÉTODOS DE NEGOCIO ---

    @Override
    public Factura guardar(Factura factura) {
        factura.setId(generadorId);
        tablaFacturas.put(generadorId, factura);
        generadorId++;
        guardarEnArchivo();
        return factura;
    }

    @Override
    public Optional<Factura> buscarPorId(Long id) {
        return Optional.ofNullable(tablaFacturas.get(id));
    }

    @Override
    public List<Factura> buscarTodos() {
        return new ArrayList<>(tablaFacturas.values());
    }

    @Override
    public void eliminar(Long id) {
        tablaFacturas.remove(id);
        guardarEnArchivo();
    }

    @Override
    public Factura actualizar(Factura facturaModificada) {
        if (tablaFacturas.containsKey(facturaModificada.getId())) {
            tablaFacturas.put(facturaModificada.getId(), facturaModificada);
            guardarEnArchivo();
            return facturaModificada;
        }
        return null;
    }
}