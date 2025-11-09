package Taller.Modelo;

public class Cliente {
    private final String nombre;
    private final int dni;
    private final int telefono;

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
