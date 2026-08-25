package entity;

import java.io.Serializable;

public class OdExtraccion extends Odontologo implements Serializable {
    private static final long serialVersionUID = 1L;

    public OdExtraccion() {}

    public OdExtraccion(Long id, String nombre, String apellido, String dni, String email, String telefono, String matricula, Double recargoEspecialidad) {
        super(id, nombre, apellido, dni, email, telefono, matricula, recargoEspecialidad);
    }


}