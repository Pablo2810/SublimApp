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


-- Telas del usuario (hereda de Tela: misma ID)
INSERT INTO TelaUsuario(id, fk_usuario) VALUES (2, 2);--ALGODON AZUL
INSERT INTO TelaUsuario(id, fk_usuario) VALUES (6, 2);--LINO CELESTE
INSERT INTO TelaUsuario(id, fk_usuario) VALUES (11, 2);--W15 NEGRO
INSERT INTO TelaUsuario(id, fk_usuario) VALUES (13, 2);--SET AMARILLO

-- Prendas
INSERT INTO Prenda(id, descripcion, precioBase) VALUES (1, 'Camiseta', 7000.0);
INSERT INTO Prenda(id, descripcion, precioBase) VALUES (2, 'Short', 5500.0);
INSERT INTO Prenda(id, descripcion, precioBase) VALUES (3, 'Buzo', 9000.0);
INSERT INTO Prenda(id, descripcion, precioBase) VALUES (4, 'Campera', 12000.0);

-- Asociación ManyToMany entre Prenda y Tela
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 9);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 10);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 11);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 12);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 13);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 14);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 15);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 16);

INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (2, 9);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (2, 10);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (2, 11);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (2, 12);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (2, 17);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (2, 18);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (2, 19);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (2, 20);

INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (3, 1);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (3, 2);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (3, 3);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (3, 4);

INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (4, 1);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (4, 2);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (4, 3);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (4, 4);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (4, 5);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (4, 6);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (4, 7);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (4, 8);

-- Talles relacionados a Prenda
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('S', 68, 1);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('M', 70, 1);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('L', 72, 1);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('XL', 74, 1);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('2XL', 76, 1);

INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('S', 38, 2);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('M', 40, 2);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('L', 42, 2);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('XL', 44, 2);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('2XL', 46, 2);

INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('S', 68, 3);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('M', 70, 3);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('L', 72, 3);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('XL', 74, 3);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('2XL', 76, 3);

INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('S', 70, 4);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('M', 72, 4);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('L', 74, 4);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('XL', 76, 4);
INSERT INTO Talle(descripcion, metrosTotales, prenda) VALUES ('2XL', 78, 4);