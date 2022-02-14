package es.udc.asi.restexample.model.repository;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import es.udc.asi.restexample.model.domain.Titulacion;
import es.udc.asi.restexample.model.repository.util.GenericDaoJpa;

@Repository
public class TitulacionDaoJpa extends GenericDaoJpa implements TitulacionDao {
	@Override
	public List<Titulacion> findAll() {
	  return entityManager.createQuery("from Titulacion", Titulacion.class).getResultList();
	}

	@Override
	public Titulacion findById(Long id) {
	  return entityManager.find(Titulacion.class, id);
	}
	
	@Override
	public Titulacion findByNombre(String nombre) {
	  try {
		  return entityManager.createQuery("from Titulacion where lower(nombre) = lower(:nombre)", Titulacion.class).setParameter("nombre", nombre).getSingleResult();
	  } catch (NoResultException err) {
		  return null;
	  }
	}

	@Override
	public void create(Titulacion titulacion) {
	  entityManager.persist(titulacion);
	}
	  
	@Override
	public void update(Titulacion titulacion) {
	  entityManager.merge(titulacion);
	}

	private void delete(Titulacion titulacion) {
	  entityManager.remove(titulacion);
	}

	@Override
	public void deleteById(Long id) {
	  Titulacion titulacion = findById(id);
	  delete(titulacion);
	}
}
