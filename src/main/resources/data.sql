-- =========================
-- LIMPIEZA
-- =========================
delete from historial;
delete from IncidenciaTecnico;
delete from incidencia;
delete from zona;
delete from persona;
-- =========================
-- PERSONAS
-- =========================
INSERT INTO Persona (usuario, contrasena, tipo, tipo_responsable, nombre, apellidos, dni, email) VALUES 
('paco_vecino', '1234', 'CIUDADANO', NULL, 'Francisco', 'García López', '12345678A', 'paco@correo.com'),
('emma_vecino', '1234', 'CIUDADANO', NULL, 'Emma', 'Rueda Sanchez', '11112222T', 'emma@correo.com'),
('javi_vecino', '1234', 'CIUDADANO', NULL, 'Javier', 'Raya Gonzalez', '11223344J', 'javi@correo.com'),

('laura_op', 'admin', 'OPERADOR', NULL, 'Laura', 'Martínez Ruiz', '87654321B', 'laura@ayto.es'),

--RESPONSABLE
('rober_tech', 'tech123', 'TECNICO', 'Limpieza', 'Roberto', 'Sánchez Soler', '11111111R', 'roberto@tech.es'),

--TÉCNICOS NORMALES
('pedro_tech', 'tech123', 'TECNICO', NULL, 'Pedro', 'Fernandez Perez', '22222222P', 'pedro@tech.es'),
('marta_tech', 'tech123', 'TECNICO', NULL, 'Marta', 'Valverde Martinez', '33333333M', 'marta@tech.es');

-- =========================
-- ZONAS
-- =========================
INSERT INTO Zona (nombre) VALUES ('Norte');
INSERT INTO Zona (nombre) VALUES ('Sur');
INSERT INTO Zona (nombre) VALUES ('Este');
INSERT INTO Zona (nombre) VALUES ('Oeste');

-- =========================
-- INCIDENCIAS
-- Todas se dan de alta con fecha_hora
-- y con estados más coherentes para pruebas
-- =========================

-- NUEVAS (para registrar / validar / consultar por periodo)
INSERT INTO Incidencia (tipo, descripcion, fk_zona, fecha_hora, estado, fk_ciudadano) VALUES
('Calzada', 'Socavón peligroso en mitad de la calle', 1, '2026-01-08 10:15:00', 'NUEVA', 1),
('Calzada', 'Socavón peligroso en mitad de la calle 2', 1, '2026-01-12 11:30:00', 'NUEVA', 1),
('Calzada', 'Socavón peligroso en mitad de la calle 3', 1, '2026-02-03 09:45:00', 'NUEVA', 1),
('Alumbrado', 'Farola parpadeando por la noche', 4, '2026-02-10 09:00:00', 'NUEVA', 3),
('Limpieza', 'Contenedor desbordado y suciedad alrededor', 2, '2026-02-14 12:00:00', 'NUEVA', 2);

-- VALIDADAS (pocas, para probar asignación)
INSERT INTO Incidencia (tipo, descripcion, fk_zona, fecha_hora, estado, fk_ciudadano) VALUES
('Mobiliario urbano', 'Banco roto con tornillos sueltos', 3, '2026-02-20 09:00:00', 'VALIDADA', 1),
('Zonas verdes', 'Árbol caído bloqueando el paso', 4, '2026-03-01 17:00:00', 'VALIDADA', 2);

-- ASIGNADA
INSERT INTO Incidencia (tipo, descripcion, fk_zona, fecha_hora, estado, coste, fk_ciudadano, fk_tecnico) VALUES
('Alumbrado', 'Farola fundida hace una semana', 2, '2026-03-03 08:30:00', 'ASIGNADA', 45.0, 1, 5);

-- EN CURSO
INSERT INTO Incidencia (tipo, descripcion, fk_zona, fecha_hora, estado, coste, fk_ciudadano, fk_tecnico, horas_prevision, trabajos_reparacion) VALUES
('Señalizacion', 'Señal caída en avenida norte', 3, '2026-03-05 10:00:00', 'EN CURSO', 75.0, 1, 5, 3, 'Colocar nueva señal y fijar soporte'),
('Alumbrado', 'Farola sin corriente en parque', 1, '2026-03-06 13:15:00', 'EN CURSO', 40.0, 1, 5, 2, 'Comprobar fusibles y cableado');

-- RESUELTA
INSERT INTO Incidencia (tipo, descripcion, fk_zona, fecha_hora, estado, coste, fk_ciudadano, fk_tecnico, horas_prevision, trabajos_reparacion, tiempo_real, trabajos_realizados) VALUES
('Limpieza', 'Grafitis en la fachada del centro cultural', 2, '2026-01-18 16:20:00', 'RESUELTA', 120.0, 1, 5, 5, 'Limpiar y pintar', 7, 'Pintadas eliminadas y pared repintada');

-- =========================
-- HISTORIAL
-- Solo algunos ejemplos útiles
-- =========================

-- Incidencia VALIDADA
INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) VALUES
('NUEVA', 'CREACION', 'El ciudadano registró el aviso', 6, 1),
('VALIDADA', 'VALIDACION', 'La operadora validó la incidencia', 6, 4);

-- Incidencia ASIGNADA
INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) VALUES
('NUEVA', 'CREACION', 'El ciudadano registró el aviso', 8, 1),
('VALIDADA', 'VALIDACION', 'La operadora validó la incidencia', 8, 4),
('ASIGNADA', 'ASIGNACION', 'La operadora asignó la incidencia al técnico Roberto', 8, 4);

-- Incidencia EN CURSO
INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) VALUES
('NUEVA', 'CREACION', 'El ciudadano registró el aviso', 9, 1),
('VALIDADA', 'VALIDACION', 'La operadora validó la incidencia', 9, 4),
('ASIGNADA', 'ASIGNACION', 'La operadora asignó la incidencia al técnico Roberto', 9, 4),
('EN CURSO', 'INICIO_REPARACION', 'El técnico inició los trabajos', 9, 5);

-- Incidencia RESUELTA
INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) VALUES
('NUEVA', 'CREACION', 'El ciudadano registró el aviso', 11, 1),
('VALIDADA', 'VALIDACION', 'La operadora validó la incidencia', 11, 4),
('ASIGNADA', 'ASIGNACION', 'La operadora asignó la incidencia al técnico Roberto', 11, 4),
('EN CURSO', 'INICIO_REPARACION', 'El técnico inició los trabajos', 11, 5),
('RESUELTA', 'FINALIZACION', 'Trabajo completado con éxito', 11, 5);


-- 2. Insertar 3 incidencias que el informe mensual cazará perfectamente

-- Incidencia 1: Para Paco (RESUELTA hace 2 días, 150 minutos)
INSERT INTO Incidencia (tipo, descripcion, fk_zona, fecha_hora, estado, fk_ciudadano, fk_tecnico, tiempo_real, trabajos_realizados)
VALUES ('Alumbrado', 'Farola fundida en calle mayor', 3, datetime('now', '-2 days'), 'RESUELTA', 1, 5, 150, 'Cambio de bombilla y cableado');

-- Incidencia 2: Para Paco (CERRADA hace 10 días, 90 minutos)
INSERT INTO Incidencia (tipo, descripcion, fk_zona, fecha_hora, estado, fk_ciudadano, fk_tecnico, tiempo_real, trabajos_realizados)
VALUES ('Limpieza', 'Graffiti en fachada del ayuntamiento', 1, datetime('now', '-10 days'), 'CERRADA', 1, 5, 90, 'Limpieza a presión con disolvente');

-- Incidencia 3: Para Lucía (RESUELTA hace 5 días, 200 minutos)
INSERT INTO Incidencia (tipo, descripcion, fk_zona, fecha_hora, estado, fk_ciudadano, fk_tecnico, tiempo_real, trabajos_realizados)
VALUES ('Mobiliario urbano', 'Banco astillado en el parque', 2, datetime('now', '-5 days'), 'RESUELTA', 1, 6, 200, 'Sustitución de maderas y barnizado');


-- Incidencia RECHAZADA 
INSERT INTO Incidencia (tipo, descripcion, fk_zona, fecha_hora, estado, fk_ciudadano) 
VALUES ('Limpieza','Solicitud de limpieza de una zona privada interior de comunidad', 2, '2026-03-18 11:40:00','RECHAZADA', 1);

-- Historial Incidencia Rechazada
INSERT INTO Historial (fecha_hora, estado, accion, detalle, fk_incidencia, fk_persona) VALUES
('2026-03-18 11:40:00', 'NUEVA',      'CREACION',   'La ciudadana registró una solicitud de limpieza', 15, 1),
('2026-03-18 12:15:00', 'RECHAZADA',  'RECHAZO',    'La operadora rechazó la incidencia por tratarse de un espacio privado fuera del servicio municipal', 15, 4);


-- Historial Incidencia CERRADA 
INSERT INTO Historial (estado, accion, detalle, fk_incidencia, fk_persona) VALUES
('NUEVA', 'CREACION', 'El ciudadano registró el aviso de graffiti en fachada', 13, 1),
('VALIDADA', 'VALIDACION', 'La operadora validó la incidencia', 13, 4),
('ASIGNADA', 'ASIGNACION', 'La operadora asignó la incidencia al técnico Roberto', 13, 4),
('EN CURSO', 'INICIO_REPARACION', 'El técnico inició la limpieza de la fachada', 13, 5),
('RESUELTA', 'FINALIZACION', 'El técnico eliminó el graffiti con limpieza a presión', 13, 5),
('CERRADA', 'CIERRE', 'La operadora cerró la incidencia tras comprobar el resultado', 13, 4);


-- =========================
-- MÁS TÉCNICOS PARA PRUEBAS
-- =========================

-- Mobiliario urbano
INSERT INTO Persona (usuario, contrasena, tipo, tipo_responsable, nombre, apellidos, dni, email)
VALUES 
('ana_mob', 'tech123', 'TECNICO', 'Mobiliario urbano', 'Ana', 'López Ruiz', '44444444A', 'ana.mob@tech.es'),
('carlos_mob', 'tech123', 'TECNICO', 'Mobiliario urbano', 'Carlos', 'Gómez Pérez', '55555555C', 'carlos.mob@tech.es');

-- Zonas verdes
INSERT INTO Persona (usuario, contrasena, tipo, tipo_responsable, nombre, apellidos, dni, email)
VALUES 
('sergio_verde', 'tech123', 'TECNICO', 'Zonas verdes', 'Sergio', 'Molina Gil', '66666666S', 'sergio.verde@tech.es'),
('lucia_verde', 'tech123', 'TECNICO', 'Zonas verdes', 'Lucía', 'Navarro Díaz', '77777777L', 'lucia.verde@tech.es');

-- Alumbrado
INSERT INTO Persona (usuario, contrasena, tipo, tipo_responsable, nombre, apellidos, dni, email)
VALUES 
('diego_luz', 'tech123', 'TECNICO', 'Alumbrado', 'Diego', 'Santos Vega', '88888888D', 'diego.luz@tech.es'),
('irene_luz', 'tech123', 'TECNICO', 'Alumbrado', 'Irene', 'Castro León', '99999999I', 'irene.luz@tech.es');

-- Limpieza
INSERT INTO Persona (usuario, contrasena, tipo, tipo_responsable, nombre, apellidos, dni, email)
VALUES 
('raul_clean', 'tech123', 'TECNICO', 'Limpieza', 'Raúl', 'Ortega Ruiz', '10101010R', 'raul.clean@tech.es'),
('nuria_clean', 'tech123', 'TECNICO', 'Limpieza', 'Nuria', 'Domínguez Pardo', '20202020N', 'nuria.clean@tech.es');

-- Calzada
INSERT INTO Persona (usuario, contrasena, tipo, tipo_responsable, nombre, apellidos, dni, email)
VALUES 
('alberto_calz', 'tech123', 'TECNICO', 'Calzada', 'Alberto', 'Gil Romero', '30303030A', 'alberto.calz@tech.es'),
('patri_calz', 'tech123', 'TECNICO', 'Calzada', 'Patricia', 'Rey Soto', '40404040P', 'patri.calz@tech.es');

-- VALIDADAS (pocas, para probar asignación)
INSERT INTO Incidencia (tipo, descripcion, fk_zona, fecha_hora, estado, fk_ciudadano) VALUES
('Limpieza', 'Banco con cagadas de paloma', 3, '2026-04-04 09:00:00', 'VALIDADA', 1),
('Limpieza', 'Plaza con residuos de comida', 4, '2026-04-01 17:00:00', 'VALIDADA', 2),
('Limpieza', 'Meadas en la puerta de los garajes', 4, '2026-04-07 20:00:00', 'VALIDADA', 2),
('Limpieza', 'Grafitis en los trenes', 4, '2026-04-10 19:00:00', 'VALIDADA', 2);
