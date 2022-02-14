package es.udc.asi.restexample.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.udc.asi.restexample.model.exception.InvalidMailException;
import es.udc.asi.restexample.model.exception.UserCorreoExistsException;
import es.udc.asi.restexample.model.exception.UsuarioSinTitulacionesException;
import es.udc.asi.restexample.model.service.UsuarioService;
import es.udc.asi.restexample.model.service.dto.CorreoDTO;
import es.udc.asi.restexample.model.service.dto.UsuarioDTOPrivado;
import es.udc.asi.restexample.model.service.dto.UsuarioDTOPublico;
import es.udc.asi.restexample.security.JWTToken;
import es.udc.asi.restexample.security.TokenProvider;
import es.udc.asi.restexample.web.exceptions.CredentialsAreNotValidException;
import es.udc.asi.restexample.web.exceptions.RequestBodyNotValidException;

/**
 * Este controlador va por separado que el UserResource porque se encarga de
 * tareas relacionadas con la autenticación, creación, etc.
 *
 * <p>
 * También permite a cada usuario logueado en la aplicación obtener información
 * de su cuenta
 */
@RestController
@RequestMapping("/api")
public class AccountResource {
  private final Logger logger = LoggerFactory.getLogger(AccountResource.class);

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UsuarioService usuarioService;

  @PostMapping("/iniciar_sesion")
  public JWTToken authenticate(@Valid @RequestBody CorreoDTO correoDTO) throws CredentialsAreNotValidException {

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        correoDTO.getCorreo(), correoDTO.getContrasena());
    try {
      Authentication authentication = authenticationManager.authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = tokenProvider.createToken(authentication);
      return new JWTToken(jwt);
    } catch (AuthenticationException e) {
      logger.warn(e.getMessage(), e);
      throw new CredentialsAreNotValidException(e.getMessage());
    }
  }

  @GetMapping("/cuenta")
  public UsuarioDTOPrivado getAccount() {
    return usuarioService.getCurrentUserWithAuthority();
  }

  @PostMapping("/crear_usuario")
  public UsuarioDTOPublico createAccount(@Valid @RequestBody UsuarioDTOPrivado usuario, Errors errors)
      throws UserCorreoExistsException, RequestBodyNotValidException, UsuarioSinTitulacionesException, InvalidMailException {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }
    return usuarioService.create(usuario.getCorreo(), usuario.getContrasena(), usuario.getNombre(), usuario.getApellido1(), usuario.getApellido2(),
    		usuario.getCategoria(), usuario.getTitulaciones());
  }
}
