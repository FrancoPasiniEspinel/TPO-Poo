package Taller.Modelo;

import java.util.Date;

public class Factura {
    private int idFactura;
    private Date fechaEmision;
    private int total;
    private String estadoFactura;

    public Factura(int idFactura, Date fechaEmision, int total, String estadoFactura) {
        this.idFactura = idFactura;
        this.fechaEmision = fechaEmision;
        this.total = total;
        this.estadoFactura = estadoFactura;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public int getTotal() {
        return total;
    }

    public String getEstadoFactura() {
        return estadoFactura;
    }
}
