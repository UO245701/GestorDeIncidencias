DROP TABLE IF EXISTS Historial;
DROP TABLE IF EXISTS IncidenciaTecnico;
DROP TABLE IF EXISTS PresupuestoTipoIncidencia;
DROP TABLE IF EXISTS Factura;
DROP TABLE IF EXISTS Incidencia;
DROP TABLE IF EXISTS Zona;
DROP TABLE IF EXISTS Persona;

CREATE TABLE Persona (
    id_persona INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario TEXT NOT NULL UNIQUE,
    contrasena TEXT NOT NULL,
    tipo TEXT CHECK(tipo IN ('CIUDADANO', 'TECNICO', 'OPERADOR')),
    tipo_responsable TEXT,
    nombre TEXT,
    apellidos TEXT,
    dni TEXT UNIQUE,
    email TEXT UNIQUE,
    precio_hora REAL DEFAULT 25.0,
    CHECK (
        tipo_responsable IS NULL 
        OR tipo = 'TECNICO'
    )
);

CREATE TABLE Zona (
    id_zona INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE
);

CREATE TABLE Incidencia (
    id_incidencia INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo TEXT,
    descripcion TEXT,
    fecha_hora DATETIME DEFAULT (datetime('now','localtime')),
    estado TEXT,
    coste REAL,
    horas_prevision INTEGER,
    trabajos_reparacion TEXT,
    fk_ciudadano INTEGER NOT NULL,
    fk_tecnico INTEGER,
    fk_zona INTEGER NOT NULL,
    tiempo_real INTEGER,
    trabajos_realizados TEXT,
    coste_materiales REAL DEFAULT 0.0,
    descripcion_materiales TEXT,
    coste_total REAL DEFAULT 0.0,
    FOREIGN KEY (fk_ciudadano) REFERENCES Persona(id_persona),
    FOREIGN KEY (fk_tecnico) REFERENCES Persona(id_persona),
    FOREIGN KEY (fk_zona) REFERENCES Zona(id_zona)
);

CREATE TABLE IncidenciaTecnico (
    fk_incidencia INTEGER NOT NULL,
    fk_tecnico INTEGER NOT NULL,
    fecha_asignacion DATETIME DEFAULT (datetime('now','localtime')),
    PRIMARY KEY (fk_incidencia, fk_tecnico),
    FOREIGN KEY (fk_incidencia) REFERENCES Incidencia(id_incidencia),
    FOREIGN KEY (fk_tecnico) REFERENCES Persona(id_persona)
);

CREATE TABLE Historial (
    id_historial INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_hora DATETIME DEFAULT (datetime('now','localtime')),
    estado TEXT,
    accion TEXT,
    detalle TEXT,
    fk_incidencia INTEGER,
    fk_persona INTEGER,
    FOREIGN KEY (fk_incidencia) REFERENCES Incidencia(id_incidencia),
    FOREIGN KEY (fk_persona) REFERENCES Persona(id_persona)
);


CREATE TABLE PresupuestoTipoIncidencia (
    id_presupuesto INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo TEXT NOT NULL,
    importe_maximo REAL NOT NULL CHECK (importe_maximo > 0),
    importe_consumido REAL NOT NULL DEFAULT 0.0 CHECK (importe_consumido >= 0),
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    activo INTEGER NOT NULL DEFAULT 1 CHECK (activo IN (0, 1)),
    CHECK (date(fecha_fin) >= date(fecha_inicio))
);

CREATE UNIQUE INDEX ux_presupuesto_tipo_activo
ON PresupuestoTipoIncidencia(tipo)
WHERE activo = 1;

CREATE TABLE Factura (
    id_factura INTEGER PRIMARY KEY AUTOINCREMENT,
    numero_factura TEXT NOT NULL UNIQUE,
    fecha_emision DATETIME NOT NULL DEFAULT (datetime('now','localtime')),
    emisor TEXT NOT NULL,
    detalle TEXT NOT NULL,
    coste_total REAL NOT NULL CHECK (coste_total >= 0),
    fk_incidencia INTEGER NOT NULL UNIQUE,
    fk_operador INTEGER NOT NULL,
    FOREIGN KEY (fk_incidencia) REFERENCES Incidencia(id_incidencia),
    FOREIGN KEY (fk_operador) REFERENCES Persona(id_persona)
);
