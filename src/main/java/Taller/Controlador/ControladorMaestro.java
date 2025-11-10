package Taller.Controlador;

import Taller.Modelo.Empleado;
import Taller.Vistas.VistaAdministrativo;
import Taller.Vistas.VistaLogin;
import Taller.Vistas.VistaMecanico;
import Taller.Vistas.VistaRecepcionista;

import javax.swing.*;

public class ControladorMaestro {
    // Vistas
    private JFrame vistaAdministrativo;
    private JFrame vistaMecanico;
    private JFrame vistaRecepcionista;
    private JFrame vistaLogin;

    // Controladores
    private ControladorLogin controladorLogin;
    private ControladorOrdenes controladorOrdenes;
    private ControladorFacturas controladorFacturas;

    public void iniciar() {
        controladorLogin = new ControladorLogin(this);
        this.vistaLogin = new VistaLogin(controladorLogin);
        vistaLogin.setVisible(true);
    }

    public void cambiarVentana(Empleado empleado) {
        vistaLogin.setVisible(false);

        switch (empleado.getTipo()) {
            case "Mecanico":
                controladorOrdenes = new ControladorOrdenes();
                vistaMecanico = new VistaMecanico(empleado, controladorOrdenes, this);
                vistaMecanico.setVisible(true);
                break;
            case "Administrativo":
                controladorFacturas = new ControladorFacturas();
                controladorOrdenes = new ControladorOrdenes();
                vistaAdministrativo = new VistaAdministrativo(controladorOrdenes, controladorFacturas, this);
                vistaAdministrativo.setVisible(true);
                break;
            case "Recepcionista":
                controladorOrdenes = new ControladorOrdenes();
                vistaRecepcionista = new VistaRecepcionista(controladorOrdenes, this);
                vistaRecepcionista.setVisible(true);
                break;
        }
    }

    public void cerrarSesion() {
        controladorLogin = new ControladorLogin(this);
        this.vistaLogin = new VistaLogin(controladorLogin);
        vistaLogin.setVisible(true);
    }
}
