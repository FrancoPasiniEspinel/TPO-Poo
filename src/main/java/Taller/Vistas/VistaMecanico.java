package Taller.Vistas;

import Taller.Modelo.OrdenDeTrabajo;
import Taller.Modelo.Repuesto;
import Taller.Modelo.Vehiculo; // Importar Vehiculo para mostrar la patente
import Taller.Modelo.Mecanico; // Importar Mecanico para la simulación

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

// Importar los Gestores necesarios
// import Taller.Gestor.GestorMecanico;
// import Taller.Gestor.GestorInventario;

public class VistaMecanico extends JFrame {

    // ** VARIABLE CRUCIAL: Almacena la orden que se está editando **
    private OrdenDeTrabajo ordenActiva = null;

    // DEPENDENCIAS (Se comentan)
    // private final GestorMecanico gestorMecanico;

    // Contenedores Principales
    private JPanel pnlCentral; // Usa CardLayout
    private CardLayout cardLayout;

    // Componentes de la GUI
    private JTable tblOrdenesAsignadas; // Tabla inicial
    private JButton btnIniciarReparacion;
    private JTextArea txtInfoOrden;

    // Componentes del Panel de Edición (Pestaña 1)
    private JTextArea txtDiagnostico;
    private JTextField txtHoras;
    private JButton btnFinalizar;
    private JButton btnRegistrarHoras;
    private JButton btnRegistrarDiagnostico;

    // Simulación de clases para que compile
    static class VehiculoSimulado extends Vehiculo {
        private String patente;
        public VehiculoSimulado(String patente) { this.patente = patente; }
        public String getPatente() { return patente; }
    }
    static class OrdenSimulada extends OrdenDeTrabajo {
        public OrdenSimulada(int id, String estado, String cliente, String patente) {
            super(id, null, null, new VehiculoSimulado(patente), new Mecanico(), null, null);
            // Simulación de campos necesarios
            this.estado = estado;
            this.cliente = cliente;
        }
        private String estado;
        private String cliente;

        //public String getEstado() { return estado; }
        public String getNombreCliente() { return cliente; }
    }


    public VistaMecanico(/* GestorMecanico gm, GestorInventario gi */) {
        super("Módulo Mecánico - Gestión de Reparación");

        inicializarComponentes();

        this.setSize(900, 650);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        // --- Contenedor Central con CardLayout ---
        cardLayout = new CardLayout();
        pnlCentral = new JPanel(cardLayout);

        // 1. Panel Inicial: Lista de Órdenes
        pnlCentral.add(crearPanelListaOrdenes(), "LISTA");

        // 2. Panel Activo: Edición de la Orden
        pnlCentral.add(crearPanelEdicionOrden(), "EDICION");

        this.add(pnlCentral, BorderLayout.CENTER);

        // Muestra la lista de órdenes al inicio
        mostrarListaOrdenes();
    }

    // =================================================================================
    // ESTADO 1: LISTA DE ÓRDENES ASIGNADAS
    // =================================================================================

    private JPanel crearPanelListaOrdenes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Órdenes Asignadas - Seleccione para trabajar"));

        // Tabla con las órdenes
        String[] columnas = {"ID Orden", "Cliente", "Patente", "Estado"};
        tblOrdenesAsignadas = new JTable(new DefaultTableModel(columnas, 0));

        // Simulación: Cargar datos para el ejemplo
        DefaultTableModel modelo = (DefaultTableModel) tblOrdenesAsignadas.getModel();
        modelo.addRow(new Object[]{(Object) 101, "Juan Pérez", "ABC 999", "PENDIENTE"});
        modelo.addRow(new Object[]{(Object) 102, "Ana Gómez", "XYZ 555", "EN ESPERA"});

        btnIniciarReparacion = new JButton("ACCEDER A LA REPARACIÓN SELECCIONADA");
        btnIniciarReparacion.setBackground(new Color(50, 150, 250));
        btnIniciarReparacion.setForeground(Color.BLACK);

        // Conexión clave: Pasar a la vista de edición
        btnIniciarReparacion.addActionListener(this::iniciarReparacion);

        panel.add(new JScrollPane(tblOrdenesAsignadas), BorderLayout.CENTER);
        panel.add(btnIniciarReparacion, BorderLayout.SOUTH);

        return panel;
    }

    private void mostrarListaOrdenes() {
        // Aquí se llamaría a gestorMecanico.obtenerOrdenesAsignadas()
        cardLayout.show(pnlCentral, "LISTA");
        this.setTitle("Módulo Mecánico - Órdenes Asignadas");
    }

    private void iniciarReparacion(ActionEvent e) {
        int filaSeleccionada = tblOrdenesAsignadas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una orden para iniciar la reparación.");
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tblOrdenesAsignadas.getModel();
        int idOrden = (int) modelo.getValueAt(filaSeleccionada, 0);
        String cliente = (String) modelo.getValueAt(filaSeleccionada, 1);
        String patente = (String) modelo.getValueAt(filaSeleccionada, 2);

        // 1. **SEGURIDAD Y CONEXIÓN**: Se carga el objeto OrdenDeTrabajo en la variable de estado
        this.ordenActiva = new OrdenSimulada(idOrden, "EN PROCESO", cliente, patente);

        // 2. Muestra la información de la orden activa en el panel de edición
        String info = "ORDEN ID " + idOrden + " | Cliente: " + cliente + " | Patente: " + patente;
        txtInfoOrden.setText(info);

        // 3. Cambia la vista al panel de edición
        cardLayout.show(pnlCentral, "EDICION");
        this.setTitle("Módulo Mecánico - Editando Orden ID: " + idOrden);
    }

    // =================================================================================
    // ESTADO 2: EDICIÓN DE LA ORDEN ACTIVA (DIAGNÓSTICO, HORAS, REPUESTOS)
    // =================================================================================

    private JPanel crearPanelEdicionOrden() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Panel Norte: Información de la Orden Activa
        txtInfoOrden = new JTextArea(3, 70);
        txtInfoOrden.setEditable(false);
        txtInfoOrden.setBorder(BorderFactory.createTitledBorder("Orden Activa"));
        panel.add(new JScrollPane(txtInfoOrden), BorderLayout.NORTH);

        // TabbedPane con Diagnóstico/Horas y Repuestos
        JTabbedPane tbpEdicion = new JTabbedPane();
        tbpEdicion.addTab("Diagnóstico y Horas", crearPanelDetallesDiagnostico());
        tbpEdicion.addTab("Registro de Repuestos", crearPanelRepuestos());
        panel.add(tbpEdicion, BorderLayout.CENTER);

        // Panel Sur: Botones de Acción Global (Marcar Espera, Finalizar)
        panel.add(crearPanelAccionesFinales(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelDetallesDiagnostico() {
        JPanel pnlDetalles = new JPanel(new GridLayout(3, 1, 10, 10));

        // 1. Diagnóstico (registrarDiagnostico)
        JPanel pnlDiag = new JPanel(new BorderLayout());
        pnlDiag.setBorder(BorderFactory.createTitledBorder("Diagnóstico y Repuestos Necesarios"));
        txtDiagnostico = new JTextArea(5, 30);
        btnRegistrarDiagnostico = new JButton("Registrar Diagnóstico y Repuestos");
        // btnRegistrarDiagnostico.addActionListener(e -> registrarDiagnostico(ordenActiva, txtDiagnostico.getText()));
        pnlDiag.add(new JScrollPane(txtDiagnostico), BorderLayout.CENTER);
        pnlDiag.add(btnRegistrarDiagnostico, BorderLayout.SOUTH);
        pnlDetalles.add(pnlDiag);

        // 2. Horas (registrarHorasTrabajo)
        JPanel pnlHoras = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlHoras.setBorder(BorderFactory.createTitledBorder("Horas de Trabajo"));
        txtHoras = new JTextField(5);
        btnRegistrarHoras = new JButton("Registrar Horas");
        // Conexión: Usa ordenActiva.getIdTurno() para saber a qué auto asignar las horas
        // btnRegistrarHoras.addActionListener(e -> registrarHorasTrabajo(ordenActiva.getIdTurno(), Double.parseDouble(txtHoras.getText())));
        pnlHoras.add(new JLabel("Horas Trabajadas:"));
        pnlHoras.add(txtHoras);
        pnlHoras.add(btnRegistrarHoras);
        pnlDetalles.add(pnlHoras);

        // 3. Observaciones (agregarObservaciones)
        // Similar a horas, usa ordenActiva.getIdTurno()

        return pnlDetalles; // Simplificado
    }

    private JPanel crearPanelRepuestos() {
        // [CÓDIGO OMITIDO POR ESPACIO]
        return new JPanel();
    }

    private JPanel crearPanelAccionesFinales() {
        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnFinalizar = new JButton("FINALIZAR REPARACIÓN");
        // btnFinalizar.addActionListener(e -> finalizarReparacion(ordenActiva.getIdTurno()));

        JButton btnMarcarEspera = new JButton("MARCAR EN ESPERA");
        // btnMarcarEspera.addActionListener(e -> marcarOrdenEnEspera(ordenActiva.getIdTurno()));

        // NUEVO: Botón para volver a la lista si se equivocó o terminó.
        JButton btnVolver = new JButton("<< Volver a Lista");
        btnVolver.addActionListener(e -> mostrarListaOrdenes());

        pnlAcciones.add(btnVolver);
        pnlAcciones.add(btnMarcarEspera);
        pnlAcciones.add(btnFinalizar);
        return pnlAcciones;
    }

    // =================================================================================
    // MÉTODOS DEL DIAGRAMA (Contratos)
    // =================================================================================

    // NOTA: Estos métodos ahora serían llamados por los ActionListeners,
    // usando this.ordenActiva como parámetro.
    public OrdenDeTrabajo verOrdenAsignada() { return null; }
    public List<Repuesto> registrarDiagnostico(OrdenDeTrabajo orden, String descripcion) { return null; }
    public boolean marcarOrdenEnEspera(int idOrden) { return true; }
    public boolean registrarRepuestosUsados(OrdenDeTrabajo orden, List<Repuesto> repuestos) { return true; }
    public boolean finalizarReparacion(int idOrden) { return true; }
    public boolean registrarHorasTrabajo(int idOrden, double horas) { return true; }
    public boolean agregarObservaciones(int idOrden, String texto) { return true; }

    // =================================================================================
    // MAIN TEMPORAL PARA VER EL DISEÑO
    // =================================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VistaMecanico();
        });
    }
}