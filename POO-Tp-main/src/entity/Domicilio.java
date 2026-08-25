package entity;

import java.io.Serializable;

public class Domicilio implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String calle;
    private String altura;
    private String localidad;
    private String provincia;

    public Domicilio() {}

    // CONSTRUCTOR 1: Para nuevos registros (Sin ID)
    public Domicilio(String calle, String altura, String localidad, String provincia) {
        this.calle = calle;
        this.altura = altura;
        this.localidad = localidad;
        this.provincia = provincia;
    }

    // CONSTRUCTOR 2: Para recuperar de la Base de Datos (Con ID)
    public Domicilio(Long id, String calle, String altura, String localidad, String provincia) {
        this.id = id;
        this.calle = calle;
        this.altura = altura;
        this.localidad = localidad;
        this.provincia = provincia;
    }

    // Getters y Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public String getAltura() { return altura; }
    public void setAltura(String altura) { this.altura = altura; }
    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    @Override
    public String toString() {
        return calle + " " + altura + ", " + localidad + " (" + provincia + ")";
    }
}