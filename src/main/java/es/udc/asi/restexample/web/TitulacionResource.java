package es.udc.asi.restexample.web;

import java.util.List;

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

import es.udc.asi.restexample.model.domain.Titulacion;
import es.udc.asi.restexample.model.exception.NotFoundException;
import es.udc.asi.restexample.model.exception.TitulacionNombreExistsException;
import es.udc.asi.restexample.model.service.TitulacionService;
import es.udc.asi.restexample.model.service.dto.TitulacionDTO;
import es.udc.asi.restexample.model.service.dto.UsuarioDTOPublico;
import es.udc.asi.restexample.web.exceptions.IdAndBodyNotMatchingOnUpdateException;
import es.udc.asi.restexample.web.exceptions.RequestBodyNotValidException;

@RestController
@RequestMapping("/api/titulaciones")
public class TitulacionResource {

  @Autowired
  private TitulacionService titulacionService;

  @GetMapping
  public List<TitulacionDTO> findAll() {
    return titulacionService.findAll();
  }
  
  @GetMapping("/{id}")
  public TitulacionDTO findOne(@PathVariable Long id) throws NotFoundException {
    return titulacionService.findById(id);
  }
  
  @GetMapping("/{nombre}/nombre")
  public TitulacionDTO findByNombre(@PathVariable String nombre) throws NotFoundException {
    return titulacionService.findByNombre(nombre);
  }
  
  @GetMapping("/{id}/usuarios")
  public List<UsuarioDTOPublico> findByTitulacionId(@PathVariable Long id) {
    return titulacionService.findByTitulacionId(id);
  }

  @PostMapping
  public TitulacionDTO create(@RequestBody @Valid TitulacionDTO titulacion, Errors errors) throws RequestBodyNotValidException, TitulacionNombreExistsException {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }

    return titulacionService.create(titulacion);
  }

  @PutMapping("/{id}")
  public TitulacionDTO update(@PathVariable Long id, @RequestBody @Valid TitulacionDTO titulacion, Errors errors)
      throws IdAndBodyNotMatchingOnUpdateException, RequestBodyNotValidException {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }

    if (id != titulacion.getId()) {
      throw new IdAndBodyNotMatchingOnUpdateException(Titulacion.class);
    }
    return titulacionService.update(titulacion);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    titulacionService.deleteById(id);
  }
}

