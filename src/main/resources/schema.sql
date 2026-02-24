-- 1. Borrado en orden inverso 
DROP TABLE IF EXISTS Historial;
DROP TABLE IF EXISTS Incidencia;
DROP TABLE IF EXISTS Persona;

CREATE TABLE Persona (
    id_persona INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario TEXT NOT NULL UNIQUE,
    contrasena TEXT NOT NULL,
    tipo TEXT CHECK(tipo IN ('CIUDADANO', 'TECNICO', 'OPERADOR')),
    nombre TEXT,
    apellidos TEXT,
    dni TEXT UNIQUE
);

CREATE TABLE Incidencia (
    id_incidencia INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo TEXT,
    descripcion TEXT,
    localizacion TEXT,
    fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado TEXT, 
    horas_estimadas REAL,
    coste REAL,
    fk_ciudadano INTEGER NOT NULL,
    fk_tecnico INTEGER,
    FOREIGN KEY (fk_ciudadano) REFERENCES Persona(id_persona),
    FOREIGN KEY (fk_tecnico) REFERENCES Persona(id_persona)
);

CREATE TABLE Historial (
    id_historial INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado TEXT,
    accion TEXT,
    detalle TEXT,
    fk_incidencia INTEGER,
    fk_persona INTEGER,
    FOREIGN KEY (fk_incidencia) REFERENCES Incidencia(id_incidencia),
    FOREIGN KEY (fk_persona) REFERENCES Persona(id_persona)
);