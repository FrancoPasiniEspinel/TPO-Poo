package Taller.Vistas;

import Taller.Controlador.ControladorMaestro;
import Taller.Controlador.ControladorOrdenes;
import Taller.Modelo.Cliente;
import Taller.Modelo.OrdenDeTrabajo;
import Taller.Modelo.Vehiculo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VistaRecepcionista extends JFrame {

    // --- CONTROLADORES ASOCIADOS ---
    private final ControladorOrdenes controladorOrdenes;
    private final ControladorMaestro controladorMaestro;

    // --- CAMPOS DE ENTRADA PARA GENERAR ORDEN ---
    private JTextField txtDni, txtNombre, txtTelefono, txtPatente, txtDescripcion, txtMarca, txtModelo, txtAnio;
    private JButton btnGenerarOrden;

    // --- CAMPOS Y COMPONENTES PARA CONSULTAR/DEVOLVER VEHÍCULO ---
    private JTextField txtPatenteBuscar;
    private JButton btnBuscarOrden, btnDevolverVehiculo;
    private JTextArea txtResultadoOrden;

    // --- CONSTRUCTOR PRINCIPAL ---
    public VistaRecepcionista(ControladorOrdenes controladorOrdenes, ControladorMaestro controladorMaestro) {
        super("Módulo Recepcionista - Taller Mecánico");
        this.controladorOrdenes = controladorOrdenes;
        this.controladorMaestro = controladorMaestro;
        inicializarVentana();
        inicializarComponentes();
    }

    // --- CONFIGURACIÓN INICIAL DE LA VENTANA ---
    private void inicializarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    // --- CONSTRUCCIÓN DE PESTAÑAS Y ESTRUCTURA PRINCIPAL ---
    private void inicializarComponentes() {
        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("1. Generar Orden de Trabajo", crearPanelGenerarOrden());
        pestañas.addTab("2. Consultar y Devolver Vehículo", crearPanelBuscarOrden());
        add(pestañas, BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    // --- PANEL INFERIOR CON BOTÓN DE CIERRE DE SESIÓN ---
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(200, 80, 80));
        btnCerrarSesion.setForeground(Color.BLACK);
        btnCerrarSesion.addActionListener(this::accionCerrarSesion);
        panel.add(btnCerrarSesion);
        return panel;
    }

    // --- ACCIÓN PARA CERRAR SESIÓN Y NOTIFICAR AL CONTROLADOR MAESTRO ---
    private void accionCerrarSesion(ActionEvent e) {
        this.dispose();
        controladorMaestro.cerrarSesion();
    }

    // --- PANEL PARA GENERAR UNA NUEVA ORDEN DE TRABAJO ---
    private JPanel crearPanelGenerarOrden() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Generar Nueva Orden de Trabajo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- CAMPOS DE DATOS DEL CLIENTE ---
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1;
        txtDni = new JTextField(15);
        panel.add(txtDni, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        panel.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(15);
        panel.add(txtTelefono, gbc);

        // --- DATOS DEL VEHÍCULO ---
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Patente:"), gbc);
        gbc.gridx = 1;
        txtPatente = new JTextField(10);
        panel.add(txtPatente, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1;
        txtMarca = new JTextField(15);
        panel.add(txtMarca, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Modelo:"), gbc);
        gbc.gridx = 1;
        txtModelo = new JTextField(15);
        panel.add(txtModelo, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Año de Fabricación:"), gbc);
        gbc.gridx = 1;
        txtAnio = new JTextField(6);
        panel.add(txtAnio, gbc);

        // --- DESCRIPCIÓN DE LA FALLA ---
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Descripción de la falla:"), gbc);
        gbc.gridx = 1;
        txtDescripcion = new JTextField(25);
        panel.add(txtDescripcion, gbc);

        // --- BOTÓN PARA CONFIRMAR CREACIÓN DE ORDEN ---
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnGenerarOrden = new JButton("Generar Orden de Trabajo");
        btnGenerarOrden.setBackground(new Color(40, 150, 40));
        btnGenerarOrden.setForeground(Color.BLACK);
        btnGenerarOrden.addActionListener(this::accionGenerarOrden);
        panel.add(btnGenerarOrden, gbc);

        return panel;
    }

    // --- ACCIÓN PARA CREAR UNA NUEVA ORDEN DE TRABAJO ---
    private void accionGenerarOrden(ActionEvent e) {
        // Obtiene y valida los datos ingresados por el recepcionista
        String dni = txtDni.getText();
        String nombre = txtNombre.getText();
        String telefono = txtTelefono.getText();
        String patente = txtPatente.getText();
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        String anioStr = txtAnio.getText();
        String descripcion = txtDescripcion.getText();

        // Verifica que todos los campos estén completos
        if (dni.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || patente.isEmpty() ||
                marca.isEmpty() || modelo.isEmpty() || anioStr.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos antes de generar la orden.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Envío de los datos al controlador, con validación numérica
        String respuesta;
        try {
            int dniNum = Integer.parseInt(dni);
            int telefonoNum = Integer.parseInt(telefono);
            int anioFab = Integer.parseInt(anioStr);
            respuesta = controladorOrdenes.generarOrden(dniNum, nombre, telefonoNum, patente, marca, modelo, anioFab, descripcion);
        } catch (NumberFormatException ex) {
            respuesta = "noNumerico";
        }

        // Interpretación del resultado devuelto por el controlador
        switch (respuesta) {
            case "fallo":
                JOptionPane.showMessageDialog(this, "Generación de orden fallida.", "Error", JOptionPane.WARNING_MESSAGE);
                break;
            case "exito":
                JOptionPane.showMessageDialog(this, "Orden generada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposFormulario();
                break;
            case "noNumerico":
                JOptionPane.showMessageDialog(this, "Error: DNI, Teléfono o Año deben ser numéricos.", "Error de formato", JOptionPane.ERROR_MESSAGE);
                break;
            case "duplicado":
                JOptionPane.showMessageDialog(this, "El vehículo ya pertenece a una Orden De Trabajo.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    // --- LIMPIA LOS CAMPOS DEL FORMULARIO TRAS GENERAR UNA ORDEN ---
    private void limpiarCamposFormulario() {
        txtDni.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtPatente.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtAnio.setText("");
        txtDescripcion.setText("");
    }

    // --- PANEL PARA CONSULTAR UNA ORDEN EXISTENTE Y DEVOLVER EL VEHÍCULO ---
    private JPanel crearPanelBuscarOrden() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Consulta de Ordenes"));

        // Panel superior con campos de búsqueda y botones
        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBusqueda.add(new JLabel("Patente:"));
        txtPatenteBuscar = new JTextField(10);
        pnlBusqueda.add(txtPatenteBuscar);

        btnBuscarOrden = new JButton("Buscar Orden");
        btnBuscarOrden.addActionListener(this::accionBuscarOrden);
        pnlBusqueda.add(btnBuscarOrden);

        btnDevolverVehiculo = new JButton("Devolver Vehículo");
        btnDevolverVehiculo.setEnabled(false);
        btnDevolverVehiculo.setBackground(new Color(200, 100, 100));
        btnDevolverVehiculo.addActionListener(this::accionDevolverVehiculo);
        pnlBusqueda.add(btnDevolverVehiculo);

        // Área de texto para mostrar los resultados
        txtResultadoOrden = new JTextArea(10, 50);
        txtResultadoOrden.setEditable(false);
        txtResultadoOrden.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        panel.add(pnlBusqueda, BorderLayout.NORTH);
        panel.add(new JScrollPane(txtResultadoOrden), BorderLayout.CENTER);

        return panel;
    }

    // --- ACCIÓN PARA BUSCAR UNA ORDEN POR PATENTE ---
    private void accionBuscarOrden(ActionEvent e) {
        // Obtiene la orden desde el controlador y muestra toda su información
        String patente = txtPatenteBuscar.getText().trim();
        if (patente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una patente para buscar.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrdenDeTrabajo orden = controladorOrdenes.buscarOrdenPorPatente(patente);
        if (orden == null) {
            txtResultadoOrden.setText("No se encontró ninguna orden asociada a la patente: " + patente);
            btnDevolverVehiculo.setEnabled(false);
            return;
        }

        Cliente cliente = orden.getClienteAsignado();
        Vehiculo vehiculo = orden.getVehiculo();

        // Construcción del resumen detallado de la orden
        String sb = "ORDEN DE TRABAJO #" + orden.getIdOrdenDeTrabajo() + "\n" +
                "Fecha de creación: " + orden.getFechaCreacion() + "\n" +
                "Estado actual: " + orden.getEstado() + "\n\n" +
                "CLIENTE\n" +
                "DNI: " + cliente.dni() + "\n" +
                "Nombre: " + cliente.nombre() + "\n" +
                "Teléfono: " + cliente.telefono() + "\n\n" +
                "VEHÍCULO\n" +
                "Patente: " + vehiculo.getPatente() + "\n" +
                "Marca: " + vehiculo.getMarca() + "\n" +
                "Modelo: " + vehiculo.getModelo() + "\n" +
                "Año de fabricación: " + vehiculo.getAñoFabricacion() + "\n\n" +
                "DIAGNÓSTICO\n" +
                orden.getDiagnostico() + "\n";

        txtResultadoOrden.setText(sb);
        // El botón de devolución solo se habilita si la orden está pagada
        btnDevolverVehiculo.setEnabled("Pagado".equalsIgnoreCase(orden.getEstado()));
    }

    // --- ACCIÓN PARA REGISTRAR LA DEVOLUCIÓN DEL VEHÍCULO ---
    private void accionDevolverVehiculo(ActionEvent e) {
        // Cambia el estado de la orden a “Entregado” si ya fue pagada
        String patente = txtPatenteBuscar.getText();
        OrdenDeTrabajo orden = controladorOrdenes.buscarOrdenPorPatente(patente);
        if (orden != null && "Pagado".equalsIgnoreCase(orden.getEstado())) {
            controladorOrdenes.registrarEntregaOrden(orden.getIdOrdenDeTrabajo());
            JOptionPane.showMessageDialog(this, "Vehículo devuelto correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            btnDevolverVehiculo.setEnabled(false);
            txtResultadoOrden.append("\nEstado actualizado: Entregado");
        }
    }
}
