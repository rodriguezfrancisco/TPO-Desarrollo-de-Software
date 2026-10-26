package service;

import entity.Factura;
import entity.Turno;

public interface IFacturaService extends IService<Factura> {
    Factura emitirYRegistrarFactura(Turno turno);
    void anularFactura(Long idFactura);
    void setAlmacenamiento(repository.IFacturaStorage almacenamiento);
}