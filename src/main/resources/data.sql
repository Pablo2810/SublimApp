-- Usuarios
INSERT INTO Usuario(id, email, password, rol, activo) VALUES (1, 'admin@unlam.edu.ar', 'test', 'ADMIN', true);
INSERT INTO Usuario(id, email, password, rol, activo) VALUES (2, 'pab@mail.com', 'pab', 'CLIENTE', true);

-- Telas base (comunes)
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (1, 'Azul', 'img/TELA_1.jpg', 150.0, 30000.0, 'SET');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (2, 'Rojo', 'img/TELA_2.jpg', 200.0, 20000.0, 'W15');
INSERT INTO Tela(id, color, imagenUrl, metros, precio, tipoTela) VALUES (3, 'Negro', 'img/TELA_3.jpg', 90.0, 17000.0, 'LINO');

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

