--Primero se deben borrar todas las tablas (de detalle a maestro) y lugo anyadirlas (de maestro a detalle)
--(en este caso en cada aplicacion se usa solo una tabla, por lo que no hace falta)

--Para giis.demo.tkrun:
--drop table Carreras;
--create table Carreras (id int primary key not null, inicio date not null, fin date not null, fecha date not null, descr varchar(32), check(inicio<=fin), check(fin<fecha));

DROP TABLE IF EXISTS historial;
DROP TABLE IF EXISTS incidencia;
DROP TABLE IF EXISTS persona;

-- Tabla Persona
CREATE TABLE Persona (
    id_persona INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario TEXT NOT NULL UNIQUE,
    contrasena TEXT NOT NULL,
    tipo TEXT CHECK(tipo IN ('CIUDADANO', 'TECNICO', 'OPERADOR')),
    nombre TEXT,
    apellidos TEXT,
    dni TEXT UNIQUE
);

-- Tabla Incidencia
CREATE TABLE Incidencia (
    id_incidencia INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo TEXT,
    descripcion TEXT,
    localizacion TEXT,
    fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado TEXT, -- 'ABIERTA', 'ASIGNADA', 'EN_PROCESO', 'RESUELTA'
    horas_estimadas REAL,
    coste REAL,
    fk_ciudadano INTEGER NOT NULL, -- El que la crea
    fk_tecnico INTEGER,            -- El que la arregla (puede ser NULL al inicio)
    FOREIGN KEY (fk_ciudadano) REFERENCES Persona(id_persona),
    FOREIGN KEY (fk_tecnico) REFERENCES Persona(id_persona)
);

-- Tabla Historial (Relaciona Incidencia y Persona)
CREATE TABLE Historial (
    id_historial INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado TEXT,      -- El nuevo estado (ej. 'ASIGNADA')
    accion TEXT,      -- Breve descripción (ej. 'ASIGNACION')
    detalle TEXT,     -- Explicación (ej. 'Operador asignó la tarea al técnico ID: 5')
    fk_incidencia INTEGER,
    fk_persona INTEGER, -- ¡IMPORTANTE! Aquí va el ID del OPERADOR (el que hace la acción)
    FOREIGN KEY (fk_incidencia) REFERENCES Incidencia(id_incidencia),
    FOREIGN KEY (fk_persona) REFERENCES Persona(id_persona)
);

