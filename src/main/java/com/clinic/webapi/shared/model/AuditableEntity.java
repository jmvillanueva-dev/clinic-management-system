package com.clinic.webapi.shared.model;

import java.time.Instant;

public interface AuditableEntity {
    void setFechaCreacion(Instant fechaCreacion);
    Instant getFechaCreacion();
    
    void setFechaActualizacion(Instant fechaActualizacion);
    Instant getFechaActualizacion();
}
