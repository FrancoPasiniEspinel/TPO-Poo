USE master;

IF NOT EXISTS (SELECT 1 FROM sys.server_principals WHERE name = 'taller_user')
BEGIN
    CREATE LOGIN taller_user 
    WITH PASSWORD = '12345678', 
         CHECK_POLICY = OFF,
         CHECK_EXPIRATION = OFF;

    ALTER SERVER ROLE sysadmin ADD MEMBER taller_user;
END

IF EXISTS (SELECT 1 FROM sys.databases WHERE name = 'TallerMecanico')
BEGIN
    DROP DATABASE TallerMecanico;
END
CREATE DATABASE TallerMecanico;
USE TallerMecanico;

IF NOT EXISTS (SELECT 1 FROM sys.tables WHERE name = 'Empleado')
BEGIN
CREATE TABLE Empleado (
	legajo INT PRIMARY KEY IDENTITY(1,1),
	contraseña VARCHAR(20) NOT NULL,
	tipo VARCHAR(20) NOT NULL,
	nombre VARCHAR(20) NOT NULL,
	apellido VARCHAR(20) NOT NULL,
	costoHora Decimal(8,2) NOT NULL
);
END


INSERT INTO Empleado (contraseña, tipo, nombre, apellido, costoHora) 
VALUES
('123', 'Mecanico', 'Juan', 'Perez', 10.0),
('123', 'Mecanico', 'Ernesto', 'Rodriguez', 10.0),
('123', 'Recepcionista', 'Pedro', 'Martinez', 8.0),
('123', 'Administrativo', 'Ramiro', 'Lopez', 12.0)
GO

SELECT * FROM Empleado;

IF NOT EXISTS (SELECT 1 FROM sys.tables WHERE name = 'Vehiculo')
BEGIN
CREATE TABLE Vehiculo(
	patenteVehiculo VARCHAR(8) PRIMARY KEY,
	marcaVehiculo VARCHAR(20) NOT NULL,
	modeloVehiculo VARCHAR(20) NOT NULL,
	añoFabricacionVehiculo INT NOT NULL
);
END

IF NOT EXISTS (SELECT 1 FROM sys.tables WHERE name = 'Repuesto')
BEGIN
CREATE TABLE Repuesto (
	codigoRepuesto INT PRIMARY KEY IDENTITY(10000,1),
	nombreRespuesto VARCHAR(50) NOT NULL,
	precioUnitario DECIMAL(9,2) NOT NULL,
	stock INT
);
END

IF NOT EXISTS (SELECT 1 FROM sys.tables WHERE name = 'Cliente')
BEGIN
CREATE TABLE Cliente(
	idCliente INT PRIMARY KEY IDENTITY(1,1),
	dniCliente INT NOT NULL,
	nombreCliente VARCHAR(50),
	telefonoCliente INT
);
END

SELECT * FROM Cliente;

IF NOT EXISTS (SELECT 1 FROM sys.tables WHERE name = 'OrdenDeTrabajo')
BEGIN
CREATE TABLE OrdenDeTrabajo (
	idOrdenDeTrabajo INT PRIMARY KEY IDENTITY(1,1),
	estado VARCHAR(20) NOT NULL,
	fechaCreacion DATE NOT NULL,
	patenteVehiculo VARCHAR(8) NOT NULL,
	legajoMecanicoAsignado INT,
	informeTecnico VARCHAR(1000),
	diagnostico VARCHAR(500),
	idCliente INT,
	horasTrabajo INT,
	FOREIGN KEY (patenteVehiculo) REFERENCES Vehiculo(patenteVehiculo),
	FOREIGN KEY (idCliente) REFERENCES Cliente(idCLiente)
);
END

SELECT * FROM OrdenDeTrabajo;

IF NOT EXISTS (SELECT 1 FROM sys.tables WHERE name = 'ItemRepuesto')
BEGIN
CREATE TABLE ItemRepuesto(
	idOrdenDeTrabajo INT,
	codigoRepuesto INT,
	PRIMARY KEY(idOrdenDeTrabajo, codigoRepuesto),
	cantidad INT,
	FOREIGN KEY (idOrdenDeTrabajo) REFERENCES OrdenDeTrabajo(idOrdenDeTrabajo),
	FOREIGN KEY (codigoRepuesto) REFERENCES Repuesto(codigoRepuesto)
);
END

IF NOT EXISTS (SELECT 1 FROM sys.tables WHERE name = 'Factura')
BEGIN
CREATE TABLE Factura(
	idFactura INT PRIMARY KEY IDENTITY(1,1),
	fechaEmision DATE,
	totalFactura INT,
	estadoFactura VARCHAR(20),
	idOrdenDeTrabajo INT,
	FOREIGN KEY (idOrdenDeTrabajo) REFERENCES OrdenDeTrabajo(idOrdenDeTrabajo)
);
END

SELECT 1 FROM Vehiculo WHERE patente = 1;