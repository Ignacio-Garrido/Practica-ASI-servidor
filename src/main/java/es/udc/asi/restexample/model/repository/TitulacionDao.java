package es.udc.asi.restexample.model.repository;

import java.util.List;

import es.udc.asi.restexample.model.domain.Titulacion;

public interface TitulacionDao {

	List<Titulacion> findAll();

	Titulacion findById(Long id);

	Titulacion findByNombre(String nombre);

	void create(Titulacion titulacion);
	  
	void update(Titulacion titulacion);

	void deleteById(Long id);
	  
}
