package es.udc.asi.restexample.model.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.asi.restexample.model.domain.Candidatura;
import es.udc.asi.restexample.model.domain.Eleccion;
import es.udc.asi.restexample.model.domain.Grupos;
import es.udc.asi.restexample.model.domain.Usuario;
import es.udc.asi.restexample.model.exception.AdelantarFechaException;
import es.udc.asi.restexample.model.exception.ElectionVotedException;
import es.udc.asi.restexample.model.exception.FechaPasadaException;
import es.udc.asi.restexample.model.exception.FechasNoValidasException;
import es.udc.asi.restexample.model.exception.NotFoundException;
import es.udc.asi.restexample.model.repository.CandidaturaDao;
import es.udc.asi.restexample.model.repository.EleccionDao;
import es.udc.asi.restexample.model.repository.TitulacionDao;
import es.udc.asi.restexample.model.service.dto.CandidaturaDTO;
import es.udc.asi.restexample.model.service.dto.EleccionDTO;
import es.udc.asi.restexample.model.service.dto.TitulacionDTO;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class EleccionService {

	@Autowired
	private EleccionDao eleccionDAO;
	
	@Autowired
	private TitulacionDao titulacionDAO;
	
	@Autowired
	private CandidaturaDao candidaturaDAO;
	
	public List<EleccionDTO> findAll() {
	  return eleccionDAO.findAll().stream().map(eleccion -> new EleccionDTO(eleccion)).collect(Collectors.toList());
	}
	
	public List<EleccionDTO> findProximas() {
	  return eleccionDAO.findProximas().stream().map(eleccion -> new EleccionDTO(eleccion)).collect(Collectors.toList());
	}
	
	public List<EleccionDTO> findActivas() {
	  return eleccionDAO.findActivas().stream().map(eleccion -> new EleccionDTO(eleccion)).collect(Collectors.toList());
	}
	
	public List<EleccionDTO> findFinalizadas() {
	  return eleccionDAO.findFinalizadas().stream().map(eleccion -> new EleccionDTO(eleccion)).collect(Collectors.toList());
	}
	
	public EleccionDTO findById(Long id) throws NotFoundException {
	  Eleccion eleccion = eleccionDAO.findById(id);
	  if (eleccion == null) {
	    throw new NotFoundException(id.toString(), Eleccion.class);
	  }
	  return new EleccionDTO(eleccionDAO.findById(id));
	}
	
	public List<CandidaturaDTO> findCandidaturas(Long id) {
	  return eleccionDAO.findCandidaturas(id).stream().map(candidatura -> new CandidaturaDTO(candidatura)).collect(Collectors.toList());
	}
	
	// Con estas anotaciones evitamos que usuarios no autorizados accedan a ciertas
	// funcionalidades
	@PreAuthorize("hasAuthority('Admin')")
	@Transactional(readOnly = false)
	public EleccionDTO create(String nombre, String centro, String grupoCandidatos, String grupoVotantes, LocalDate fechaLimitePresentacion,
			LocalDate fechaLimiteVotacion, List<TitulacionDTO> titulaciones) throws FechasNoValidasException, FechaPasadaException {
	  Eleccion eleccion = new Eleccion();
	  eleccion.setNombre(nombre);
	  eleccion.setCentro(centro);
	  eleccion.setGrupoCandidatos(Grupos.valueOf(grupoCandidatos));
	  eleccion.setGrupoVotantes(Grupos.valueOf(grupoVotantes));
	  eleccion.setFechaLimitePresentacion(fechaLimitePresentacion);
	  eleccion.setFechaLimiteVotacion(fechaLimiteVotacion);
	  if (titulaciones != null) {
	   titulaciones.forEach(titulacion -> {
	      eleccion.getTitulaciones().add(titulacionDAO.findById(titulacion.getId()));
	    });
	  }
	  if (!eleccion.getFechaLimitePresentacion().isBefore(eleccion.getFechaLimiteVotacion())) {
		  throw new FechasNoValidasException();
	  }
	  if (!LocalDate.now().isBefore(eleccion.getFechaLimitePresentacion())) {
		  throw new FechaPasadaException();
	  }
	  eleccionDAO.create(eleccion);
	  return new EleccionDTO(eleccion);
	}
	
	@PreAuthorize("hasAuthority('Admin')")
	@Transactional(readOnly = false)
	public EleccionDTO update(EleccionDTO eleccion) throws AdelantarFechaException, FechasNoValidasException {
	  Eleccion bdEleccion = eleccionDAO.findById(eleccion.getId());
	  if ((eleccion.getFechaLimitePresentacion().isBefore(bdEleccion.getFechaLimitePresentacion())) ||
			  (eleccion.getFechaLimiteVotacion().isBefore(bdEleccion.getFechaLimiteVotacion()))) {
		  throw new AdelantarFechaException();
	  }
	  if (!eleccion.getFechaLimitePresentacion().isBefore(eleccion.getFechaLimiteVotacion())) {
		  throw new FechasNoValidasException();
	  }
	  bdEleccion.setFechaLimitePresentacion(eleccion.getFechaLimitePresentacion());
	  bdEleccion.setFechaLimiteVotacion(eleccion.getFechaLimiteVotacion());
	  eleccionDAO.update(bdEleccion);
	  return new EleccionDTO(bdEleccion);
	}
	
	@PreAuthorize("hasAuthority('Admin')")
	@Transactional(readOnly = false)
	public void deleteById(Long id) throws ElectionVotedException {
	  Eleccion eleccion = eleccionDAO.findById(id);
	  //Borramos todas las candidaturas de la elección que borramos
	  List<Candidatura> candidaturaLista = candidaturaDAO.findAll();
	  List<Candidatura> auxLista = new ArrayList<>();
	  for(Candidatura candidatura : candidaturaLista) {
		  if(candidatura.getEleccion().getNombre().equals(eleccion.getNombre())) {
			  if (candidatura.getVotantes().size() > 0) {
				  throw new ElectionVotedException(id.toString(), Eleccion.class);
			  } else {
				  auxLista.add(candidatura);
			  }
		  }
	  }
	  for(Candidatura aux : auxLista) {
		  candidaturaDAO.deleteById(aux.getId());
	  }
	  eleccionDAO.deleteById(id);
	}
}
