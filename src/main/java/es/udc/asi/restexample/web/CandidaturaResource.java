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
import es.udc.asi.restexample.model.exception.AlreadyParticipatedException;
import es.udc.asi.restexample.model.exception.DeadlineException;
import es.udc.asi.restexample.model.exception.InvalidGroupException;
import es.udc.asi.restexample.model.exception.NotFoundException;
import es.udc.asi.restexample.model.service.CandidaturaService;
import es.udc.asi.restexample.model.service.dto.CandidaturaDTO;
import es.udc.asi.restexample.model.service.dto.UsuarioDTOPublico;
import es.udc.asi.restexample.web.exceptions.IdAndBodyNotMatchingOnUpdateException;
import es.udc.asi.restexample.web.exceptions.RequestBodyNotValidException;

@RestController
@RequestMapping("/api/candidaturas")
public class CandidaturaResource {
	
	@Autowired
	private CandidaturaService candidaturaService;
	
	@GetMapping
	public List<CandidaturaDTO> findAll() {
	  return candidaturaService.findAll();
	}

	@GetMapping("/{id}")
	public CandidaturaDTO findOne(@PathVariable Long id) throws NotFoundException {
	  return candidaturaService.findById(id);
	}

	@PostMapping
	public CandidaturaDTO create(@RequestBody @Valid CandidaturaDTO candidatura, Errors errors) throws RequestBodyNotValidException, DeadlineException, InvalidGroupException, AlreadyParticipatedException {
	  if (errors.hasErrors()) {
	    throw new RequestBodyNotValidException(errors);
	  }
	  return candidaturaService.create(candidatura);
	}

	@PutMapping("/{id}")
	public CandidaturaDTO update(@PathVariable Long id, @RequestBody @Valid CandidaturaDTO candidatura, Errors errors)
	    throws IdAndBodyNotMatchingOnUpdateException, RequestBodyNotValidException, DeadlineException {
	  if (errors.hasErrors()) {
	    throw new RequestBodyNotValidException(errors);
	  }
	  if (id != candidatura.getId()) {
	    throw new IdAndBodyNotMatchingOnUpdateException(Eleccion.class);
	  }
	  return candidaturaService.update(candidatura);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) throws DeadlineException{
	  candidaturaService.deleteById(id);
	}
	
	@PutMapping("/{id}/votar")
	public CandidaturaDTO votar(@PathVariable @Valid Long id, @Valid @RequestBody UsuarioDTOPublico usuario, Errors errors) throws RequestBodyNotValidException, DeadlineException, InvalidGroupException, AlreadyParticipatedException {
	  if (errors.hasErrors()) {
	    throw new RequestBodyNotValidException(errors);
	  }
	  return candidaturaService.votar(id, usuario);
	}
}
