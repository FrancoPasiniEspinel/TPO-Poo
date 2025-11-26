package Taller.Modelo;

public class Cliente {
    private String nombre;
    private int dni;
    private int telefono;

    public Cliente(String nombre, int dni, int telefono) {
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDni() {
        return dni;
    }

    public int getTelefono() {
        return telefono;
    }
}
