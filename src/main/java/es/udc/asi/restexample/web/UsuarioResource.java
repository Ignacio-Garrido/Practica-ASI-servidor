package es.udc.asi.restexample.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.udc.asi.restexample.model.domain.Eleccion;
import es.udc.asi.restexample.model.domain.Usuario;
import es.udc.asi.restexample.model.exception.BadCredentialsException;
import es.udc.asi.restexample.model.exception.ModelException;
import es.udc.asi.restexample.model.exception.NotFoundException;
import es.udc.asi.restexample.model.exception.SelfUserException;
import es.udc.asi.restexample.model.exception.UserCorreoExistsException;
import es.udc.asi.restexample.model.service.UsuarioService;
import es.udc.asi.restexample.model.service.dto.CorreoDTO;
import es.udc.asi.restexample.model.service.dto.EleccionDTO;
import es.udc.asi.restexample.model.service.dto.ImagenDTO;
import es.udc.asi.restexample.model.service.dto.UsuarioDTOPublico;
import es.udc.asi.restexample.web.exceptions.CredentialsAreNotValidException;
import es.udc.asi.restexample.web.exceptions.IdAndBodyNotMatchingOnUpdateException;
import es.udc.asi.restexample.web.exceptions.RequestBodyNotValidException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

  @Autowired
  private UsuarioService usuarioService;
  
  @Autowired
  private AccountResource accountResource;


  @GetMapping
  public List<UsuarioDTOPublico> findAll(@RequestParam(required = false) String query, @RequestParam(required = false) String cat) {
    return usuarioService.findAll(query, cat);
  }
  
  @GetMapping("/{id}")
  public UsuarioDTOPublico findOne(@PathVariable Long id) throws NotFoundException {
    return usuarioService.findById(id);
  }
  
  @GetMapping("/{correo}/correo")
  public UsuarioDTOPublico findByCorreo(@PathVariable String correo) throws NotFoundException {
    return usuarioService.findByCorreo(correo);
  }
  
  @PutMapping("/{id}")
  public void editar(@PathVariable Long id, @Valid @RequestBody UsuarioDTOPublico usuario, Errors errors)
	throws IdAndBodyNotMatchingOnUpdateException, RequestBodyNotValidException, ModelException {
	if (errors.hasErrors()) {
		throw new RequestBodyNotValidException(errors);
	}
	if (id != usuario.getId()) {
	    throw new IdAndBodyNotMatchingOnUpdateException(Usuario.class);
	}
	usuarioService.editar(usuario.getCorreo(), usuario.getCategoria(), usuario.getTitulaciones());
  }
  
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) throws SelfUserException {
    usuarioService.delete(id);
  }
  
  @PostMapping("/{id}/imagen")
  @ResponseStatus(HttpStatus.OK)
  public void guardarImagenUsuario(@PathVariable Long id, @RequestParam MultipartFile file, HttpServletResponse response)
      throws ModelException {
    usuarioService.guardarImagenUsuario(id, file);
  }
  
  @GetMapping("/{id}/imagen")
  @ResponseStatus(HttpStatus.OK)
  public void getImagenUsuario(@PathVariable Long id, HttpServletResponse response) throws ModelException {
	  ImagenDTO imagen = usuarioService.getImagenUsuario(id);
	  
	  try {
		  response.setContentType(imagen.getTipo());
		  response.setHeader("Content-disposition", "filename=" + imagen.getNombreFichero());
		  IOUtils.copy(imagen.getInputStream(), response.getOutputStream());
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
  }
  
  @PutMapping("/{id}/cambiar_contrasena")
  @ResponseStatus(HttpStatus.OK)
  public void cambiarContrasena(@PathVariable Long id, @Valid @RequestBody CorreoDTO correoDTO, Errors errors)
	throws IdAndBodyNotMatchingOnUpdateException, RequestBodyNotValidException, UserCorreoExistsException, BadCredentialsException, CredentialsAreNotValidException {
	if (errors.hasErrors()) {
		throw new RequestBodyNotValidException(errors);
	}
    accountResource.authenticate(correoDTO);
	usuarioService.cambiarContrasena(id, correoDTO.getCorreo(), correoDTO.getNuevaContrasena());
  }
  
  @GetMapping("/{id}/historial_candidato")
  public List<EleccionDTO> getHistorialCandidato(@PathVariable Long id) {
	  return usuarioService.getHistorialCandidato(id);
  }
  
  @GetMapping("/{id}/historial_votante")
	public List<EleccionDTO> findEleccionesVotante(@PathVariable @Valid Long id) throws NotFoundException {
	  return usuarioService.findEleccionesVotante(id);
	}
}
