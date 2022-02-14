package es.udc.asi.restexample.model.repository;

import java.util.List;
import java.util.Set;

import es.udc.asi.restexample.model.domain.Candidatura;
import es.udc.asi.restexample.model.domain.Usuario;

public interface CandidaturaDao {
  List<Candidatura> findAll();
  
  Candidatura findById(Long id);

  void create(Candidatura candidatura);
  
  void update(Candidatura candidatura);
  
  void deleteById(Long id);
}
