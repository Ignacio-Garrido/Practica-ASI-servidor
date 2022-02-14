package es.udc.asi.restexample.model.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import es.udc.asi.restexample.model.domain.Candidatura;
import es.udc.asi.restexample.model.domain.Categoria;
import es.udc.asi.restexample.model.domain.Eleccion;
import es.udc.asi.restexample.model.domain.Usuario;
import es.udc.asi.restexample.model.exception.InvalidMailException;
import es.udc.asi.restexample.model.exception.ModelException;
import es.udc.asi.restexample.model.exception.NotFoundException;
import es.udc.asi.restexample.model.exception.SelfUserException;
import es.udc.asi.restexample.model.exception.UserCorreoExistsException;
import es.udc.asi.restexample.model.exception.UsuarioSinTitulacionesException;
import es.udc.asi.restexample.model.repository.CandidaturaDao;
import es.udc.asi.restexample.model.repository.EleccionDao;
import es.udc.asi.restexample.model.repository.TitulacionDao;
import es.udc.asi.restexample.model.repository.UsuarioDao;
import es.udc.asi.restexample.model.service.dto.EleccionDTO;
import es.udc.asi.restexample.model.service.dto.ImagenDTO;
import es.udc.asi.restexample.model.service.dto.TitulacionDTO;
import es.udc.asi.restexample.model.service.dto.UsuarioDTOPrivado;
import es.udc.asi.restexample.model.service.dto.UsuarioDTOPublico;
import es.udc.asi.restexample.model.service.util.ImagenService;
import es.udc.asi.restexample.security.SecurityUtils;
import es.udc.asi.restexample.web.exceptions.IdAndBodyNotMatchingOnUpdateException;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class UsuarioService {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UsuarioDao usuarioDAO;
  
  @Autowired
  private CandidaturaDao candidaturaDAO;
  
  @Autowired
  private EleccionDao eleccionDAO;
  
  @Autowired
  private CandidaturaService candidaturaService;
  
  @Autowired
  private TitulacionDao titulacionDAO;
  
  @Autowired
  private ImagenService imagenService;

  public List<UsuarioDTOPublico> findAll(String filter, String cat) {
    return usuarioDAO.findAll(filter, cat).stream().map(user -> new UsuarioDTOPublico(user)).collect(Collectors.toList());
  }
  
  public UsuarioDTOPublico findById(Long id) throws NotFoundException {
	Usuario usuario = usuarioDAO.findById(id);
	if (usuario == null) {
		throw new NotFoundException(id.toString(), Usuario.class);
	}
	return new UsuarioDTOPublico(usuario);
  }
  
  @PreAuthorize("hasAuthority('Admin')")
  public UsuarioDTOPublico findByCorreo(String correo) throws NotFoundException {
	Usuario usuario = usuarioDAO.findByCorreo(correo);
	if (usuario == null) {
		return null;
	}
	return new UsuarioDTOPublico(usuario);
  }
  
  @PreAuthorize("hasAuthority('Admin')")
  @Transactional(readOnly = false)
  public UsuarioDTOPublico create(String correo, String contrasena, String nombre, String apellido1, String apellido2, String categoria, List<TitulacionDTO> titulaciones) throws UserCorreoExistsException, UsuarioSinTitulacionesException, InvalidMailException {
	  if (usuarioDAO.findByCorreo(correo) != null) {
	      throw new UserCorreoExistsException(correo);
	  }
	  
	  Usuario usuario = new Usuario();
	  String contrasenaEncriptada = passwordEncoder.encode(contrasena);
	  
	  if (!Pattern.compile("^(.+)@(\\S+)$").matcher(correo).matches()) {
		  throw new InvalidMailException(correo);
	  }
	  usuario.setCorreo(correo);
	  usuario.setContrasena(contrasenaEncriptada);
	  usuario.setNombre(nombre);
	  usuario.setApellido1(apellido1);
	  usuario.setApellido2(apellido2);
	  usuario.setCategoria(Categoria.valueOf(categoria));
	  
	  if ((titulaciones.isEmpty()) && (!categoria.equals("Admin"))) {
		  throw new UsuarioSinTitulacionesException(correo);
	  } else {
		  titulaciones.forEach(titulacion -> {
			  usuario.getTitulaciones().add(titulacionDAO.findById(titulacion.getId()));
		  });
	  }
	  
	  usuarioDAO.create(usuario);
	  return new UsuarioDTOPublico(usuarioDAO.findByCorreo(correo));
  }
  
  @PreAuthorize("hasAuthority('Admin')")
  @Transactional(readOnly = false)
  public void delete(Long id) throws SelfUserException {
	Usuario usuario = usuarioDAO.findById(id);
	UsuarioDTOPrivado currentUser = getCurrentUserWithAuthority();
    if (currentUser.getId().equals(id)) {
      throw new SelfUserException();
    } else { //Borramos todas las candidaturas (y referencias de votos) del usuario que borramos
	    List<Candidatura> candidaturaList = candidaturaDAO.findAll();
	    for(Candidatura candidatura : candidaturaList) {
	    	if (candidatura.getVotantes().contains(usuario)) {
	    		candidatura.getVotantes().remove(usuario);
	    		if (!candidatura.getEleccion().getFechaLimiteVotacion().isBefore(LocalDate.now())) {
	    			candidatura.setNumVotos(candidatura.getNumVotos()-1);
	    		}
	    	}
	    	if(candidatura.getCandidato().getCorreo().equals(usuario.getCorreo())) {
	    		candidaturaDAO.deleteById(candidatura.getId());
	    	}
	    }
		usuarioDAO.deleteById(usuario.getId());
    }
  }
  
  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public void cambiarContrasena(Long id, String correo, String nuevaContrasena) throws IdAndBodyNotMatchingOnUpdateException {
	Usuario usuario = usuarioDAO.findByCorreo(correo);
	if (id != usuario.getId()) {
	    throw new IdAndBodyNotMatchingOnUpdateException(Usuario.class);
	}
	String contrasenaEncriptada = passwordEncoder.encode(nuevaContrasena);
	usuario.setContrasena(contrasenaEncriptada);
	usuarioDAO.update(usuario);
  }
  
  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public void editar(String correo, String categoria, List<TitulacionDTO> titulaciones) throws IdAndBodyNotMatchingOnUpdateException, ModelException, UsuarioSinTitulacionesException {
	  Usuario usuario = usuarioDAO.findByCorreo(correo);
	  usuario.setCategoria(Categoria.valueOf(categoria));
	  if (categoria.equals("Admin")) {
		  usuario.getTitulaciones().clear();
	  } else if (titulaciones.isEmpty()) {
		  throw new UsuarioSinTitulacionesException(correo);
	  } else {
		  usuario.getTitulaciones().clear();
		  titulaciones.forEach(titulacion -> {
			  usuario.getTitulaciones().add(titulacionDAO.findById(titulacion.getId()));
		  });
	  }
	  usuarioDAO.update(usuario);
	  
	  List<Eleccion> elecciones = eleccionDAO.findProximas();
	  elecciones.addAll(eleccionDAO.findActivas());
	  List<Candidatura> candidaturas = candidaturaDAO.findAll();
	  Usuario actualizado = usuarioDAO.findByCorreo(correo);
	  for(Candidatura c :candidaturas) {
		  if((c.getCandidato().getCorreo().equals(correo)) && (elecciones.contains(c.getEleccion()))) {
			  if(!candidaturaService.comprobarPermiso(c.getEleccion(), actualizado, false)) {
				  candidaturaDAO.deleteById(c.getId());
			  }
		  }
	  };
  }
  
  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public void guardarImagenUsuario (Long id, MultipartFile imagen) throws ModelException {
	  Usuario usuario = usuarioDAO.findById(id);
	  if (usuario == null) throw new NotFoundException(id.toString(), Usuario.class);
	  
	  String path = imagenService.saveImage(imagen, id); 
	  usuario.setImagen(path);
	  usuarioDAO.update(usuario);
  }
  
  public ImagenDTO getImagenUsuario (Long id) throws ModelException {
	  Usuario usuario = usuarioDAO.findById(id);
	  if (usuario == null) throw new NotFoundException(id.toString(), Usuario.class);
	  
	  return imagenService.getImage(usuario.getImagen(), id);
  }
  
  
  public UsuarioDTOPrivado getCurrentUserWithAuthority() {
    String currentUserCorreo = SecurityUtils.getCurrentUserCorreo();
    if (currentUserCorreo != null) {
      return new UsuarioDTOPrivado(usuarioDAO.findByCorreo(currentUserCorreo));
    }
    return null;
  }
  
  public List<EleccionDTO> getHistorialCandidato(Long id) {
	  List<Candidatura> candidaturas = candidaturaDAO.findAll();
	  List<EleccionDTO> elecciones = new ArrayList<>();
	  for (Candidatura c : candidaturas) {
		  if (c.getCandidato().getId().equals(id)) {
			  elecciones.add(new EleccionDTO(c.getEleccion()));
		  }
	  }
	  return elecciones;
  }
  
  public List<EleccionDTO> findEleccionesVotante(Long id){
		Usuario usuario = usuarioDAO.findById(id);
		List<Candidatura> candidaturas = candidaturaDAO.findAll();
		List<EleccionDTO> elevotantes = new ArrayList<>();
		for (Candidatura x : candidaturas) {
			if (x.getVotantes().contains(usuario)) {
				elevotantes.add(new EleccionDTO(x.getEleccion()));
			}
		}
		return elevotantes;
  }
}
