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
INSERT INTO Prenda(id, descripcion, precioBase, imagenUrl) VALUES (1, 'Camiseta', 7000.0, "https://ik.imagekit.io/dwcllhcyp/prendas/remera_blanca.png");
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
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES (1, 'S', 68, 71, 76, 86, 91, 'Argentina');
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES (1, 'M', 70, 77, 82, 92, 97, 'Argentina');
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES (1, 'L', 72, 83, 88, 98, 103, 'Argentina');
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES (1, 'XL', 74, 89, 94, 104, 109, 'Argentina');
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES (1, '2XL', 76, 95, 100, 110, 115, 'Argentina');

INSERT INTO Talle(descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES ('P', 69, 70, 75, 88, 93, 'Brazil');
INSERT INTO Talle(descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES ('M', 71, 76, 81, 94, 99, 'Brazil');
INSERT INTO Talle(descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES ('G', 73, 82, 87, 100, 105, 'Brazil');
INSERT INTO Talle(descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES ('GG', 75, 88, 93, 106, 111, 'Brazil');
INSERT INTO Talle(descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES ('XGG', 77, 94, 100, 112, 117, 'Brazil');

INSERT INTO Talle(descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES ('38', 67, 70, 75, 88, 93, 'Spain');
INSERT INTO Talle(descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES ('40', 69, 76, 81, 94, 99, 'Spain');
INSERT INTO Talle(descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES ('42', 71, 82, 87, 100, 105, 'Spain');
INSERT INTO Talle(descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES ('44', 73, 88, 93, 106, 111, 'Spain');
INSERT INTO Talle(descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES ('46', 75, 94, 99, 112, 117, 'Spain');

INSERT INTO Talle(descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES ('S', 26.5, 28, 30, 34, 36, 'United States');
INSERT INTO Talle(descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES ('M', 27.5, 31, 33, 38, 40, 'United States');
INSERT INTO Talle(descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES ('L', 28.5, 34, 36, 42, 44, 'United States');
INSERT INTO Talle(descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES ('XL', 29.5, 37, 39, 46, 48, 'United States');
INSERT INTO Talle(descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX, pais)
VALUES ('XXL', 30.5, 40, 42, 50, 52, 'United States');


INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX)
VALUES (2, 'S', 68, 71, 76, 86, 91);
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX)
VALUES (2, 'M', 70, 77, 82, 92, 97);
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX)
VALUES (2, 'L', 72, 83, 88, 98, 103);
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX)
VALUES (2, 'XL', 74, 89, 94, 104, 109);
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX)
VALUES (2, 'XXL', 76, 95, 100, 110, 115);

INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX)
VALUES (3, 'S', 68, 71, 76, 86, 91);
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX)
VALUES (3, 'M', 70, 77, 82, 92, 97);
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX)
VALUES (3, 'L', 72, 83, 88, 98, 103);
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX)
VALUES (3, 'XL', 74, 89, 94, 104, 109);
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX)
VALUES (3, 'XXL', 76, 95, 100, 110, 115);

INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX)
VALUES (4, 'S', 68, 71, 76, 86, 91);
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX)
VALUES (4, 'M', 70, 77, 82, 92, 97);
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX)
VALUES (4, 'L', 72, 83, 88, 98, 103);
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX)
VALUES (4, 'XL', 74, 89, 94, 104, 109);
INSERT INTO Talle(prenda, descripcion, metrosTotales, cinturaMIN, cinturaMAX, pechoMIN, pechoMAX)
VALUES (4, 'XXL', 76, 95, 100, 110, 115);