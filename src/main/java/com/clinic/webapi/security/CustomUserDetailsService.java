package com.clinic.webapi.security;

import com.clinic.webapi.model.entity.Usuario;
import com.clinic.webapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UsuarioRepository usuarioRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

    // 1. Obtener la lista de nombres de roles con el prefijo "ROLE_"
    String[] authorities = usuario.getRoles().stream()
        .map(rol -> "ROLE_" + rol.getNombre().toUpperCase())
        .toArray(String[]::new);

    // 2. Mapear la entidad Usuario a un objeto UserDetails de Spring Security
    return org.springframework.security.core.userdetails.User.builder()
        .username(usuario.getEmail())
        .password(usuario.getPasswordHash())
        .disabled(!usuario.isEstaActivo())
        .authorities(authorities)
        .build();
  }
}