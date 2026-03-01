--Datos para carga inicial de la base de datos
delete from persona;

INSERT INTO Persona (usuario, contrasena, tipo, nombre, apellidos, dni, email) VALUES 
('paco_vecino', '1234', 'CIUDADANO', 'Francisco', 'García López', '12345678A', 'paco@correo.com'),
('laura_op', 'admin', 'OPERADOR', 'Laura', 'Martínez Ruiz', '87654321B', 'laura@ayto.es'),
('rober_tech', 'tech123', 'TECNICO', 'Roberto', 'Sánchez Soler', '11223344C', 'roberto@tech.es');

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

INSERT INTO Incidencia (tipo, descripcion, localizacion, estado, fk_ciudadano) VALUES
('Alumbrado', 'Farola parpadeando por la noche', 'Calle Luna 8', 'VALIDADA', 1),
('Limpieza', 'Contenedor desbordado y suciedad alrededor', 'Av. Constitución 22', 'VALIDADA', 1),
('Mobiliario urbano', 'Banco roto con tornillos sueltos', 'Parque Central', 'VALIDADA', 1),
('Zonas verdes', 'Árbol caído bloqueando el paso', 'Paseo del Río', 'VALIDADA', 1),
('Señalización', 'Señal de STOP doblada y casi caída', 'Cruce Av. Norte con Calle Sur', 'VALIDADA', 1),
('Calzada', 'Bache profundo en carril derecho', 'Ronda Oeste km 2', 'VALIDADA', 1);

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