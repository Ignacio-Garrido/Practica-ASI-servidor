package es.udc.asi.restexample.model.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.asi.restexample.model.domain.Eleccion;
import es.udc.asi.restexample.model.domain.Titulacion;
import es.udc.asi.restexample.model.domain.Usuario;
import es.udc.asi.restexample.model.exception.NotFoundException;
import es.udc.asi.restexample.model.exception.TitulacionNombreExistsException;
import es.udc.asi.restexample.model.repository.EleccionDao;
import es.udc.asi.restexample.model.repository.TitulacionDao;
import es.udc.asi.restexample.model.repository.UsuarioDao;
import es.udc.asi.restexample.model.service.dto.TitulacionDTO;
import es.udc.asi.restexample.model.service.dto.UsuarioDTOPublico;

@Service
@Transactional(readOnly = true)
public class TitulacionService {

	@Autowired
	private TitulacionDao titulacionDAO;

	@Autowired
	private EleccionDao eleccionDAO;
	
	@Autowired
	private UsuarioDao usuarioDAO;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public List<TitulacionDTO> findAll() {
		return titulacionDAO.findAll().stream().sorted(Comparator.comparing(Titulacion::getNombre)).map(TitulacionDTO::new)
				.collect(Collectors.toList());
	}
	
	public TitulacionDTO findById(Long id) throws NotFoundException {
		Titulacion titulacion = titulacionDAO.findById(id);
		if (titulacion == null) {
			throw new NotFoundException(id.toString(), Titulacion.class);
		}
		return new TitulacionDTO(titulacionDAO.findById(id));
	}

	@PreAuthorize("hasAuthority('Admin')")
	public TitulacionDTO findByNombre(String nombre) throws NotFoundException {
		Titulacion titulacion = titulacionDAO.findByNombre(nombre);
		if (titulacion == null) {
			throw new NotFoundException(nombre, Titulacion.class);
		}
		return new TitulacionDTO(titulacionDAO.findByNombre(nombre));
	}
	
	// Con estas anotaciones evitamos que usuarios no autorizados accedan a ciertas
	// funcionalidades
	@PreAuthorize("hasAuthority('Admin')")
	@Transactional(readOnly = false)
	public TitulacionDTO create(TitulacionDTO titulacion) throws TitulacionNombreExistsException {
		if (titulacionDAO.findByNombre(titulacion.getNombre()) != null) {
		      throw new TitulacionNombreExistsException(titulacion.getNombre());
		  }
		Titulacion bdTitulacion = new Titulacion(titulacion.getNombre());
		titulacionDAO.create(bdTitulacion);
		return new TitulacionDTO(bdTitulacion);
	}
	
	@PreAuthorize("hasAuthority('Admin')")
	@Transactional(readOnly = false)
	public TitulacionDTO update(TitulacionDTO titulacion) {
		Titulacion bdTitulacion = titulacionDAO.findById(titulacion.getId());
		bdTitulacion.setNombre(titulacion.getNombre());
		titulacionDAO.update(bdTitulacion);
		return new TitulacionDTO(bdTitulacion);
	}

	// ##### Si se borra una titulacion se deberia borrar de elecciones y usuarios pero...
	// ##### en caso de que tengan solo esa titulacion...
	// ##### puede haber usuarios y elecciones sin titulaciones?
	@PreAuthorize("hasAuthority('Admin')")
	@Transactional(readOnly = false)
	public void deleteById(Long id) {
		List<Eleccion> eleccionList = eleccionDAO.findAll();
		List<Usuario> usuarioList = usuarioDAO.findAll(null, null);
		Titulacion titulacion = titulacionDAO.findById(id);
		for (Eleccion eleccion : eleccionList) {
			Set<Titulacion> titulacionList = new HashSet<>();
			boolean update = false;
			for (Titulacion titulacionAux : eleccion.getTitulaciones()) {
				if (titulacionAux != titulacion) {
					titulacionList.add(titulacionAux);
				} else {
					update = true;
				}
			}
			if (update) {
				if (titulacionList.size() > 0) {
					eleccion.setTitulaciones(titulacionList);
					eleccionDAO.update(eleccion);
				} else {
					eleccionDAO.deleteById(eleccion.getId());
				}
			}
		}
		for (Usuario usuario : usuarioList) {
			List<Titulacion> titulacionList = new ArrayList<>();
			boolean update = false;
			for (Titulacion titulacionAux : usuario.getTitulaciones()) {
				if (titulacionAux != titulacion) {
					titulacionList.add(titulacionAux);
				} else {
					update = true;
				}
			}
			if (update) {
				if (titulacionList.size() > 0) {
					usuario.setTitulaciones(titulacionList);
					usuarioDAO.update(usuario);
				} else {
					usuarioDAO.deleteById(usuario.getId());
				}
			}
		}
		titulacionDAO.deleteById(id);
	}

	public List<UsuarioDTOPublico> findByTitulacionId(Long id) {
		List<UsuarioDTOPublico> aux = usuarioService.findAll(null, null);
		List<UsuarioDTOPublico> usuarioList = new ArrayList<>();
		for (UsuarioDTOPublico usuario : aux) {
			boolean add = false;
			for (TitulacionDTO titulacion : usuario.getTitulaciones()) {
				if (titulacion.getId().equals(id)) {
					add = true;
				}
			}
			if (add) {
				usuarioList.add(usuario);
			}
		}
		return usuarioList;
	}
}
