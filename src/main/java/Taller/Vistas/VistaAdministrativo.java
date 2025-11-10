package Taller.Vistas;

import Taller.Controlador.ControladorFacturas;
import Taller.Controlador.ControladorMaestro;
import Taller.Controlador.ControladorOrdenes;
import Taller.Modelo.Factura;
import Taller.Modelo.OrdenDeTrabajo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.Optional;

public class VistaAdministrativo extends JFrame {

    // Controladores
    private final ControladorFacturas controladorFacturas;
    private final ControladorOrdenes contraladorOrdenes;
    private final ControladorMaestro controladorMaestro;

    //Objetos
    private Factura factura;
    private OrdenDeTrabajo ordenDeTrabajo;

    // Componentes
    private JTabbedPane tabbedPane;

    private JTable tblRepuestosFaltantes;
    private DefaultTableModel modeloTablaRepuestos;
    private JButton btnSolicitarPedido;
    private JButton btnRegistrarRecepcion;
    private JTextField txtCodRepuestoRecibido;
    private JTextField txtCantRecibida;

    // Campos pestaña de pagos
    private JTextField txtPatentePago;
    private JTextField txtIdOrdenPago;
    private JTextField txtMontoRecibido;
    private JComboBox<String> cmbMetodoPago;
    private JTextField txtNumTarjeta;
    private JTextField txtTitularTarjeta;
    private JTextField txtCodigoAut;
    private JButton btnBuscarFactura;
    private JButton btnConfirmarPago;

    public VistaAdministrativo(ControladorOrdenes controladorOrdenes, ControladorFacturas controladorFacturas, ControladorMaestro controladorMaestro) {
        super("Módulo Administrativo - Finanzas e Inventario");

        this.contraladorOrdenes = controladorOrdenes;
        this.controladorFacturas = controladorFacturas;
        this.controladorMaestro = controladorMaestro;

        inicializarComponentes();

        this.setSize(900, 650);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("1. Gestión de Inventario/Pedidos", crearPanelInventario());
        tabbedPane.addTab("2. Procesamiento de Pagos/Facturación", crearPanelPagos());

        this.add(tabbedPane, BorderLayout.CENTER);
        this.add(crearPanelCerrarSesion(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelCerrarSesion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(200, 80, 80));
        btnCerrarSesion.setForeground(Color.BLACK);
        btnCerrarSesion.addActionListener(this::accionCerrarSesion);
        panel.add(btnCerrarSesion);
        return panel;
    }

    private void accionCerrarSesion(ActionEvent e) {
        dispose();
        controladorMaestro.cerrarSesion();
    }

    private JPanel crearPanelInventario() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlFaltantes = crearPanelRepuestosFaltantes();
        panel.add(pnlFaltantes, BorderLayout.CENTER);

        JPanel pnlRecepcion = crearPanelRecepcionRepuestos();
        panel.add(pnlRecepcion, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelRepuestosFaltantes() {
        JPanel pnlFaltantes = new JPanel(new BorderLayout());
        pnlFaltantes.setBorder(BorderFactory.createTitledBorder("Repuestos a Encargar (Lista de Pedido Activo)"));

        String[] columnas = {"Código", "Nombre", "Cantidad Necesaria", "Precio Unitario", "Total"};
        modeloTablaRepuestos = new DefaultTableModel(columnas, 0);
        tblRepuestosFaltantes = new JTable(modeloTablaRepuestos);

        modeloTablaRepuestos.addRow(new Object[]{"Filt-01", "Filtro Aceite", 5, 45.50, 227.50});
        modeloTablaRepuestos.addRow(new Object[]{"Filt-02", "Filtro Aire", 10, 30.00, 300.00});

        btnSolicitarPedido = new JButton("CONFIRMAR PEDIDO DE REPUESTOS");
        btnSolicitarPedido.setBackground(new Color(50, 150, 250));
        btnSolicitarPedido.setForeground(Color.WHITE);

        pnlFaltantes.add(new JScrollPane(tblRepuestosFaltantes), BorderLayout.CENTER);
        pnlFaltantes.add(btnSolicitarPedido, BorderLayout.SOUTH);

        return pnlFaltantes;
    }

    private JPanel crearPanelRecepcionRepuestos() {
        JPanel pnlRecepcion = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlRecepcion.setBorder(BorderFactory.createTitledBorder("Carga de Repuestos Recibidos"));

        pnlRecepcion.add(new JLabel("Código Repuesto:"));
        txtCodRepuestoRecibido = new JTextField(10);
        pnlRecepcion.add(txtCodRepuestoRecibido);

        pnlRecepcion.add(new JLabel("Cantidad Recibida:"));
        txtCantRecibida = new JTextField(5);
        pnlRecepcion.add(txtCantRecibida);

        btnRegistrarRecepcion = new JButton("Registrar Recepción");
        btnRegistrarRecepcion.setBackground(new Color(40, 150, 40));
        btnRegistrarRecepcion.setForeground(Color.WHITE);
        btnRegistrarRecepcion.addActionListener(this::registrarRecepcionDeRepuestos);

        pnlRecepcion.add(btnRegistrarRecepcion);

        return pnlRecepcion;
    }

    private void registrarRecepcionDeRepuestos(ActionEvent e) {
        String codigo = txtCodRepuestoRecibido.getText().trim();
        String cantidadStr = txtCantRecibida.getText().trim();

        if (codigo.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar Código y Cantidad recibida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La Cantidad debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        actualizarTablaTrasRecepcion(codigo, cantidad);
    }

    private void actualizarTablaTrasRecepcion(String codigoRecibido, int cantidadRecibida) {
        DefaultTableModel modelo = (DefaultTableModel) tblRepuestosFaltantes.getModel();

        for (int i = modelo.getRowCount() - 1; i >= 0; i--) {
            String codigoTabla = (String) modelo.getValueAt(i, 0);

            if (codigoTabla.equals(codigoRecibido)) {
                Object cantidadObj = modelo.getValueAt(i, 2);
                int cantidadNecesaria;

                try {
                    cantidadNecesaria = ((Number) cantidadObj).intValue();
                } catch (Exception e) {
                    cantidadNecesaria = (int) modelo.getValueAt(i, 2);
                }

                if (cantidadRecibida >= cantidadNecesaria) {
                    modelo.removeRow(i);
                    JOptionPane.showMessageDialog(this, "¡Recepción Completa! Repuesto " + codigoRecibido + " borrado de la lista de pendientes.", "Actualización", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    int nuevoFaltante = cantidadNecesaria - cantidadRecibida;
                    double precioUnitario = (double) modelo.getValueAt(i, 3);
                    double nuevoTotal = nuevoFaltante * precioUnitario;

                    modelo.setValueAt(Optional.of(nuevoFaltante), i, 2);
                    modelo.setValueAt(Optional.of(nuevoTotal), i, 4);
                    JOptionPane.showMessageDialog(this, "Cantidad de repuesto " + codigoRecibido + " actualizada en la tabla. Faltan: " + nuevoFaltante, "Actualización Parcial", JOptionPane.INFORMATION_MESSAGE);
                }

                txtCodRepuestoRecibido.setText("");
                txtCantRecibida.setText("");
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "No se encontró ningún pedido pendiente para el código " + codigoRecibido + ".", "Búsqueda Fallida", JOptionPane.WARNING_MESSAGE);
    }

    // Panel de pagos
    private JPanel crearPanelPagos() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- 1. Campo Patente ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Patente del Vehículo:"), gbc);

        gbc.gridx = 1;
        txtPatentePago = new JTextField(10);
        panel.add(txtPatentePago, gbc);

        btnBuscarFactura = new JButton("Buscar Factura");
        gbc.gridx = 2;
        btnBuscarFactura.setBackground(new Color(70, 130, 180));
        btnBuscarFactura.setForeground(Color.WHITE);
        btnBuscarFactura.addActionListener(this::buscarFactura);
        panel.add(btnBuscarFactura, gbc);

        // --- 2. ID Orden ---
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("ID Orden Asociada:"), gbc);
        gbc.gridx = 1;
        txtIdOrdenPago = new JTextField(10);
        txtIdOrdenPago.setEditable(false);
        panel.add(txtIdOrdenPago, gbc);

        // --- 3. Monto ---
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Monto a Pagar:"), gbc);
        gbc.gridx = 1;
        txtMontoRecibido = new JTextField(10);
        panel.add(txtMontoRecibido, gbc);

        // --- 4. Método de Pago ---
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Método de Pago:"), gbc);
        gbc.gridx = 1;
        cmbMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Tarjeta/Transferencia"});
        panel.add(cmbMetodoPago, gbc);
        cmbMetodoPago.addActionListener(ev -> toggleCamposTarjeta());

        // --- 5. Campos adicionales para tarjeta ---
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Número de Tarjeta:"), gbc);
        gbc.gridx = 1;
        txtNumTarjeta = new JTextField(15);
        panel.add(txtNumTarjeta, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Titular de Tarjeta:"), gbc);
        gbc.gridx = 1;
        txtTitularTarjeta = new JTextField(15);
        panel.add(txtTitularTarjeta, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Código Autorización:"), gbc);
        gbc.gridx = 1;
        txtCodigoAut = new JTextField(10);
        panel.add(txtCodigoAut, gbc);

        // Inicialmente, ocultar los campos de tarjeta
        setCamposTarjetaVisible(false);

        // --- 6. Botón Confirmar Pago ---
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        btnConfirmarPago = new JButton("CONFIRMAR PAGO Y ACEPTAR RETIRO");
        btnConfirmarPago.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirmarPago.setBackground(new Color(250, 100, 100));
        btnConfirmarPago.setForeground(Color.WHITE);
        btnConfirmarPago.addActionListener(this::confirmarPago);
        panel.add(btnConfirmarPago, gbc);

        return panel;
    }

    private void toggleCamposTarjeta() {
        boolean esTarjeta = cmbMetodoPago.getSelectedItem().toString().equalsIgnoreCase("Tarjeta/Transferencia");
        setCamposTarjetaVisible(esTarjeta);
    }

    private void setCamposTarjetaVisible(boolean visible) {
        txtNumTarjeta.setVisible(visible);
        txtTitularTarjeta.setVisible(visible);
        txtCodigoAut.setVisible(visible);
    }

    private void buscarFactura(ActionEvent e) {
        String patente = txtPatentePago.getText().trim();
        if (patente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar una patente.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ordenDeTrabajo = contraladorOrdenes.buscarOrdenPorPatente(patente);
        if (ordenDeTrabajo == null){
            JOptionPane.showMessageDialog(this, "No se encontro orden asociada a la patente",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Objects.equals(ordenDeTrabajo.getEstado(), "Reparado")){
            JOptionPane.showMessageDialog(this, "La orden no se encuentra en estado \"Reparado\"\nNo se puede generar una factura.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        factura = controladorFacturas.obtenerFacturaPorIdOrden(ordenDeTrabajo.getIdOrdenDeTrabajo());
        if (factura == null){
            JOptionPane.showMessageDialog(this, "No se pudo obtener la factura asociada a la patente",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Factura encontrada para la patente " + patente + ".\nDatos cargados automáticamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        txtIdOrdenPago.setText(String.valueOf((factura.getIdFactura())));
        txtMontoRecibido.setText(String.valueOf(factura.getTotal()));
    }

    private void confirmarPago(ActionEvent e) {
        String idOrdenStr = txtIdOrdenPago.getText().trim();
        String montoStr = txtMontoRecibido.getText().trim();
        String metodoPago = (String) cmbMetodoPago.getSelectedItem();

        if (idOrdenStr.isEmpty() || montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe buscar una factura y completar los datos de pago.", "Datos Incompletos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (metodoPago.equalsIgnoreCase("Tarjeta/Transferencia")) {
            if (txtNumTarjeta.getText().trim().isEmpty() ||
                    txtTitularTarjeta.getText().trim().isEmpty() ||
                    txtCodigoAut.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos de tarjeta.", "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Logica de tramitacion y confirmacion de pago con entidad bancaria sí corresponde

        boolean respuesta;

        respuesta = contraladorOrdenes.registrarPagoOrden(ordenDeTrabajo.getIdOrdenDeTrabajo());
        if (!respuesta){
            JOptionPane.showMessageDialog(this, "No se actualizar el estado de la orden.", "Error", JOptionPane.WARNING_MESSAGE);
        }

        respuesta = controladorFacturas.registrarPagoFactura(factura.getIdFactura());
        if (!respuesta){
            JOptionPane.showMessageDialog(this, "No se actualizar el estado de la factura.", "Error", JOptionPane.WARNING_MESSAGE);
        }

        JOptionPane.showMessageDialog(this,
                "Pago registrado correctamente para la orden #" + idOrdenStr +
                        "\nMétodo: " + metodoPago +
                        "\nMonto: $" + montoStr +
                        "\nVehículo habilitado para retiro.",
                "Pago Confirmado", JOptionPane.INFORMATION_MESSAGE);

        limpiarCamposPago();
    }

    private void limpiarCamposPago() {
        txtPatentePago.setText("");
        txtIdOrdenPago.setText("");
        txtMontoRecibido.setText("");
        txtNumTarjeta.setText("");
        txtTitularTarjeta.setText("");
        txtCodigoAut.setText("");
        cmbMetodoPago.setSelectedIndex(0);
        setCamposTarjetaVisible(false);
    }
}
