package repository;

public interface IFacturaStorage {
    String guardarComprobante(Long turnoId, String contenido);
}