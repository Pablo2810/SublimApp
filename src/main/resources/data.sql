-- Usuarios
INSERT INTO Usuario(email, password, rol, activo) VALUES ('admin@unlam.edu.ar', 'test', 'ADMIN', true);
INSERT INTO Usuario(email, password, rol, activo) VALUES ('pab@mail.com', 'pab', 'CLIENTE', true);

-- TELAS DE FÁBRICA (insertadas en la tabla Tela)

-- ALGODÓN
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('rojo', 'img/TELA_1.jpg', 100.0, 12000.0, 'ALGODON');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('azul', 'img/TELA_2.jpg', 100.0, 12000.0, 'ALGODON');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('rosa', 'img/TELA_3.jpg', 100.0, 12000.0, 'ALGODON');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('gris', 'img/TELA_4.jpg', 100.0, 12000.0, 'ALGODON');

-- LINO
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('azul', 'img/TELA_5.jpg', 100.0, 13000.0, 'LINO');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('celeste', 'img/TELA_6.jpg', 100.0, 13000.0, 'LINO');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('verde', 'img/TELA_7.jpg', 100.0, 13000.0, 'LINO');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('verde agua', 'img/TELA_8.jpg', 100.0, 13000.0, 'LINO');

-- W15
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('marron', 'img/TELA_9.jpg', 100.0, 10000.0, 'W15');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('naranja', 'img/TELA_10.jpg', 100.0, 10000.0, 'W15');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('negro', 'img/TELA_11.jpg', 100.0, 10000.0, 'W15');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('gris oscuro', 'img/TELA_12.jpg', 100.0, 10000.0, 'W15');

-- SET
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('amarillo', 'img/TELA_13.jpg', 100.0, 15000.0, 'SET');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('amarillo oscuro', 'img/TELA_14.jpg', 100.0, 15000.0, 'SET');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('rosa', 'img/TELA_15.jpg', 100.0, 15000.0, 'SET');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('verde agua', 'img/TELA_16.jpg', 100.0, 15000.0, 'SET');

-- NEOPRENO
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('lila', 'img/TELA_17.jpg', 100.0, 18000.0, 'NEOPRENO');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('rosa', 'img/TELA_18.jpg', 100.0, 18000.0, 'NEOPRENO');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('marron', 'img/TELA_19.jpg', 100.0, 18000.0, 'NEOPRENO');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('rosa oscuro', 'img/TELA_20.jpg', 100.0, 18000.0, 'NEOPRENO');

-- POLIESTER
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('celes', 'img/TELA_21.jpg', 100.0, 20000.0, 'POLIESTER');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('beige', 'img/TELA_22.jpg', 100.0, 20000.0, 'POLIESTER');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('marron', 'img/TELA_23.jpg', 100.0, 20000.0, 'POLIESTER');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('blanco', 'img/TELA_24.jpg', 100.0, 20000.0, 'POLIESTER');

/*
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES('Azul', 'img/TELA_1.jpg', 150.0, 30000.0, 'SET');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES('Rojo', 'img/TELA_2.jpg', 200.0, 20000.0, 'W15');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES('Negro', 'img/TELA_3.jpg', 90.0, 17000.0, 'LINO');

INSERT INTO TelaUsuario (id, fk_usuario) VALUES (3, 2);
*/

-- Telas del usuario (hereda de Tela: misma ID)
INSERT INTO TelaUsuario(id, fk_usuario) VALUES (3, 2);

-- Prendas
INSERT INTO Prenda(descripcion, precioBase) VALUES ('Camiseta', 7000.0);
INSERT INTO Prenda(descripcion, precioBase) VALUES ('Short', 5500.0);

-- Asociación ManyToMany entre Prenda y Tela
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 1);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 2);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (2, 2);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (2, 3);

-- Talles relacionados a Prenda
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('S', 69, 1);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('L', 74, 1);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('2XL', 79, 1);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('M', 46, 2);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('XL', 50, 2);

