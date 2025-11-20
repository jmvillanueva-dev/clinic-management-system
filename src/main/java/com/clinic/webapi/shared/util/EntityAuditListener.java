package com.clinic.webapi.shared.util;

import com.clinic.webapi.shared.model.AuditableEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class EntityAuditListener {

  @PrePersist
  public void setCreationDate(Object entity) {
    if (entity instanceof AuditableEntity auditable) {
      Instant now = Instant.now();
      if (auditable.getFechaCreacion() == null) {
        auditable.setFechaCreacion(now);
      }
      auditable.setFechaActualizacion(now);
    }
  }

  @PreUpdate
  public void setUpdateDate(Object entity) {
    if (entity instanceof AuditableEntity auditable) {
      auditable.setFechaActualizacion(Instant.now());
    }
  }
}
