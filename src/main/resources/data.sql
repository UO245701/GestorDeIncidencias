--Datos para carga inicial de la base de datos
delete from persona;

INSERT INTO Persona (usuario, contrasena, tipo, nombre, apellidos, dni, email) VALUES 
('paco_vecino', '1234', 'CIUDADANO', 'Francisco', 'García López', '12345678A', 'paco@correo.com'),
('emma_vecino', '1234', 'CIUDADANO', 'Emma', 'Rueda Sanchez', '11112222T', 'emma@correo.com'),
('javi_vecino', '1234', 'CIUDADANO', 'Javier', 'Raya Gonzalez', '11223344J', 'javi@correo.com'),
('laura_op', 'admin', 'OPERADOR', 'Laura', 'Martínez Ruiz', '87654321B', 'laura@ayto.es'),
('rober_tech', 'tech123', 'TECNICO', 'Roberto', 'Sánchez Soler', '11111111R', 'roberto@tech.es'),
('pedro_tech', 'tech123', 'TECNICO', 'Pedro', 'Fernandez Perez', '22222222P', 'pedro@tech.es'),
('marta_tech', 'tech123', 'TECNICO', 'Marta', 'Valverde Martinez', '33333333M', 'marta@tech.es');

delete from incidencia;

-- 1. Incidencia recién creada por el ciudadano (ID 1)
INSERT INTO Incidencia (tipo, descripcion, localizacion, estado, fk_ciudadano) VALUES 
('Calzada', 'Socavón peligroso en mitad de la calle', 'Calle Mayor 15', 'ABIERTA', 1),
('Calzada', 'Socavón peligroso en mitad de la calle 2', 'Calle Mayor 15', 'NUEVA', 1),
('Calzada', 'Socavón peligroso en mitad de la calle 3', 'Calle Mayor 15', 'NUEVA', 1);

-- 2. Incidencia ya asignada a un técnico (ID 3) por el ciudadano (ID 1)
INSERT INTO Incidencia (tipo, descripcion, localizacion, estado, horas_estimadas, coste, fk_ciudadano, fk_tecnico) 
VALUES ('Alumbrado', 'Farola fundida hace una semana', 'Plaza España', 'ASIGNADA', 2.5, 45.0, 1, 3);

-- 3. Incidencia resuelta
INSERT INTO Incidencia (tipo, descripcion, localizacion, estado, horas_estimadas, coste, fk_ciudadano, fk_tecnico) 
VALUES ('Limpieza', 'Grafitis en la fachada del centro cultural', 'Av. Libertad s/n', 'RESUELTA', 4.0, 120.0, 1, 3);

-- Incidencias VALIDADA (las que se pueden asignar por el operador)
-- (fk_ciudadano = 1 -> paco_vecino)

INSERT INTO Incidencia 
(tipo, descripcion, localizacion, fecha_hora, estado, fk_ciudadano) VALUES
('Alumbrado', 'Farola parpadeando por la noche', 'Calle Luna 8', '2026-01-10 09:00:00', 'VALIDADA', 3),
('Limpieza', 'Contenedor desbordado y suciedad alrededor', 'Av. Constitución 22', '2026-01-14 12:00:00', 'VALIDADA', 2),
('Mobiliario urbano', 'Banco roto con tornillos sueltos', 'Parque Central', '2026-01-20 09:00:00', 'VALIDADA', 1),
('Zonas verdes', 'Árbol caído bloqueando el paso', 'Paseo del Río', '2026-02-10 17:00:00', 'VALIDADA', 2),
('Señalización', 'Señal de STOP doblada y casi caída', 'Cruce Av. Norte con Calle Sur', '2026-02-27 18:30:00', 'VALIDADA', 3),
('Calzada', 'Bache profundo en carril derecho', 'Ronda Oeste km 2', '2026-03-03 09:30:00', 'VALIDADA', 2);

 delete from historial;
 -- El Ciudadano crea la incidencia
INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) 
VALUES ('ABIERTA', 'CREACION', 'El ciudadano registró el aviso', 2, 1);

-- La Operadora (ID 2) la asigna al Técnico (ID 3)
INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) 
VALUES ('ASIGNADA', 'ASIGNACION', 'La operadora Laura asignó el trabajo a Roberto', 2, 2);

-- El historial de la incidencia resuelta (ID 3)
INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) 
VALUES ('RESUELTA', 'FINALIZACION', 'Trabajo de limpieza completado con éxito', 3, 3);