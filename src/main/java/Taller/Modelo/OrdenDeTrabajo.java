package Taller.Modelo;

import java.util.Date;

public class OrdenDeTrabajo {
    private int idOrdenDeTrabajo;
    private String estado;
    private Date fechaCreacion;
    private Vehiculo vehiculo;
    private Mecanico mecanicoAsignado;
    private Cliente clienteAsignado;
    // private String informeTecnico;
    private String diagnostico;
    // private List<ItemRepuesto> repuestosUtilizados;

    public OrdenDeTrabajo(int idOrdenDeTrabajo, String estado, Date fechaCreacion, Vehiculo vehiculo, Mecanico mecanicoAsignado, Cliente clienteAsignado, String diagnostico) {
        this.idOrdenDeTrabajo = idOrdenDeTrabajo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.vehiculo = vehiculo;
        this.mecanicoAsignado = mecanicoAsignado;
        this.clienteAsignado = clienteAsignado;
        this.diagnostico = diagnostico;
    }

    public int getIdOrdenDeTrabajo() {
        return idOrdenDeTrabajo;
    }

    public String getEstado() {
        return estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Mecanico getMecanicoAsignado() {
        return mecanicoAsignado;
    }

    public Cliente getClienteAsignado() {
        return clienteAsignado;
    }

//    public String getInformeTecnico() {
//        return informeTecnico;
//    }

    public String getDiagnostico() {
        return diagnostico;
    }

//    public List<ItemRepuesto> getRepuestosUtilizados() {
//        return repuestosUtilizados;
//    }


}
