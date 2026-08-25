package entity;

import java.io.Serializable;

public enum EstadoTurno implements Serializable {
    PENDIENTE,
    COMPLETADO,
    CANCELADO,
    AUSENTE;
}