-- Usuarios
INSERT INTO Usuario(email, password, rol, activo) VALUES ('admin@unlam.edu.ar', 'test', 'ADMIN', true);
INSERT INTO Usuario(email, password, rol, activo) VALUES ('pab@mail.com', 'pab', 'CLIENTE', true);

-- TELAS DE FÁBRICA (insertadas en la tabla Tela)

-- ALGODÓN
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('rojo', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_1.jpg?updatedAt=1751405832681', 100.0, 12000.0, 'ALGODON');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('azul', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_2.jpg?updatedAt=1751405832806', 100.0, 12000.0, 'ALGODON');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('rosa', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_3.jpg?updatedAt=1751405832460', 100.0, 12000.0, 'ALGODON');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('gris', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_4.jpg?updatedAt=1751405832638', 100.0, 12000.0, 'ALGODON');

-- LINO
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('azul', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_5.jpg?updatedAt=1751405834509', 100.0, 13000.0, 'LINO');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('celeste', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_6.jpg?updatedAt=1751405834678', 100.0, 13000.0, 'LINO');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('verde', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_7.jpg?updatedAt=1751405834824', 100.0, 13000.0, 'LINO');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('verde agua', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_8.jpg?updatedAt=1751405834570', 100.0, 13000.0, 'LINO');

-- W15
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('marron', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_9.jpg?updatedAt=1751405830143', 100.0, 10000.0, 'W15');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('naranja', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_10.jpg?updatedAt=1751405830086', 100.0, 10000.0, 'W15');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('negro', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_11.jpg?updatedAt=1751405829956', 100.0, 10000.0, 'W15');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('gris oscuro', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_12.jpg?updatedAt=1751405829881', 100.0, 10000.0, 'W15');

-- SET
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('amarillo', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_13.jpg?updatedAt=1751405829924', 100.0, 15000.0, 'SET');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('amarillo oscuro', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_14.jpg?updatedAt=1751405830058', 100.0, 15000.0, 'SET');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('rosa', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_15.jpg?updatedAt=1751405829951', 100.0, 15000.0, 'SET');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('verde agua', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_16.jpg?updatedAt=1751405830061', 100.0, 15000.0, 'SET');

-- NEOPRENO
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('lila', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_17.jpg?updatedAt=1751405830137', 100.0, 18000.0, 'NEOPRENO');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('rosa', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_18.jpg?updatedAt=1751405830342', 100.0, 18000.0, 'NEOPRENO');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('marron', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_19.jpg?updatedAt=1751405831897', 100.0, 18000.0, 'NEOPRENO');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('rosa oscuro', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_20.jpg?updatedAt=1751405832107', 100.0, 18000.0, 'NEOPRENO');

-- POLIESTER
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('celes', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_21.jpg?updatedAt=1751405832340', 100.0, 20000.0, 'POLIESTER');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('beige', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_22.jpg?updatedAt=1751405832270', 100.0, 20000.0, 'POLIESTER');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('marron', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_23.jpg?updatedAt=1751405832402', 100.0, 20000.0, 'POLIESTER');
INSERT INTO Tela(color, imagenUrl, metros, precio, tipoTela) VALUES ('blanco', 'https://ik.imagekit.io/dwcllhcyp/telas/TELA_24.jpg?updatedAt=1751405832481', 100.0, 20000.0, 'POLIESTER');


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