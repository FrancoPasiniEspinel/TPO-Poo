package Taller.Controlador;

import Taller.Modelo.Factura;
import Taller.Modelo.GestorFacturas;

public class ControladorFacturas {
    private final GestorFacturas gestorFacturas;

    public ControladorFacturas() {
        this.gestorFacturas = new GestorFacturas();
    }

    public Factura obtenerFacturaPorIdOrden(int idOrdenDeTrabajo) {
        Factura factura = gestorFacturas.obtenerFacturaPorIdOrden(idOrdenDeTrabajo);
        if (factura == null) {
            factura = gestorFacturas.generarFactura(idOrdenDeTrabajo);
        }
        return factura;
    }

    public boolean registrarPagoFactura(int idFactura) {
        return gestorFacturas.registrarPagoFactura(idFactura);
    }
}
