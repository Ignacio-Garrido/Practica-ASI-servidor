package es.udc.asi.restexample.model.repository;

import java.util.List;

import es.udc.asi.restexample.model.domain.Candidatura;
import es.udc.asi.restexample.model.domain.Eleccion;

public interface EleccionDao {
	List<Eleccion> findAll();

	List<Eleccion> findProximas();

	List<Eleccion> findActivas();

	List<Eleccion> findFinalizadas();

	Eleccion findById(Long id);
	
	Eleccion findByNombre(String nombre);
	
	List<Candidatura> findCandidaturas(Long id);

	void create(Eleccion eleccion);

	void update(Eleccion eleccion);

	void deleteById(Long id);
}
