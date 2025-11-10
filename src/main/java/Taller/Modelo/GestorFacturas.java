package Taller.Modelo;

import java.sql.*;

public class GestorFacturas {

    private static final String connectionUrl = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=TallerMecanico;encrypt=true;trustServerCertificate=true;";
    private static final String user = "taller_user";
    private static final String password = "12345678";

    public Factura obtenerFacturaPorIdOrden(int idOrdenDeTrabajo) {
        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {
            try {
                PreparedStatement ps = connection.prepareStatement("SELECT TOP 1 * FROM Factura WHERE idOrdenDeTrabajo = ?;");
                ps.setInt(1, idOrdenDeTrabajo);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return new Factura(
                            rs.getInt("idFactura"),
                            rs.getDate("fechaEmision"),
                            rs.getInt("totalFactura"),
                            rs.getString("estadoFactura")
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

    public Factura generarFactura(int idOrdenDeTrabajo) {
        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {
            try {
                PreparedStatement ps = connection.prepareStatement("""
                INSERT INTO Factura (fechaEmision, totalFactura, estadoFactura, idOrdenDeTrabajo)
                SELECT 
                    GETDATE() AS fechaEmision, 
                    (OT.horasTrabajo * E.costoHora) AS totalFactura, 
                    'Pendiente' AS estadoFactura, 
                    OT.idOrdenDeTrabajo
                FROM OrdenDeTrabajo OT
                JOIN Empleado E ON OT.legajoMecanicoAsignado = E.legajo
                WHERE OT.idOrdenDeTrabajo = ?;
                """);
                ps.setInt(1, idOrdenDeTrabajo);

                if (ps.executeUpdate() > 0) {
                    return obtenerFacturaPorIdOrden(idOrdenDeTrabajo);
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

    public boolean registrarPagoFactura(int idFactura) {
        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {
            try {
                PreparedStatement ps = connection.prepareStatement("UPDATE Factura SET estadoFactura = 'Pagada' WHERE idFactura = ?;");
                ps.setInt(1, idFactura);

                return ps.executeUpdate() > 0;
            } catch (SQLException ex) {
                System.err.println("Error en ejecucion de la sentencia SQL: " + ex.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error conectando a la BDD: " + e.getMessage());
            return false;
        }
    }
}
