package es.udc.
asi.restexample.security;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.udc.asi.restexample.model.domain.Usuario;
import es.udc.asi.restexample.model.repository.UsuarioDao;

@Component
public class MyUserDetailsService implements UserDetailsService {
  private final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

  @Autowired
  private UsuarioDao usuarioDAO;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
    Usuario usuario = usuarioDAO.findByCorreo(correo);
    if (usuario == null) {
      throw new UsernameNotFoundException("El usuario con el correo " + correo + " no se encontró");
    }
    logger.info("Cargado usuario {} con categoría {}", correo, usuario.getCategoria().name());
    GrantedAuthority categoria = new SimpleGrantedAuthority(usuario.getCategoria().name());
    return new org.springframework.security.core.userdetails.User(correo, usuario.getContrasena(),
        Collections.singleton(categoria));
  }
}
