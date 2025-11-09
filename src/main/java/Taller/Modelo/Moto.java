package Taller.Modelo;

import java.util.Date;

public class Moto extends Vehiculo {
    private final int cant_Tiempos;
    private final int cilindrada;
    private final TipoTraccionMoto tipoTraccionMoto;
    private final TipoFrenosMoto tipoFrenosMoto;

    public Moto(String patente, String marca, String modelo, int añoFabricacion, Date ultimoServio, TipoCaja tipoCaja, int numeroMotor, int kilometraje, int numeroChasis, TipoCombustible tipoCombustible, int cant_Tiempos, int cilindrada, TipoTraccionMoto tipoTraccionMoto, TipoFrenosMoto tipoFrenosMoto) {
        // super(patente, marca, modelo, añoFabricacion, ultimoServio, tipoCaja, numeroMotor, kilometraje, numeroChasis, tipoCombustible);
        this.cant_Tiempos = cant_Tiempos;
        this.cilindrada = cilindrada;
        this.tipoTraccionMoto = tipoTraccionMoto;
        this.tipoFrenosMoto = tipoFrenosMoto;
    }
}
