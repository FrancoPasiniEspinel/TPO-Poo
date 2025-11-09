package Taller.Modelo;

import java.sql.*;
import java.time.LocalDate;

public class GestorOrdenes {

    private static final String connectionUrl = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=TallerMecanico;encrypt=true;trustServerCertificate=true;";
    private static final String user = "taller_user";
    private static final String password = "12345678";

    public GestorOrdenes() {}

    public boolean generarOrden(int dni, String nombre, int telefono, String patente, String marca, String modelo, int añoFabricacion, String descripcion) {
        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {
            connection.setAutoCommit(false);
            PreparedStatement ps;
            int filas;

            try {
                ps = connection.prepareStatement("INSERT INTO Cliente (dniCliente, nombreCliente, telefonoCliente) VALUES (?, ?, ?)");
                ps.setInt(1, dni);
                ps.setString(2, nombre);
                ps.setInt(3, telefono);

                filas = ps.executeUpdate();
                if (filas == 0) { return false; }

                ps = connection.prepareStatement("INSERT INTO Vehiculo (patenteVehiculo, marcaVehiculo, modeloVehiculo, añoFabricacionvehiculo) VALUES (?, ?, ?, ?)");
                ps.setString(1, patente);
                ps.setString(2, marca);
                ps.setString(3, modelo);
                ps.setInt(4, añoFabricacion);

                filas = ps.executeUpdate();
                if (filas == 0) { return false; }

                ps = connection.prepareStatement(
                        "INSERT INTO OrdenDeTrabajo (estado, fechaCreacion, patenteVehiculo, mecanicoAsignado, diagnostico, idCliente)" +
                        " VALUES ('Pendiente', ?, ?, null, ?, (SELECT TOP 1 idCliente FROM Cliente WHERE dniCliente = ?))");
                ps.setDate(1, Date.valueOf(LocalDate.now()));
                ps.setString(2, patente);
                ps.setString(3, descripcion);
                ps.setInt(4, dni);

                filas = ps.executeUpdate();
                if (filas == 0) { return false; }

                connection.commit();
                return true;
            } catch (SQLException ex) {
                System.err.println("Error en ejecucion de la sentencia SQL: " + ex.getMessage());
                connection.rollback();
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error conectando a la BDD: " + e.getMessage());
            return false;
        }
    }

    public OrdenDeTrabajo buscarOrdenPorPatente(String patente) {
        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {
            try {
                PreparedStatement ps = connection.prepareStatement("""
                                SELECT o.idOrdenDeTrabajo, o.estado, o.fechaCreacion, o.mecanicoAsignado, o.informeTecnico, o.diagnostico,
                                    c.idCliente, c.dniCliente, c.nombreCliente, c.telefonoCliente,
                                    v.patenteVehiculo, v.marcaVehiculo, v.modeloVehiculo, v.añoFabricacionVehiculo
                                FROM OrdenDeTrabajo o
                                JOIN Cliente c ON o.idCliente = c.idCliente
                                JOIN Vehiculo v ON o.patenteVehiculo = v.patenteVehiculo
                                WHERE v.patenteVehiculo = ?;
                """);
                ps.setString(1, patente);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new OrdenDeTrabajo(
                            rs.getInt("idOrdenDeTrabajo"),
                            rs.getString("estado"),
                            rs.getDate("fechaCreacion"),
                            new Vehiculo(
                                    rs.getString("patenteVehiculo"),
                                    rs.getString("marcaVehiculo"),
                                    rs.getString("modeloVehiculo"),
                                    rs.getInt("añoFabricacionVehiculo")
                            ),
                            null,
                            new Cliente(
                                    rs.getString("nombreCliente"),
                                    rs.getInt("dniCliente"),
                                    rs.getInt("telefonoCliente")
                                    ),
                            rs.getString("diagnostico")
                    );
                }
                return null;
            } catch (SQLException ex) {
                System.err.println("Error en ejecucion de la sentencia SQL: " + ex.getMessage());
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error conectando a la BDD: " + e.getMessage());
            return null;
        }
    }

    public boolean registrarEntregaVehiculo(int idOrdenDeTrabajo) {
        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {
            try{
                PreparedStatement ps = connection.prepareStatement("UPDATE OrdenDeTrabajo SET estado = 'Finalizado' WHERE idOrdenDeTrabajo = ?;");
                ps.setInt(1, idOrdenDeTrabajo);

                if (ps.executeUpdate()>0){
                    return true;
                }
                return false;
            } catch (SQLException ex){
                System.err.println("Error en ejecucion de la sentencia SQL: " + ex.getMessage());
                return false;
            }
        }
        catch (SQLException e) {
            System.err.println("Error conectando a la BDD: " + e.getMessage());
            return false;
        }
    }
}
