package Taller.Modelo;

import java.sql.*;
import java.time.LocalDate;

public class GestorOrdenes {

    private static final String connectionUrl = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=TallerMecanico;encrypt=true;trustServerCertificate=true;";
    private static final String user = "taller_user";
    private static final String password = "12345678";

    public GestorOrdenes() {
    }

    public String generarOrden(int dni, String nombre, int telefono, String patente, String marca, String modelo, int añoFabricacion, String descripcion) {
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
                if (filas == 0) {
                    return "fallo";
                }


                ps = connection.prepareStatement("INSERT INTO Vehiculo (patenteVehiculo, marcaVehiculo, modeloVehiculo, añoFabricacionvehiculo) VALUES (?, ?, ?, ?)");
                ps.setString(1, patente);
                ps.setString(2, marca);
                ps.setString(3, modelo);
                ps.setInt(4, añoFabricacion);

                filas = ps.executeUpdate();
                if (filas == 0) {
                    return "fallo";
                }

                ps = connection.prepareStatement(
                        "INSERT INTO OrdenDeTrabajo (estado, fechaCreacion, patenteVehiculo, legajoMecanicoAsignado, diagnostico, idCliente)" +
                                " VALUES ('Pendiente', ?, ?, null, ?, (SELECT TOP 1 idCliente FROM Cliente WHERE dniCliente = ?))");
                ps.setDate(1, Date.valueOf(LocalDate.now()));
                ps.setString(2, patente);
                ps.setString(3, descripcion);
                ps.setInt(4, dni);

                filas = ps.executeUpdate();
                if (filas == 0) {
                    return "fallo";
                }

                connection.commit();
                return "exito";
            } catch (SQLException ex) {
                if (ex.getErrorCode() == 2627 || ex.getErrorCode() == 2601) {
                    System.err.println("Error en ejecucion de la sentencia SQL: " + ex.getMessage());
                    connection.rollback();
                    return "duplicado";
                } else {
                    System.err.println("Error en ejecucion de la sentencia SQL: " + ex.getMessage());
                    connection.rollback();
                    return "fallo";
                }
            }

        } catch (SQLException e) {
            System.err.println("Error conectando a la BDD: " + e.getMessage());
            return "fallo";
        }
    }

    public OrdenDeTrabajo buscarOrdenPorPatente(String patente) {
        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {
            try {
                PreparedStatement ps = connection.prepareStatement("""
                                        SELECT o.idOrdenDeTrabajo, o.estado, o.fechaCreacion, o.legajoMecanicoAsignado, o.informeTecnico, o.diagnostico, o.horasTrabajo,
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
                            rs.getString("diagnostico"),
                            rs.getString("informeTecnico"),
                            rs.getInt("horasTrabajo")
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

    public boolean registrarEntregaOrden(int idOrdenDeTrabajo) {
        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {
            try {
                PreparedStatement ps = connection.prepareStatement("UPDATE OrdenDeTrabajo SET estado = 'Finalizado' WHERE idOrdenDeTrabajo = ?;");
                ps.setInt(1, idOrdenDeTrabajo);

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

    public OrdenDeTrabajo obtenerOrdenMecanico(int legajo) {
        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {
            try {
                PreparedStatement ps = connection.prepareStatement("SELECT TOP 1 * FROM ordenDeTrabajo WHERE legajoMecanicoAsignado = ? AND estado = 'Pendiente' ORDER BY fechaCreacion;");
                ps.setInt(1, legajo);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return this.buscarOrdenPorPatente(rs.getString("patenteVehiculo"));
                } else {
                    return null;
                }
            } catch (SQLException ex) {
                System.err.println("Error en ejecucion de la sentencia SQL: " + ex.getMessage());
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error conectando a la BDD: " + e.getMessage());
            return null;
        }
    }

    public OrdenDeTrabajo asignarOrdenPrioritaria(int legajo) {
        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {
            try {
                connection.setAutoCommit(false);
                PreparedStatement ps = connection.prepareStatement("SELECT TOP 1 * FROM ordenDeTrabajo WHERE estado = 'Pendiente' ORDER BY fechaCreacion");
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    return null;
                }

                ps = connection.prepareStatement("UPDATE OrdenDeTrabajo SET legajoMecanicoAsignado = ?, estado = 'En_Reparacion' WHERE idOrdenDeTrabajo = ?;");
                ps.setInt(1, legajo);
                ps.setInt(2, rs.getInt("idOrdenDeTrabajo"));

                if (ps.executeUpdate() > 0) {
                    connection.commit();
                    return this.buscarOrdenPorPatente(rs.getString("patenteVehiculo"));
                }
                connection.rollback();
                return null;
            } catch (SQLException ex) {
                System.err.println("Error en ejecucion de la sentencia SQL: " + ex.getMessage());
                connection.rollback();
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error conectando a la BDD: " + e.getMessage());
            return null;
        }
    }

    public boolean modificarInformeTecnico(int idOrdenDeTrabajo, String detalle) {
        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {
            try {
                PreparedStatement ps = connection.prepareStatement("UPDATE OrdenDeTrabajo SET informeTecnico = ? WHERE idOrdenDeTrabajo = ?;");
                ps.setString(1, detalle);
                ps.setInt(2, idOrdenDeTrabajo);

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

    public boolean actualizarHorasOrden(int idOrdenDeTrabajo, int horasTrabajo) {
        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {
            try {
                PreparedStatement ps = connection.prepareStatement("UPDATE OrdenDeTrabajo SET horasTrabajo = ? WHERE idOrdenDeTrabajo = ?;");
                ps.setInt(1, horasTrabajo);
                ps.setInt(2, idOrdenDeTrabajo);

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

    public boolean registrarReparacionOrden(int idOrdenDeTrabajo) {
        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {
            try {
                PreparedStatement ps = connection.prepareStatement("UPDATE OrdenDeTrabajo SET estado = 'Reparado' WHERE idOrdenDeTrabajo = ?;");
                ps.setInt(1, idOrdenDeTrabajo);

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

    public boolean registrarPagoOrden(int idOrdenDeTrabajo) {
        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {
            try {
                PreparedStatement ps = connection.prepareStatement("UPDATE OrdenDeTrabajo SET estado = 'Pagado' WHERE idOrdenDeTrabajo = ?;");
                ps.setInt(1, idOrdenDeTrabajo);

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
