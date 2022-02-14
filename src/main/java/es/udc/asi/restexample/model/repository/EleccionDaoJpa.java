package es.udc.asi.restexample.model.repository;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import es.udc.asi.restexample.model.domain.Candidatura;
import es.udc.asi.restexample.model.domain.Eleccion;
import es.udc.asi.restexample.model.repository.util.GenericDaoJpa;

@Repository
public class EleccionDaoJpa extends GenericDaoJpa implements EleccionDao {

	@Override
	public List<Eleccion> findAll() {
	  return entityManager.createQuery("from Eleccion", Eleccion.class).getResultList();
	}
	
	@Override
	public List<Eleccion> findProximas() {
		  LocalDate fecha = LocalDate.now();
		  return entityManager.createQuery("from Eleccion e where e.fechaLimitePresentacion >= :fecha order by e.fechaLimitePresentacion ASC", Eleccion.class).setParameter("fecha", fecha).getResultList();
	}
	
	@Override
	public List<Eleccion> findActivas() {
		  LocalDate fecha = LocalDate.now();
		  return entityManager.createQuery("from Eleccion e where e.fechaLimitePresentacion < :fecha AND e.fechaLimiteVotacion >= :fecha order by e.fechaLimiteVotacion ASC", Eleccion.class).setParameter("fecha", fecha).getResultList();
	}
	
	@Override
	public List<Eleccion> findFinalizadas() {
	  LocalDate fecha = LocalDate.now();
	  return entityManager.createQuery("from Eleccion e where e.fechaLimiteVotacion < :fecha order by e.fechaLimiteVotacion DESC", Eleccion.class).setParameter("fecha", fecha).getResultList();
	}
	
	@Override
	public Eleccion findById(Long id) {
	  return entityManager.find(Eleccion.class, id);
	}
	
	@Override
	public Eleccion findByNombre(String nombre) {
		TypedQuery<Eleccion> query = entityManager.createQuery("from Eleccion e where e.nombre = :nombre", Eleccion.class)
		     .setParameter("nombre", nombre);
		return DataAccessUtils.singleResult(query.getResultList());
	}
	
	@Override
	public List<Candidatura> findCandidaturas(Long id) {
	  return entityManager.createQuery("from Candidatura where eleccion.id = :id", Candidatura.class).setParameter("id", id).getResultList();
	}
	
	@Override
	public void create(Eleccion eleccion) {
	  entityManager.persist(eleccion);
	}
	
	@Override
	public void update(Eleccion eleccion) {
	  entityManager.merge(eleccion);
	}
	
	private void delete(Eleccion eleccion) {
	  entityManager.remove(eleccion);
	}
	
	@Override
	public void deleteById(Long id) {
	  Eleccion eleccion = findById(id);
	  delete(eleccion);
	}
}
