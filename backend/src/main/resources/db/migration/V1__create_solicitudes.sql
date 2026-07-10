CREATE TABLE solicitudes (
    id UUID PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    solicitante VARCHAR(100) NOT NULL,
    aprobador VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    creado_en TIMESTAMP NOT NULL,
    actualizado_en TIMESTAMP NOT NULL
);

CREATE TABLE historial_cambios (
    id UUID PRIMARY KEY,
    solicitud_id UUID NOT NULL REFERENCES solicitudes (id),
    estado VARCHAR(50) NOT NULL,
    comentario TEXT,
    realizado_por VARCHAR(100) NOT NULL,
    fecha TIMESTAMP NOT NULL
);

CREATE TABLE notificaciones (
    id UUID PRIMARY KEY,
    destinatario VARCHAR(100) NOT NULL,
    solicitud_id UUID NOT NULL REFERENCES solicitudes (id),
    mensaje TEXT NOT NULL,
    leida BOOLEAN NOT NULL DEFAULT FALSE,
    creado_en TIMESTAMP NOT NULL
);

CREATE INDEX idx_solicitudes_aprobador ON solicitudes (aprobador);
CREATE INDEX idx_solicitudes_estado ON solicitudes (estado);
CREATE INDEX idx_historial_cambios_solicitud_id ON historial_cambios (solicitud_id);
CREATE INDEX idx_notificaciones_destinatario ON notificaciones (destinatario);
