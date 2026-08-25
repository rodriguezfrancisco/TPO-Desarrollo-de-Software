package service;

public class Validador {
    
    public static void validarTextoObligatorio(String valor, String campo, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatoInvalidoException(campo, mensaje);
        }
    }

    public static void validarNumeroPositivo(Double valor, String campo, String mensaje) {
        if (valor == null || valor <= 0) {
            throw new DatoInvalidoException(campo, mensaje);
        }
    }

    public static void validarObjeto(Object objeto, String campo, String mensaje) {
        if (objeto == null) {
            throw new DatoInvalidoException(campo, mensaje);
        }
    }

    public static void validarId(Long id, String campo) {
        if (id == null || id <= 0) {
            throw new DatoInvalidoException(campo, "El ID provisto no es válido.");
        }
    }
}