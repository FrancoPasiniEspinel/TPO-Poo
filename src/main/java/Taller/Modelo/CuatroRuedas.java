package Taller.Modelo;

import java.util.Date;

public abstract class CuatroRuedas extends Vehiculo {
    private final int cantPuertas;
    private final boolean tieneAireAcondicionado;
    private final String tipoDeTraccion;
    private final String tipoMotor;

    public CuatroRuedas(String patente, String marca, String modelo, int añoFabricacion, Date ultimoServio, TipoCaja tipoCaja, int numeroMotor, int kilometraje, int numeroChasis, TipoCombustible tipoCombustible, int cantPuertas, boolean tieneAireAcondicionado, String tipoDeTraccion, String tipoMotor) {
        // super(patente, marca, modelo, añoFabricacion, ultimoServio, tipoCaja, numeroMotor, kilometraje, numeroChasis, tipoCombustible);
        this.cantPuertas = cantPuertas;
        this.tieneAireAcondicionado = tieneAireAcondicionado;
        this.tipoDeTraccion = tipoDeTraccion;
        this.tipoMotor = tipoMotor;
    }
}
