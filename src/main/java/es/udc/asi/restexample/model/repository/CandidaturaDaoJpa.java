package es.udc.asi.restexample.model.repository;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import es.udc.asi.restexample.model.domain.Candidatura;
import es.udc.asi.restexample.model.domain.Usuario;
import es.udc.asi.restexample.model.repository.util.GenericDaoJpa;

@Repository
public class CandidaturaDaoJpa extends GenericDaoJpa implements CandidaturaDao {

  @Override
  public List<Candidatura> findAll() {
    return entityManager.createQuery("from Candidatura", Candidatura.class).getResultList();
  }

  @Override
  public Candidatura findById(Long id) {
    return entityManager.find(Candidatura.class, id);
  }

  @Override
  public void create(Candidatura candidatura) {
    entityManager.persist(candidatura);
  }
  
  @Override
  public void update(Candidatura candidatura) {
	entityManager.merge(candidatura);
  }
  
  private void delete(Candidatura candidatura) {
	entityManager.remove(candidatura);
  }
	
  @Override
  public void deleteById(Long id) {
	Candidatura candidatura = findById(id);
	delete(candidatura);
  }
}
