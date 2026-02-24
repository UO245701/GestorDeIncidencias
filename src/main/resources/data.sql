--Datos para carga inicial de la base de datos

--Para giis.demo.tkrun:
delete from carreras;
insert into carreras(id,inicio,fin,fecha,descr) values 
	(100,'2016-10-05','2016-10-25','2016-11-09','finalizada'),
	(101,'2016-10-05','2016-10-25','2016-11-10','en fase 3'),
	(102,'2016-11-05','2016-11-09','2016-11-20','en fase 2'),
	(103,'2016-11-10','2016-11-15','2016-11-21','en fase 1'),
	(104,'2016-11-11','2016-11-15','2016-11-22','antes inscripcion');

delete from persona;

INSERT INTO Persona (usuario, contrasena, tipo, nombre, apellidos, dni) VALUES 
('paco_vecino', '1234', 'CIUDADANO', 'Francisco', 'García López', '12345678A'),
('laura_op', 'admin', 'OPERADOR', 'Laura', 'Martínez Ruiz', '87654321B'),
('rober_tech', 'tech123', 'TECNICO', 'Roberto', 'Sánchez Soler', '11223344C');

delete from incidencia;

-- 1. Incidencia recién creada por el ciudadano (ID 1)
INSERT INTO Incidencia (tipo, descripcion, localizacion, estado, fk_ciudadano) 
VALUES ('Bache', 'Socavón peligroso en mitad de la calle', 'Calle Mayor 15', 'ABIERTA', 1);

-- 2. Incidencia ya asignada a un técnico (ID 3) por el ciudadano (ID 1)
INSERT INTO Incidencia (tipo, descripcion, localizacion, estado, horas_estimadas, coste, fk_ciudadano, fk_tecnico) 
VALUES ('Farola', 'Farola fundida hace una semana', 'Plaza España', 'ASIGNADA', 2.5, 45.0, 1, 3);

-- 3. Incidencia resuelta
INSERT INTO Incidencia (tipo, descripcion, localizacion, estado, horas_estimadas, coste, fk_ciudadano, fk_tecnico) 
VALUES ('Limpieza', 'Grafitis en la fachada del centro cultural', 'Av. Libertad s/n', 'RESUELTA', 4.0, 120.0, 1, 3);

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