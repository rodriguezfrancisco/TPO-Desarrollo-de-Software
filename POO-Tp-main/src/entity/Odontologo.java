package entity;

import java.io.Serializable;

public class Odontologo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    private String matricula;
    private Especialidad especialidad; // Nueva propiedad para la especialidad
    private Double recargo;

    public Odontologo() {}


    public Odontologo(Long id, String nombre, String apellido, String dni, String email, String telefono, String matricula, Especialidad especialidad, Double recargo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.recargo = recargo;
    }

    

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }
    public Double getRecargo() { return recargo; }
    public void setRecargo(Double recargo) { this.recargo= recargo; }

    @Override
    public String toString() {
        return "[ID: " + id + "] " + nombre + " " + apellido + " - Matrícula: " + matricula + " - Especialidad: " + especialidad;
    }
}