package Taller.Vistas;

import Taller.Controlador.ControladorOrdenes;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import Taller.Modelo.Cliente;
import Taller.Modelo.OrdenDeTrabajo;
import Taller.Modelo.Vehiculo;

public class VistaRecepcionista extends JFrame {

    private ControladorOrdenes controladorOrdenes;

    // --- COMPONENTES DEL FORMULARIO ---
    private JTextField txtDni, txtNombre, txtTelefono, txtPatente, txtDescripcion, txtMarca, txtModelo, txtAnio;
    private JButton btnGenerarOrden;

    // --- COMPONENTES DEL BUSCADOR ---
    private JTextField txtPatenteBuscar;
    private JButton btnBuscarOrden, btnDevolverVehiculo;
    private JTextArea txtResultadoOrden;


    public VistaRecepcionista(ControladorOrdenes controladorOrdenes) {
        super("Módulo Recepcionista - Taller Mecánico");

        this.controladorOrdenes = controladorOrdenes;
        inicializarVentana();
        inicializarComponentes();
    }

    private void inicializarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        JTabbedPane pestañas = new JTabbedPane();

        pestañas.addTab("1. Generar Orden de Trabajo", crearPanelGenerarOrden());
        pestañas.addTab("2. Consultar y Devolver Vehículo", crearPanelBuscarOrden());

        add(pestañas, BorderLayout.CENTER);
    }

    // PESTAÑA 1: FORMULARIO DE NUEVA ORDEN
    private JPanel crearPanelGenerarOrden() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Generar Nueva Orden de Trabajo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Datos del cliente ---
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

        // --- Datos del vehículo ---
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

        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Descripción de la falla:"), gbc);
        gbc.gridx = 1;
        txtDescripcion = new JTextField(25);
        panel.add(txtDescripcion, gbc);

        // --- Botón para crear la orden ---
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        btnGenerarOrden = new JButton("Generar Orden de Trabajo");
        btnGenerarOrden.setBackground(new Color(40, 150, 40));
        btnGenerarOrden.setForeground(Color.BLACK);
        btnGenerarOrden.addActionListener(this::accionGenerarOrden);
        panel.add(btnGenerarOrden, gbc);

        return panel;
    }

    private void accionGenerarOrden(ActionEvent e) {
        String dni = txtDni.getText();
        String nombre = txtNombre.getText();
        String telefono = txtTelefono.getText();
        String patente = txtPatente.getText();
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        String anioStr = txtAnio.getText();
        String descripcion = txtDescripcion.getText();

        if (dni.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || patente.isEmpty() ||
                marca.isEmpty() || modelo.isEmpty() || anioStr.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Complete todos los campos antes de generar la orden.",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean respuesta;
        try {
            int dniNum = Integer.parseInt(dni);
            int telefonoNum = Integer.parseInt(telefono);
            int anioFab = Integer.parseInt(anioStr);

            respuesta = controladorOrdenes.generarOrden(
                    dniNum, nombre, telefonoNum, patente, marca, modelo, anioFab, descripcion);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: DNI, Teléfono o Año deben ser valores numéricos.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
            respuesta = false;
        }

        if (!respuesta) {
            JOptionPane.showMessageDialog(this,
                    "Generación de orden fallida.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Orden generada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposFormulario();
        }
    }

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

    // PESTAÑA 2: BUSCADOR DE ORDENES
    private JPanel crearPanelBuscarOrden() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Consulta de Ordenes"));

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

        txtResultadoOrden = new JTextArea(10, 50);
        txtResultadoOrden.setEditable(false);
        txtResultadoOrden.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        panel.add(pnlBusqueda, BorderLayout.NORTH);
        panel.add(new JScrollPane(txtResultadoOrden), BorderLayout.CENTER);

        return panel;
    }

    private void accionBuscarOrden(ActionEvent e) {
        String patente = txtPatenteBuscar.getText().trim();
        if (patente.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese una patente para buscar.",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE);
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

        StringBuilder sb = new StringBuilder();
        sb.append("ORDEN DE TRABAJO #").append(orden.getIdOrdenDeTrabajo()).append("\n");
        sb.append("Fecha de creación: ").append(orden.getFechaCreacion()).append("\n");
        sb.append("Estado actual: ").append(orden.getEstado()).append("\n\n");

        sb.append("CLIENTE\n");
        sb.append("DNI: ").append(cliente.getDni()).append("\n");
        sb.append("Nombre: ").append(cliente.getNombre()).append("\n");
        sb.append("Teléfono: ").append(cliente.getTelefono()).append("\n\n");

        sb.append("VEHÍCULO\n");
        sb.append("Patente: ").append(vehiculo.getPatente()).append("\n");
        sb.append("Marca: ").append(vehiculo.getMarca()).append("\n");
        sb.append("Modelo: ").append(vehiculo.getModelo()).append("\n");
        sb.append("Año de fabricación: ").append(vehiculo.getAñoFabricacion()).append("\n\n");

        sb.append("DIAGNÓSTICO\n");
        sb.append(orden.getDiagnostico()).append("\n");

        txtResultadoOrden.setText(sb.toString());

        // Habilitar el botón solo si la orden está finalizada
        btnDevolverVehiculo.setEnabled("Pagado".equalsIgnoreCase(orden.getEstado()));
    }

    private void accionDevolverVehiculo(ActionEvent e) {
        String patente = txtPatenteBuscar.getText();
        OrdenDeTrabajo orden = controladorOrdenes.buscarOrdenPorPatente(patente);

        if (orden != null && "Pagado".equalsIgnoreCase(orden.getEstado())) {
            controladorOrdenes.registrarEntregaVehiculo(orden.getIdOrdenDeTrabajo());
            JOptionPane.showMessageDialog(this,
                    "Vehículo devuelto correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            btnDevolverVehiculo.setEnabled(false);
            txtResultadoOrden.append("\nEstado actualizado: Entregado");
        }
    }
}