package es.udc.asi.restexample.web;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.udc.asi.restexample.model.domain.Eleccion;
import es.udc.asi.restexample.model.domain.Usuario;
import es.udc.asi.restexample.model.exception.AdelantarFechaException;
import es.udc.asi.restexample.model.exception.AlreadyParticipatedException;
import es.udc.asi.restexample.model.exception.DeadlineException;
import es.udc.asi.restexample.model.exception.ElectionVotedException;
import es.udc.asi.restexample.model.exception.FechaPasadaException;
import es.udc.asi.restexample.model.exception.FechasNoValidasException;
import es.udc.asi.restexample.model.exception.InvalidGroupException;
import es.udc.asi.restexample.model.exception.NotFoundException;
import es.udc.asi.restexample.model.service.CandidaturaService;
import es.udc.asi.restexample.model.service.EleccionService;
import es.udc.asi.restexample.model.service.dto.CandidaturaDTO;
import es.udc.asi.restexample.model.service.dto.EleccionDTO;
import es.udc.asi.restexample.web.exceptions.IdAndBodyNotMatchingOnUpdateException;
import es.udc.asi.restexample.web.exceptions.RequestBodyNotValidException;

@RestController
@RequestMapping("/api/elecciones")
public class EleccionResource {
	
	@Autowired
	private EleccionService eleccionService;
	
	@Autowired
	private CandidaturaService candidaturaService;
	
	@GetMapping
	public List<EleccionDTO> findAll() {
	  return eleccionService.findAll();
	}
	
	@GetMapping("/proximas")
	public List<EleccionDTO> findProximas() {
	  return eleccionService.findProximas();
	}
	
	@GetMapping("/activas")
	public List<EleccionDTO> findActivas() {
	  return eleccionService.findActivas();
	}
	
	@GetMapping("/finalizadas")
	public List<EleccionDTO> findFinalizadas() {
	  return eleccionService.findFinalizadas();
	}

	@GetMapping("/{id}")
	public EleccionDTO findOne(@PathVariable Long id) throws NotFoundException {
	  return eleccionService.findById(id);
	}
	
	@GetMapping("/{id}/candidaturas")
	public List<CandidaturaDTO> findCandidaturas(@PathVariable Long id) {
	  return eleccionService.findCandidaturas(id);
	}

	@PostMapping
	public EleccionDTO create(@RequestBody @Valid EleccionDTO eleccion, Errors errors) throws RequestBodyNotValidException, FechasNoValidasException, FechaPasadaException {
	  if (errors.hasErrors()) {
	    throw new RequestBodyNotValidException(errors);
	  }
	  return eleccionService.create(eleccion.getNombre(), eleccion.getCentro(), eleccion.getGrupoCandidatos(), eleccion.getGrupoVotantes(), eleccion.getFechaLimitePresentacion(), eleccion.getFechaLimiteVotacion(), eleccion.getTitulaciones());
	}

	@PutMapping("/{id}")
	public EleccionDTO update(@PathVariable Long id, @RequestBody @Valid EleccionDTO eleccion, Errors errors)
	    throws IdAndBodyNotMatchingOnUpdateException, RequestBodyNotValidException, AdelantarFechaException, FechasNoValidasException {
	  if (errors.hasErrors()) {
	    throw new RequestBodyNotValidException(errors);
	  }
	  if (id != eleccion.getId()) {
	    throw new IdAndBodyNotMatchingOnUpdateException(Eleccion.class);
	  }
	  return eleccionService.update(eleccion);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) throws ElectionVotedException {
	  eleccionService.deleteById(id);
	}
	
	@PostMapping("/{id}/candidatura")
	public CandidaturaDTO create(@PathVariable Long id, @RequestBody @Valid CandidaturaDTO candidatura, Errors errors) throws RequestBodyNotValidException, DeadlineException, InvalidGroupException, NotFoundException, AlreadyParticipatedException {
	  if (errors.hasErrors()) {
	    throw new RequestBodyNotValidException(errors);
	  }
	  EleccionDTO eleccion = eleccionService.findById(id);
	  candidatura.setEleccion(eleccion);
	  return candidaturaService.create(candidatura);
	}

}
