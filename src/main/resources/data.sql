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


delete from zona;

INSERT INTO Zona (nombre) VALUES ('Norte');
INSERT INTO Zona (nombre) VALUES ('Sur');
INSERT INTO Zona (nombre) VALUES ('Este');
INSERT INTO Zona (nombre) VALUES ('Oeste');

delete from incidencia;

-- 1. Incidencia recién creada por el ciudadano (ID 1)
INSERT INTO Incidencia (tipo, descripcion, fk_zona, estado, fk_ciudadano) VALUES 
('Calzada', 'Socavón peligroso en mitad de la calle', 1, 'NUEVA', 1),
('Calzada', 'Socavón peligroso en mitad de la calle 2', 1, 'NUEVA', 1),
('Calzada', 'Socavón peligroso en mitad de la calle 3', 1, 'NUEVA', 1);

-- 2. Incidencia ya asignada a un técnico (ID 3) por el ciudadano (ID 1)
INSERT INTO Incidencia (tipo, descripcion, fk_zona, estado, coste, fk_ciudadano, fk_tecnico) 
VALUES ('Alumbrado', 'Farola fundida hace una semana', 2, 'ASIGNADA', 45.0, 1, 5);

-- 3. Incidencia resuelta
INSERT INTO Incidencia (tipo, descripcion, estado, fk_ciudadano, fk_tecnico, fk_zona, coste, horas_prevision, trabajos_reparacion, tiempo_real, trabajos_realizados) 
VALUES ('Limpieza', 'Grafitis en la fachada del centro cultural', 'RESUELTA', 1, 5, 2, 120.0, 5, 'Limpiar y pintar', 7, 'Pintarlas paredes de nuevo');

-- Incidencias VALIDADA (las que se pueden asignar por el operador)
-- (fk_ciudadano = 1 -> paco_vecino)

INSERT INTO Incidencia 
(tipo, descripcion, fk_zona, fecha_hora, estado, fk_ciudadano) VALUES
('Alumbrado', 'Farola parpadeando por la noche', 4, '2026-01-10 09:00:00', 'VALIDADA', 3),
('Limpieza', 'Contenedor desbordado y suciedad alrededor', 2, '2026-01-14 12:00:00', 'VALIDADA', 2),
('Mobiliario urbano', 'Banco roto con tornillos sueltos', 3, '2026-01-20 09:00:00', 'VALIDADA', 1),
('Zonas verdes', 'Árbol caído bloqueando el paso', 4, '2026-02-10 17:00:00', 'VALIDADA', 2),
('Señalización', 'Señal de STOP doblada y casi caída', 4, '2026-02-27 18:30:00', 'VALIDADA', 3),
('Calzada', 'Bache profundo en carril derecho', 3, '2026-03-03 09:30:00', 'VALIDADA', 2);

 delete from historial;
 -- El Ciudadano crea la incidencia
INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) 
VALUES ('NUEVA', 'CREACION', 'El ciudadano registró el aviso', 2, 1);

-- La Operadora (ID 2) la asigna al Técnico (ID 3)
INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) 
VALUES ('ASIGNADA', 'ASIGNACION', 'La operadora Laura asignó el trabajo a Roberto', 2, 4);

-- El historial de la incidencia resuelta (ID 3)
INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) 
VALUES ('RESUELTA', 'FINALIZACION', 'Trabajo de limpieza completado con éxito', 3, 5);


-- 2. Pon una incidencia en estado VALIDADA y asígnasela a Roberto (ID del técnico)
-- Primero mira qué ID tiene Roberto: SELECT id_persona FROM Persona WHERE email = 'roberto@tech.es';
UPDATE Incidencia SET estado = 'VALIDADA', fk_tecnico = (SELECT id_persona FROM Persona WHERE email = 'roberto@tech.es') WHERE id_incidencia = 2;


INSERT INTO Incidencia(tipo, descripcion, estado, fk_ciudadano, fk_tecnico, fk_zona, coste, horas_prevision, trabajos_reparacion, tiempo_real, trabajos_realizados) 
VALUES('Alumbrado','Farola apagada en calle Mayor','EN CURSO',1,5,1,50,2,'Revisar instalación eléctrica',NULL,NULL);

INSERT INTO Incidencia(tipo, descripcion, estado, fk_ciudadano, fk_tecnico, fk_zona, coste, horas_prevision, trabajos_reparacion, tiempo_real, trabajos_realizados) 
VALUES('Limpieza','Contenedor roto en plaza central','EN CURSO',1,5,2,20,1,'Sustituir contenedor dañado',NULL,NULL);

INSERT INTO Incidencia(tipo, descripcion, estado, fk_ciudadano, fk_tecnico, fk_zona, coste, horas_prevision, trabajos_reparacion, tiempo_real, trabajos_realizados) 
VALUES('Señalizacion','Señal caída en avenida norte','EN CURSO',1,5,3,75,3,'Colocar nueva señal y fijar soporte',NULL,NULL);

INSERT INTO Incidencia(tipo, descripcion, estado, fk_ciudadano, fk_tecnico, fk_zona, coste, horas_prevision, trabajos_reparacion, tiempo_real, trabajos_realizados) 
VALUES('Alumbrado','Farola sin corriente en parque','EN CURSO',1,5,1,40,2,'Comprobar fusibles y cableado',NULL,NULL);

INSERT INTO Incidencia(tipo, descripcion, estado, fk_ciudadano, fk_tecnico, fk_zona, coste, horas_prevision, trabajos_reparacion, tiempo_real, trabajos_realizados) 
VALUES('Limpieza','Papeleras dañadas en plaza','EN CURSO',1,5,2,15,1,'Cambiar papeleras rotas',NULL,NULL);

INSERT INTO Incidencia(tipo, descripcion, estado, fk_ciudadano, fk_tecnico, fk_zona, coste, horas_prevision, trabajos_reparacion, tiempo_real, trabajos_realizados) 
VALUES('Señalizacion','Semáforo desconfigurado','EN CURSO',1,5,3,90,4,'Revisar controlador y reprogramar',NULL,NULL);