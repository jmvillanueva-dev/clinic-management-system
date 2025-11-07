package com.clinic.webapi.util;

import com.clinic.webapi.model.entity.Empleado;
import com.clinic.webapi.model.entity.Rol;
import com.clinic.webapi.model.entity.Usuario;
import org.springframework.stereotype.Component;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;

@Component
public class EntityAuditListener {

  @PrePersist
  public void setCreationDate(Object entity) {
    Instant now = Instant.now();

    if (entity instanceof Empleado empleado) {
      if (empleado.getFechaCreacion() == null) {
        empleado.setFechaCreacion(now);
      }
      empleado.setFechaActualizacion(now);
    } else if (entity instanceof Usuario usuario) {
      if (usuario.getFechaCreacion() == null) {
        usuario.setFechaCreacion(now);
      }
      usuario.setFechaActualizacion(now);
    } else if (entity instanceof Rol rol) {
      if (rol.getFechaCreacion() == null) {
        rol.setFechaCreacion(now);
      }
      rol.setFechaActualizacion(now);
    }
  }

  @PreUpdate
  public void setUpdateDate(Object entity) {
    Instant now = Instant.now();

    if (entity instanceof Empleado empleado) {
      empleado.setFechaActualizacion(now);
    } else if (entity instanceof Usuario usuario) {
      usuario.setFechaActualizacion(now);
    } else if (entity instanceof Rol rol) {
      rol.setFechaActualizacion(now);
    }
  }
}