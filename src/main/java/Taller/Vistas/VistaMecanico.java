package Taller.Vistas;

import Taller.Controlador.ControladorMaestro;
import Taller.Controlador.ControladorOrdenes;
import Taller.Modelo.Cliente;
import Taller.Modelo.Empleado;
import Taller.Modelo.OrdenDeTrabajo;
import Taller.Modelo.Vehiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VistaMecanico extends JFrame {

    private final Empleado mecanico;
    private final ControladorOrdenes controladorOrdenes;
    private final ControladorMaestro controladorMaestro;
    private OrdenDeTrabajo ordenAsignada;

    private JTextArea txtInfoOrden;
    private JTextArea txtDiagnostico;
    private JTextArea txtDetalleTecnico;
    private JTextField txtHoras;
    private JTable tblRepuestos;
    private JTextField txtNombreRepuesto;
    private JTextField txtCantidad;
    private DefaultTableModel modeloRepuestos;
    private JLabel lblHorasTotales;
    private JButton btnRegistrarHoras;
    private JButton btnGuardarDetalle;
    private JButton btnAgregarRepuesto;
    private JButton btnFinalizar;

    public VistaMecanico(Empleado mecanico, ControladorOrdenes controladorOrdenes, ControladorMaestro controladorMaestro) {
        super("Módulo Mecánico - Orden Asignada");
        this.controladorOrdenes = controladorOrdenes;
        this.controladorMaestro = controladorMaestro;
        this.mecanico = mecanico;
        this.ordenAsignada = controladorOrdenes.obtenerOrdenMecanico(mecanico.getLegajo());
        inicializarComponentes();
        mostrarDatosOrden();
        this.setSize(950, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        JPanel contenido = new JPanel(new BorderLayout(10, 10));
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtInfoOrden = new JTextArea(6, 60);
        txtInfoOrden.setEditable(false);
        txtInfoOrden.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtInfoOrden.setBorder(BorderFactory.createTitledBorder("Información de la Orden"));
        contenido.add(new JScrollPane(txtInfoOrden), BorderLayout.NORTH);

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Diagnóstico, Horas y Detalles", crearPanelDiagnosticoHorasDetalles());
        pestañas.addTab("Repuestos Usados", crearPanelRepuestos());
        contenido.add(pestañas, BorderLayout.CENTER);

        contenido.add(crearPanelAccionesFinales(), BorderLayout.SOUTH);
        this.add(contenido);

        if (ordenAsignada == null) {
            deshabilitarFuncionesPorFaltaDeOrden();
        }
    }

    private JPanel crearPanelDiagnosticoHorasDetalles() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        JPanel pnlDiag = new JPanel(new BorderLayout());
        pnlDiag.setBorder(BorderFactory.createTitledBorder("Diagnóstico General"));
        txtDiagnostico = new JTextArea(5, 60);
        txtDiagnostico.setEditable(false);
        pnlDiag.add(new JScrollPane(txtDiagnostico), BorderLayout.CENTER);
        panel.add(pnlDiag);

        JPanel pnlHoras = new JPanel();
        pnlHoras.setLayout(new BoxLayout(pnlHoras, BoxLayout.Y_AXIS));
        pnlHoras.setBorder(BorderFactory.createTitledBorder("Registro de Horas de Trabajo"));

        JPanel filaHoras = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        txtHoras = new JTextField(5);
        btnRegistrarHoras = new JButton("Registrar Horas");
        btnRegistrarHoras.addActionListener(this::accionRegistrarHoras);
        filaHoras.add(new JLabel("Horas trabajadas:"));
        filaHoras.add(txtHoras);
        filaHoras.add(btnRegistrarHoras);
        pnlHoras.add(filaHoras);

        lblHorasTotales = new JLabel();
        actualizarHorasTotales();
        JPanel filaTotal = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        filaTotal.add(lblHorasTotales);
        pnlHoras.add(filaTotal);
        panel.add(pnlHoras);

        JPanel pnlDetalle = new JPanel(new BorderLayout());
        pnlDetalle.setBorder(BorderFactory.createTitledBorder("Detalles Técnicos / Observaciones"));
        txtDetalleTecnico = new JTextArea(4, 60);
        pnlDetalle.add(new JScrollPane(txtDetalleTecnico), BorderLayout.CENTER);
        btnGuardarDetalle = new JButton("Guardar Detalle Técnico");
        btnGuardarDetalle.addActionListener(this::accionGuardarDetalleTecnico);
        pnlDetalle.add(btnGuardarDetalle, BorderLayout.SOUTH);
        panel.add(pnlDetalle);

        return panel;
    }

    private JPanel crearPanelRepuestos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] columnas = {"Nombre Repuesto", "Cantidad"};
        modeloRepuestos = new DefaultTableModel(columnas, 0);
        tblRepuestos = new JTable(modeloRepuestos);
        panel.add(new JScrollPane(tblRepuestos), BorderLayout.CENTER);

        JPanel pnlCarga = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlCarga.setBorder(BorderFactory.createTitledBorder("Agregar Repuesto Usado"));
        txtNombreRepuesto = new JTextField(15);
        txtCantidad = new JTextField(5);
        btnAgregarRepuesto = new JButton("Agregar");
        btnAgregarRepuesto.addActionListener(this::accionAgregarRepuesto);
        pnlCarga.add(new JLabel("Nombre:"));
        pnlCarga.add(txtNombreRepuesto);
        pnlCarga.add(new JLabel("Cantidad:"));
        pnlCarga.add(txtCantidad);
        pnlCarga.add(btnAgregarRepuesto);
        panel.add(pnlCarga, BorderLayout.NORTH);

        return panel;
    }

    private JPanel crearPanelAccionesFinales() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnFinalizar = new JButton("Finalizar Reparación");
        btnFinalizar.setBackground(new Color(60, 170, 60));
        btnFinalizar.addActionListener(this::accionFinalizar);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(200, 80, 80));
        btnCerrarSesion.setForeground(Color.BLACK);
        btnCerrarSesion.addActionListener(this::accionCerrarSesion);

        panel.add(btnFinalizar);
        panel.add(btnCerrarSesion);
        return panel;
    }

    private void accionCerrarSesion(ActionEvent e) {
        dispose();
        controladorMaestro.cerrarSesion();
    }

    private void actualizarHorasTotales() {
        if (ordenAsignada != null) {
            lblHorasTotales.setText("Total acumulado: " + ordenAsignada.getHorasTrabajo() + " horas");
        } else {
            lblHorasTotales.setText("Total acumulado: 0 horas");
        }
    }

    private void accionRegistrarHoras(ActionEvent e) {
        try {
            int horas = Integer.parseInt(txtHoras.getText());
            ordenAsignada.setHorasTrabajo(ordenAsignada.getHorasTrabajo() + horas);
            boolean respuesta = controladorOrdenes.actualizarHorasOrden(
                    ordenAsignada.getIdOrdenDeTrabajo(),
                    ordenAsignada.getHorasTrabajo()
            );
            if (!respuesta) {
                JOptionPane.showMessageDialog(this, "Actualización de horas fallida.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            actualizarHorasTotales();
            txtHoras.setText("");
            JOptionPane.showMessageDialog(this, "Horas registradas correctamente.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido para las horas trabajadas.");
        }
    }

    private void accionGuardarDetalleTecnico(ActionEvent e) {
        String detalle = txtDetalleTecnico.getText().trim();
        if (detalle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar algún detalle técnico antes de guardar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean respuesta = controladorOrdenes.modificarInformeTecnico(ordenAsignada.getIdOrdenDeTrabajo(), detalle);
        if (respuesta) {
            JOptionPane.showMessageDialog(this, "Detalle técnico guardado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "Guardado de detalle técnico fallido.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void accionAgregarRepuesto(ActionEvent e) {
        String nombre = txtNombreRepuesto.getText().trim();
        String cantidadStr = txtCantidad.getText().trim();
        if (nombre.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete ambos campos para agregar un repuesto.");
            return;
        }
        try {
            int cantidad = Integer.parseInt(cantidadStr);
            modeloRepuestos.addRow(new Object[]{nombre, cantidad});
            txtNombreRepuesto.setText("");
            txtCantidad.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser numérica.");
        }
    }

    private void accionFinalizar(ActionEvent e) {
        boolean respuesta = controladorOrdenes.registrarReparacionOrden(ordenAsignada.getIdOrdenDeTrabajo());
        if (!respuesta) {
            JOptionPane.showMessageDialog(this, "Registro de reparación fallida.", "Error", JOptionPane.WARNING_MESSAGE);
        } else {
            ordenAsignada = controladorOrdenes.obtenerOrdenMecanico(mecanico.getLegajo());
            actualizarVistaConNuevaOrden();
            JOptionPane.showMessageDialog(this, "La orden de trabajo ha sido marcada como Reparada.");
        }
    }

    private void actualizarVistaConNuevaOrden() {
        if (ordenAsignada == null) {
            deshabilitarFuncionesPorFaltaDeOrden();
            JOptionPane.showMessageDialog(this, "No hay nuevas órdenes asignadas al mecánico.", "Sin órdenes pendientes", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        mostrarDatosOrden();
    }

    private void deshabilitarFuncionesPorFaltaDeOrden() {
        txtDiagnostico.setText("(sin orden asignada)");
        txtDetalleTecnico.setText("(sin orden asignada)");
        txtHoras.setEnabled(false);
        txtNombreRepuesto.setEnabled(false);
        txtCantidad.setEnabled(false);
        btnRegistrarHoras.setEnabled(false);
        btnGuardarDetalle.setEnabled(false);
        btnAgregarRepuesto.setEnabled(false);
        btnFinalizar.setEnabled(false);
    }

    private void mostrarDatosOrden() {
        if (ordenAsignada == null) {
            txtInfoOrden.setText("No hay ninguna orden de trabajo asignada.");
            return;
        }

        Cliente c = ordenAsignada.getClienteAsignado();
        Vehiculo v = ordenAsignada.getVehiculo();

        String sb = "ORDEN DE TRABAJO #" + ordenAsignada.getIdOrdenDeTrabajo() + "\n" +
                "Estado: " + ordenAsignada.getEstado() + "\n" +
                "Fecha creación: " + ordenAsignada.getFechaCreacion() + "\n\n" +
                "CLIENTE:\n" +
                "DNI: " + c.dni() + "\n" +
                "Nombre: " + c.nombre() + "\n" +
                "Teléfono: " + c.telefono() + "\n\n" +
                "VEHÍCULO:\n" +
                "Patente: " + v.getPatente() + "\n" +
                "Marca: " + v.getMarca() + "\n" +
                "Modelo: " + v.getModelo() + "\n" +
                "Año: " + v.getAñoFabricacion();

        txtInfoOrden.setText(sb);
        txtDiagnostico.setText(ordenAsignada.getDiagnostico() == null ? "" : ordenAsignada.getDiagnostico());
        txtDetalleTecnico.setText(ordenAsignada.getInformeTecnico() == null ? "" : ordenAsignada.getInformeTecnico());
        actualizarHorasTotales();
    }
}
