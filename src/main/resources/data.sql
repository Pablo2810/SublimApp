INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'admin@unlam.edu.ar', 'test', 'ADMIN', true);

INSERT INTO Tela(tipoTela, metros, color, precio, imagenUrl) VALUES('W15', 300.0, 'Rojo', 25000.0, 'img/TELA_1.jpg');
INSERT INTO Tela(tipoTela, metros, color, precio, imagenUrl) VALUES('SET', 180.0, 'Azul', 30000.0, 'img/TELA_2.jpg');

INSERT INTO TelaUsuario (fk_usuario, fk_tela, metrosDisponibles) VALUES (1, 2, 40.0);

INSERT INTO Prenda(descripcion, precioBase) VALUES ('Camiseta', '7000.0');
INSERT INTO Prenda(descripcion, precioBase) VALUES ('Short', '5500.0');

INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 1);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (1, 2);
INSERT INTO prenda_tela (prendas_id, telas_id) VALUES (2, 2);

INSERT INTO Talle (prenda, descripcion, metrosTotales) VALUES (1, 'S', 69);
INSERT INTO Talle (prenda, descripcion, metrosTotales) VALUES (1, 'L', 74);
INSERT INTO Talle (prenda, descripcion, metrosTotales) VALUES (1, '2XL', 79);


INSERT INTO Talle (prenda, descripcion, metrosTotales) VALUES (2, 'M', 46);
INSERT INTO Talle (prenda, descripcion, metrosTotales) VALUES (2, 'XL', 50);