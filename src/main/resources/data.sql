-- Usuarios
INSERT INTO Usuario(id, email, password, rol, activo) VALUES (1, 'admin@unlam.edu.ar', 'test', 'ADMIN', true);
INSERT INTO Usuario(id, email, password, rol, activo) VALUES (2, 'pab@mail.com', 'pab', 'CLIENTE', true);

-- TELAS DE FÁBRICA (insertadas en la tabla Tela)

-- ALGODÓN
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (101, 'rojo', 'img/TELA_1.jpg', 100.0, 12000.0, 'ALGODON');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (102, 'azul', 'img/TELA_2.jpg', 100.0, 12000.0, 'ALGODON');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (103, 'rosa', 'img/TELA_3.jpg', 100.0, 12000.0, 'ALGODON');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (104, 'gris', 'img/TELA_4.jpg', 100.0, 12000.0, 'ALGODON');

-- LINO
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (105, 'azul', 'img/TELA_5.jpg', 100.0, 13000.0, 'LINO');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (106, 'celeste', 'img/TELA_6.jpg', 100.0, 13000.0, 'LINO');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (107, 'verde', 'img/TELA_7.jpg', 100.0, 13000.0, 'LINO');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (108, 'verde agua', 'img/TELA_8.jpg', 100.0, 13000.0, 'LINO');

-- W15
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (109, 'marron', 'img/TELA_9.jpg', 100.0, 10000.0, 'W15');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (110, 'naranja', 'img/TELA_10.jpg', 100.0, 10000.0, 'W15');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (111, 'negro', 'img/TELA_11.jpg', 100.0, 10000.0, 'W15');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (112, 'gris oscuro', 'img/TELA_12.jpg', 100.0, 10000.0, 'W15');

-- SET
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (113, 'amarillo', 'img/TELA_13.jpg', 100.0, 15000.0, 'SET');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (114, 'amarillo oscuro', 'img/TELA_14.jpg', 100.0, 15000.0, 'SET');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (115, 'rosa', 'img/TELA_15.jpg', 100.0, 15000.0, 'SET');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (116, 'verde agua', 'img/TELA_16.jpg', 100.0, 15000.0, 'SET');

-- NEOPRENO
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (117, 'lila', 'img/TELA_17.jpg', 100.0, 18000.0, 'NEOPRENO');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (118, 'rosa', 'img/TELA_18.jpg', 100.0, 18000.0, 'NEOPRENO');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (119, 'marron', 'img/TELA_19.jpg', 100.0, 18000.0, 'NEOPRENO');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (120, 'rosa oscuro', 'img/TELA_20.jpg', 100.0, 18000.0, 'NEOPRENO');

-- POLIESTER
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (121, 'celes', 'img/TELA_21.jpg', 100.0, 20000.0, 'POLIESTER');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (122, 'beige', 'img/TELA_22.jpg', 100.0, 20000.0, 'POLIESTER');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (123, 'marron', 'img/TELA_23.jpg', 100.0, 20000.0, 'POLIESTER');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (124, 'blanco', 'img/TELA_24.jpg', 100.0, 20000.0, 'POLIESTER');

/*
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES('Azul', 'img/TELA_1.jpg', 150.0, 30000.0, 'SET');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES('Rojo', 'img/TELA_2.jpg', 200.0, 20000.0, 'W15');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES('Negro', 'img/TELA_3.jpg', 90.0, 17000.0, 'LINO');

INSERT INTO TelaUsuario (id, fk_usuario) VALUES (3, 2);
*/

-- Telas del usuario (hereda de Tela: misma ID)
INSERT INTO TelaUsuario(id, fk_usuario) VALUES (3, 2);

-- Prendas
INSERT INTO Prenda(id, descripcion, precioBase) VALUES (1, 'Camiseta', 7000.0);
INSERT INTO Prenda(id, descripcion, precioBase) VALUES (2, 'Short', 5500.0);

-- Asociación ManyToMany entre Prenda y Tela
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 1);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 2);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (2, 2);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (2, 3);

-- Talles relacionados a Prenda
INSERT INTO Talle(id, descripcion, metrosTotales, prenda) VALUES (1, 'S', 69, 1);
INSERT INTO Talle(id, descripcion, metrosTotales, prenda) VALUES (2, 'L', 74, 1);
INSERT INTO Talle(id, descripcion, metrosTotales, prenda) VALUES (3, '2XL', 79, 1);
INSERT INTO Talle(id, descripcion, metrosTotales, prenda) VALUES (4, 'M', 46, 2);
INSERT INTO Talle(id, descripcion, metrosTotales, prenda) VALUES (5, 'XL', 50, 2);

