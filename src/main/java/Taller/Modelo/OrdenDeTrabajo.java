package Taller.Modelo;

import java.util.Date;

public class OrdenDeTrabajo {
    private final int idOrdenDeTrabajo;
    private final Date fechaCreacion;
    private final Vehiculo vehiculo;
    private String estado;
    private Mecanico mecanicoAsignado;
    private Cliente clienteAsignado;
    private String informeTecnico;
    private String diagnostico;
    private int horasTrabajo;

    public OrdenDeTrabajo(int idOrdenDeTrabajo, String estado, Date fechaCreacion, Vehiculo vehiculo, Mecanico mecanicoAsignado, Cliente clienteAsignado, String diagnostico, String informeTecnico, int horasTrabajo) {
        this.idOrdenDeTrabajo = idOrdenDeTrabajo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.vehiculo = vehiculo;
        this.mecanicoAsignado = mecanicoAsignado;
        this.clienteAsignado = clienteAsignado;
        this.diagnostico = diagnostico;
        this.informeTecnico = informeTecnico;
        this.horasTrabajo = horasTrabajo;
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

    public Cliente getClienteAsignado() {
        return clienteAsignado;
    }

    public String getInformeTecnico() {
        return informeTecnico;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public int getHorasTrabajo() {
        return horasTrabajo;
    }

    public void setHorasTrabajo(int horasTrabajo) {
        this.horasTrabajo = horasTrabajo;
    }
}
