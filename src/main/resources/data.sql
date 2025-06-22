INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'admin@unlam.edu.ar', 'test', 'ADMIN', true);
INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'pab@mail.com', 'pab', 'CLIENTE', true);

INSERT INTO Tela(DTYPE, color, imagenUrl, metros, precio, tipoTela) VALUES('Tela', 'Azul', 'img/TELA_1.jpg', 150.0, 30000.0, 'SET');
INSERT INTO Tela(DTYPE, color, imagenUrl, metros, precio, tipoTela) VALUES('Tela', 'Rojo', 'img/TELA_2.jpg', 200.0, 20000.0, 'W15');
INSERT INTO Tela(DTYPE, color, imagenUrl, metros, precio, tipoTela, fk_usuario) VALUES('TelaUsuario', 'Negro', 'img/TELA_3.jpg', 90.0, 17000.0, 'LINO', 2);

INSERT INTO Prenda(descripcion, precioBase) VALUES ('Camiseta', '7000.0');
INSERT INTO Prenda(descripcion, precioBase) VALUES ('Short', '5500.0');

INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 1);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 2);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (2, 2);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (2, 3);

INSERT INTO Talle (descripcion, metrosTotales, prenda) VALUES ('S', 69, 1);
INSERT INTO Talle (descripcion, metrosTotales, prenda) VALUES ('L', 74, 1);
INSERT INTO Talle (descripcion, metrosTotales, prenda) VALUES ('2XL', 79, 1);
INSERT INTO Talle (descripcion, metrosTotales, prenda) VALUES ('M', 46, 2);
INSERT INTO Talle (descripcion, metrosTotales, prenda) VALUES ('XL', 50, 2);