package repository;

import entity.Odontologo;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OdontologoRepository implements IRepository<Odontologo> {

    private Map<Long, Odontologo> tablaOdontologos = new HashMap<>();
    private Long generadorId = 1L;
    private String FILE_NAME; // Ya no es constante, se calcula dinámicamente

    public OdontologoRepository() {
        inicializarPersistencia();
        cargarDesdeArchivo();
    }

    private void inicializarPersistencia() {
        File dataFolder = encontrarCarpetaData();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        this.FILE_NAME = dataFolder.getAbsolutePath() + File.separator + "odontologos.dat";
    }

    private File encontrarCarpetaData() {
        // Opción 1: Intentar buscar en la ruta estándar (apertura desde 'Codigo - Sonrisa Feliz')
        File f = new File("src" + File.separator + "data");
        if (f.exists()) return f;

        // Opción 2: Respaldo buscando desde 'POO-Tp'
        f = new File("Codigo - Sonrisa Feliz" + File.separator + "src" + File.separator + "data");
        if (f.exists()) return f;

        // Opción 3: Fallback por si ninguna existe (crea la carpeta donde está parado el IDE)
        return new File("src" + File.separator + "data");
    }

    // --- MÉTODOS DE PERSISTENCIA ---

    private void guardarEnArchivo() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(tablaOdontologos);
            out.writeObject(generadorId);
        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void cargarDesdeArchivo() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                tablaOdontologos = (Map<Long, Odontologo>) in.readObject();
                generadorId = (Long) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar: " + e.getMessage());
            }
        }
    }

    // --- MÉTODOS SOBREESCRITOS ---

    @Override
    public Odontologo guardar(Odontologo odontologo) {
        odontologo.setId(generadorId);
        tablaOdontologos.put(generadorId, odontologo);
        generadorId++;
        guardarEnArchivo();
        return odontologo;
    }

    @Override
    public Optional<Odontologo> buscarPorId(Long id) {
        return Optional.ofNullable(tablaOdontologos.get(id));
    }

    @Override
    public List<Odontologo> buscarTodos() {
        return new ArrayList<>(tablaOdontologos.values());
    }

    @Override
    public void eliminar(Long id) {
        tablaOdontologos.remove(id);
        guardarEnArchivo();
    }

    @Override
    public Odontologo actualizar(Odontologo odontologoModificado) {
        if (tablaOdontologos.containsKey(odontologoModificado.getId())) {
            tablaOdontologos.put(odontologoModificado.getId(), odontologoModificado);
            guardarEnArchivo();
            return odontologoModificado;
        }
        return null;
    }
}