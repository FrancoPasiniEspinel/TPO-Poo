package Taller.Modelo;

import java.util.Date;

public class Vehiculo {
    private String patente;
    private String marca;
    private String modelo;
    private int añoFabricacion;
//    private Date ultimoServio;
//    private TipoCaja tipoCaja;
//    private int numeroMotor;
//    private int kilometraje;
//    private int numeroChasis;
//    private TipoCombustible tipoCombustible;


    public Vehiculo(String patente, String marca, String modelo, int añoFabricacion) {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.añoFabricacion = añoFabricacion;
    }

    public Vehiculo() {
    }

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAñoFabricacion() {
        return añoFabricacion;
    }

//    public Date getUltimoServio() {
//        return ultimoServio;
//    }
//
//    public TipoCaja getTipoCaja() {
//        return tipoCaja;
//    }
//
//    public int getNumeroMotor() {
//        return numeroMotor;
//    }
//
//    public int getKilometraje() {
//        return kilometraje;
//    }
//
//    public int getNumeroChasis() {
//        return numeroChasis;
//    }
//
//    public TipoCombustible getTipoCombustible() {
//        return tipoCombustible;
//    }
}
