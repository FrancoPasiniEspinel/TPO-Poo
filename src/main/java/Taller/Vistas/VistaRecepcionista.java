// Versión modificada de VistaRecepcionista con estilo profesional "Corporate Minimal"
// Aplicado manualmente componente por componente (Opción B)

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
    private JTextField txtDni, txtNombre, txtTelefono, txtPatente, txtMarca, txtModelo, txtAnio;
    private JTextArea txtDescripcion;
    private JButton btnGenerarOrden;
    private JButton btnConsultarDatosAsociados;

    // --- CAMPOS Y COMPONENTES PARA CONSULTAR/DEVOLVER VEHÍCULO ---
    private JTextField txtPatenteBuscar;
    private JButton btnBuscarOrden, btnDevolverVehiculo;
    private JPanel panelResultadoOrden;

    public VistaRecepcionista(ControladorOrdenes controladorOrdenes, ControladorMaestro controladorMaestro) {
        super("Módulo Recepcionista - Taller Mecánico");
        this.controladorOrdenes = controladorOrdenes;
        this.controladorMaestro = controladorMaestro;
        inicializarVentana();
        inicializarComponentes();
    }

    private void inicializarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {

        UIManager.put("Label.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("TextArea.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("ScrollPane.font", new Font("SansSerif", Font.PLAIN, 14));

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.setFont(new Font("SansSerif", Font.BOLD, 14));

        pestañas.addTab("1. Generar Orden", crearPanelGenerarOrden());
        pestañas.addTab("2. Consultar/Devolver", crearPanelBuscarOrden());

        add(pestañas, BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(Color.WHITE);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(200, 80, 80));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.addActionListener(this::accionCerrarSesion);

        panel.add(btnCerrarSesion);
        return panel;
    }

    private void accionCerrarSesion(ActionEvent e) {
        dispose();
        controladorMaestro.cerrarSesion();
    }

    private JPanel crearPanelGenerarOrden() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- COMPONENTES PROFESIONALES ---
        Font f14 = new Font("SansSerif", Font.PLAIN, 14);

        // --- PATENTE ---
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblPatente = new JLabel("Patente:");
        lblPatente.setFont(f14);
        panel.add(lblPatente, gbc);

        gbc.gridx = 1;
        txtPatente = new JTextField(12);
        txtPatente.setFont(f14);
        txtPatente.setBackground(Color.WHITE);
        txtPatente.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(txtPatente, gbc);

        // --- BOTÓN DEBAJO DE PATENTE ---
        gbc.gridx = 1; gbc.gridy = 1;
        btnConsultarDatosAsociados = new JButton("Obtener últimos datos asociados");
        btnConsultarDatosAsociados.setBackground(new Color(70, 120, 200));
        btnConsultarDatosAsociados.setForeground(Color.WHITE);
        btnConsultarDatosAsociados.setFocusPainted(false);
        btnConsultarDatosAsociados.addActionListener(this::accionConsultarDatosAsociados);
        panel.add(btnConsultarDatosAsociados, gbc);

        // --- CAMPOS CLIENTE ---
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblDni = new JLabel("DNI:");
        lblDni.setFont(f14);
        panel.add(lblDni, gbc);

        gbc.gridx = 1;
        txtDni = new JTextField(15);
        txtDni.setFont(f14);
        txtDni.setBackground(Color.WHITE);
        txtDni.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(txtDni, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(f14);
        panel.add(lblNombre, gbc);

        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        txtNombre.setFont(f14);
        txtNombre.setBackground(Color.WHITE);
        txtNombre.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(f14);
        panel.add(lblTelefono, gbc);

        gbc.gridx = 1;
        txtTelefono = new JTextField(15);
        txtTelefono.setFont(f14);
        txtTelefono.setBackground(Color.WHITE);
        txtTelefono.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(txtTelefono, gbc);

        // --- VEHÍCULO ---
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblMarca = new JLabel("Marca:");
        lblMarca.setFont(f14);
        panel.add(lblMarca, gbc);

        gbc.gridx = 1;
        txtMarca = new JTextField(15);
        txtMarca.setFont(f14);
        txtMarca.setBackground(Color.WHITE);
        txtMarca.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(txtMarca, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        JLabel lblModelo = new JLabel("Modelo:");
        lblModelo.setFont(f14);
        panel.add(lblModelo, gbc);

        gbc.gridx = 1;
        txtModelo = new JTextField(15);
        txtModelo.setFont(f14);
        txtModelo.setBackground(Color.WHITE);
        txtModelo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(txtModelo, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        JLabel lblAnio = new JLabel("Año de fabricación:");
        lblAnio.setFont(f14);
        panel.add(lblAnio, gbc);

        gbc.gridx = 1;
        txtAnio = new JTextField(6);
        txtAnio.setFont(f14);
        txtAnio.setBackground(Color.WHITE);
        txtAnio.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(txtAnio, gbc);

        // --- DESCRIPCIÓN MULTILÍNEA ---
        gbc.gridx = 0; gbc.gridy = 8;
        JLabel lblDesc = new JLabel("Falla reportada:");
        lblDesc.setFont(f14);
        panel.add(lblDesc, gbc);

        gbc.gridx = 1;
        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setFont(f14);
        txtDescripcion.setBackground(Color.WHITE);
        txtDescripcion.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(scrollDesc, gbc);

        // --- BOTÓN GENERAR ---
        gbc.gridx = 0; gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        btnGenerarOrden = new JButton("Generar Orden de Trabajo");
        btnGenerarOrden.setBackground(new Color(40, 150, 40));
        btnGenerarOrden.setForeground(Color.WHITE);
        btnGenerarOrden.setFocusPainted(false);
        btnGenerarOrden.addActionListener(this::accionGenerarOrden);

        panel.add(btnGenerarOrden, gbc);

        return panel;
    }

    private void accionConsultarDatosAsociados(ActionEvent e) {
        String patente = txtPatente.getText();
        OrdenDeTrabajo ordenAsociada = controladorOrdenes.buscarOrdenPorPatente(patente);

        if (ordenAsociada == null) {
            limpiarCamposFormulario();
            txtPatente.setText(patente);
            JOptionPane.showMessageDialog(this, "No existen datos asociados.", "Patente nueva", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Cliente c = ordenAsociada.getClienteAsignado();
        Vehiculo v = ordenAsociada.getVehiculo();

        JOptionPane.showMessageDialog(this, "Datos cargados correctamente.");

        txtDni.setText(String.valueOf(c.getDni()));
        txtNombre.setText(c.getNombre());
        txtTelefono.setText(String.valueOf(c.getTelefono()));
        txtMarca.setText(v.getMarca());
        txtModelo.setText(v.getModelo());
        txtAnio.setText(String.valueOf(v.getAñoFabricacion()));
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
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return;
        }

        try {
            int dniNum = Integer.parseInt(dni);
            int telNum = Integer.parseInt(telefono);
            int anio = Integer.parseInt(anioStr);

            String resp = controladorOrdenes.generarOrden(dniNum, nombre, telNum, patente, marca, modelo, anio, descripcion);

            switch (resp) {
                case "exito":
                    JOptionPane.showMessageDialog(this, "Orden generada correctamente.");
                    limpiarCamposFormulario();
                    break;

                case "duplicado":
                    JOptionPane.showMessageDialog(this, "El vehículo ya pertenece a otra orden.");
                    break;

                default:
                    JOptionPane.showMessageDialog(this, "Error al generar orden.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "DNI / Teléfono / Año deben ser numéricos.");
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

    private JPanel crearPanelBuscarOrden() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBusqueda.setBackground(Color.WHITE);

        JLabel lblBuscar = new JLabel("Patente:");
        lblBuscar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pnlBusqueda.add(lblBuscar);

        txtPatenteBuscar = new JTextField(10);
        txtPatenteBuscar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPatenteBuscar.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pnlBusqueda.add(txtPatenteBuscar);

        btnBuscarOrden = new JButton("Buscar");
        btnBuscarOrden.setBackground(new Color(70, 120, 200));
        btnBuscarOrden.setForeground(Color.WHITE);
        btnBuscarOrden.setFocusPainted(false);
        btnBuscarOrden.addActionListener(this::accionBuscarOrden);
        pnlBusqueda.add(btnBuscarOrden);

        btnDevolverVehiculo = new JButton("Devolver");
        btnDevolverVehiculo.setEnabled(false);
        btnDevolverVehiculo.setBackground(new Color(200, 120, 120));
        btnDevolverVehiculo.setForeground(Color.WHITE);
        btnDevolverVehiculo.setFocusPainted(false);
        btnDevolverVehiculo.addActionListener(this::accionDevolverVehiculo);
        pnlBusqueda.add(btnDevolverVehiculo);

        panelResultadoOrden = new JPanel();
        panelResultadoOrden.setLayout(new BoxLayout(panelResultadoOrden, BoxLayout.Y_AXIS));
        panelResultadoOrden.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panelResultadoOrden.setBackground(new Color(245, 245, 245));

        JScrollPane scrollResultado = new JScrollPane(panelResultadoOrden);
        scrollResultado.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollResultado.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        panel.add(pnlBusqueda, BorderLayout.NORTH);
        panel.add(scrollResultado, BorderLayout.CENTER);

        return panel;
    }

    private void accionBuscarOrden(ActionEvent e) {
        String patente = txtPatenteBuscar.getText();
        panelResultadoOrden.removeAll();

        if (patente.isEmpty()) {
            panelResultadoOrden.add(new JLabel("Ingrese una patente."));
            actualizarPanelResultado();
            return;
        }

        OrdenDeTrabajo o = controladorOrdenes.buscarOrdenPorPatente(patente);
        if (o == null) {
            panelResultadoOrden.add(new JLabel("No se encontró orden para: " + patente));
            btnDevolverVehiculo.setEnabled(false);
            actualizarPanelResultado();
            return;
        }

        Cliente c = o.getClienteAsignado();
        Vehiculo v = o.getVehiculo();

        panelResultadoOrden.add(crearTituloOrden(o.getIdOrdenDeTrabajo()));
        panelResultadoOrden.add(crearBloque("Estado", o.getEstado()));

        panelResultadoOrden.add(crearBloque("DNI", String.valueOf(c.getDni())));
        panelResultadoOrden.add(crearBloque("Nombre", c.getNombre()));
        panelResultadoOrden.add(crearBloque("Teléfono", String.valueOf(c.getTelefono())));

        panelResultadoOrden.add(crearBloque("Marca", v.getMarca()));
        panelResultadoOrden.add(crearBloque("Modelo", v.getModelo()));
        panelResultadoOrden.add(crearBloque("Año de fabricación", String.valueOf(v.getAñoFabricacion())));

        panelResultadoOrden.add(crearBloque("Falla reportada", o.getDiagnostico()));

        btnDevolverVehiculo.setEnabled("Pagado".equalsIgnoreCase(o.getEstado()));

        actualizarPanelResultado();
    }

    private JPanel crearTituloOrden(int numeroOrden) {
        JPanel cont = new JPanel(new BorderLayout());
        cont.setBorder(BorderFactory.createEmptyBorder(10, 8, 15, 8));
        cont.setBackground(new Color(245, 245, 245));

        JLabel titulo = new JLabel("Orden #" + numeroOrden);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));

        cont.add(titulo, BorderLayout.CENTER);
        return cont;
    }

    private JPanel crearBloque(String titulo, String valor) {
        JPanel bloque = new JPanel(new BorderLayout());
        bloque.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        bloque.setBackground(new Color(245, 245, 245));

        JLabel lblTitulo = new JLabel("<html><b>" + titulo + ":</b></html>");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel lblValor = new JLabel("<html><div style='width:350px;'>" +
                valor.replace("\n", "<br>") +
                "</div></html>");
        lblValor.setFont(new Font("SansSerif", Font.PLAIN, 14));

        bloque.add(lblTitulo, BorderLayout.NORTH);
        bloque.add(lblValor, BorderLayout.CENTER);

        return bloque;
    }

    private void actualizarPanelResultado() {
        panelResultadoOrden.revalidate();
        panelResultadoOrden.repaint();
    }

    private void accionDevolverVehiculo(ActionEvent e) {
        String patente = txtPatenteBuscar.getText();
        OrdenDeTrabajo o = controladorOrdenes.buscarOrdenPorPatente(patente);

        if (o != null && "Pagado".equalsIgnoreCase(o.getEstado())) {
            controladorOrdenes.registrarEntregaOrden(o.getIdOrdenDeTrabajo());
            JOptionPane.showMessageDialog(this, "Vehículo entregado correctamente.");
            btnDevolverVehiculo.setEnabled(false);
            accionBuscarOrden(null);
        }
    }
}
