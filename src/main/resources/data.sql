INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'admin@unlam.edu.ar', 'test', 'ADMIN', true);

INSERT INTO Prenda(descripcion, precioBase) VALUES ('Remera', '100.0');
INSERT INTO Prenda(descripcion, precioBase) VALUES ('Campera', '200.0');
INSERT INTO Prenda(descripcion, precioBase) VALUES ('Buzo', '150.0');
INSERT INTO Prenda(descripcion, precioBase) VALUES ('Musculosa', '80.0');

INSERT INTO Talle (descripcion, metrosTotales) VALUES ('S', 69);
INSERT INTO Talle (descripcion, metrosTotales) VALUES ('M', 71);
INSERT INTO Talle (descripcion, metrosTotales) VALUES ('L', 74);
INSERT INTO Talle (descripcion, metrosTotales) VALUES ('XL', 77);
INSERT INTO Talle (descripcion, metrosTotales) VALUES ('2XL', 79);
/*
  CREATE TABLE Usuario (
    id BIGINT PRIMARY KEY,
    rol VARCHAR(50),
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    telefono BIGINT,
    email VARCHAR(150) UNIQUE,
    contrasenia VARCHAR(255),
    nivelPromocional INTEGER,
    metrosTotalesHistoricos DOUBLE,
    frecuenciaPedidos DOUBLE
);

CREATE TABLE Promocion (
    id BIGINT PRIMARY KEY,
    descripcion TEXT,
    tipo VARCHAR(50),
    nivelPromocion INTEGER
);

CREATE TABLE Pedido (
    id BIGINT PRIMARY KEY,
    fechaCreacion DATE,
    estado VARCHAR(50),
    cantidadCopias INTEGER,
    metros_totales DOUBLE,
    costoServicio DOUBLE,
    usuario_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);

CREATE TABLE Archivo (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(150),
    tipo VARCHAR(50),
    peso DOUBLE,
    pedido_id BIGINT,
    FOREIGN KEY (pedido_id) REFERENCES Pedido(id)
);

CREATE TABLE Tela (
    id BIGINT PRIMARY KEY,
    tipoTela VARCHAR(50),
    metros DOUBLE,
    archivo_id BIGINT,
    FOREIGN KEY (archivo_id) REFERENCES Archivo(id)
);

CREATE TABLE Simulacion (
    id BIGINT PRIMARY KEY,
    tiempoEstimadoTotal DOUBLE,
    tiempoRipeo DOUBLE,
    tiempoImpresion DOUBLE,
    requiereClean BOOLEAN,
    fechaSimulacion DATE,
    archivo_id BIGINT,
    FOREIGN KEY (archivo_id) REFERENCES Archivo(id)
);

CREATE TABLE Maquina (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(100),
    velocidadImpresion DOUBLE,
    requiereClean BOOLEAN,
    tiempoUltimaLimpieza TIMESTAMP,
    activo BOOLEAN
);
*/

