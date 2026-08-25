package repository;

import entity.Turno;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TurnoRepository implements IRepository<Turno> {

    private List<Turno> tablaTurnos = new ArrayList<>();
    private Long generadorId = 1L;
    private String FILE_NAME; // Dinámico

    public TurnoRepository() {
        inicializarPersistencia();
        cargarDesdeArchivo();
    }

    private void inicializarPersistencia() {
        File dataFolder = encontrarCarpetaData();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        this.FILE_NAME = dataFolder.getAbsolutePath() + File.separator + "turnos.dat";
    }

    private File encontrarCarpetaData() {
        // 1. Ruta estándar (si el IDE está en 'Codigo - Sonrisa Feliz')
        File f = new File("src" + File.separator + "data");
        if (f.exists()) return f;

        // 2. Respaldo (si el IDE está en 'POO-Tp')
        f = new File("Codigo - Sonrisa Feliz" + File.separator + "src" + File.separator + "data");
        if (f.exists()) return f;

        // 3. Fallback: creación local
        return new File("src" + File.separator + "data");
    }

    // --- MÉTODOS DE PERSISTENCIA ---

    private void guardarEnArchivo() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(tablaTurnos);
            out.writeObject(generadorId);
        } catch (IOException e) {
            System.err.println("Error al guardar turnos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void cargarDesdeArchivo() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                tablaTurnos = (List<Turno>) in.readObject();
                generadorId = (Long) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar turnos: " + e.getMessage());
            }
        }
    }

    // --- MÉTODOS DE NEGOCIO ---

    @Override
    public Turno guardar(Turno turno) {
        turno.setId(generadorId);
        generadorId++;
        tablaTurnos.add(turno);
        guardarEnArchivo();
        return turno;
    }

    @Override
    public Optional<Turno> buscarPorId(Long id) {
        return tablaTurnos.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Turno> buscarTodos() {
        return new ArrayList<>(tablaTurnos);
    }

    @Override
    public void eliminar(Long id) {
        if (tablaTurnos.removeIf(turno -> turno.getId().equals(id))) {
            guardarEnArchivo();
        }
    }

    @Override
    public Turno actualizar(Turno turnoModificado) {

        for (Turno actual : tablaTurnos) {
            if (actual.getId().equals(turnoModificado.getId())) {

                actual.setPaciente(turnoModificado.getPaciente());
                actual.setOdontologo(turnoModificado.getOdontologo());
                actual.setFecha(turnoModificado.getFecha());
                actual.setHora(turnoModificado.getHora());
                actual.setEstado(turnoModificado.getEstado());

                guardarEnArchivo();
                return actual;
            }
        }
        return null;
    }
}