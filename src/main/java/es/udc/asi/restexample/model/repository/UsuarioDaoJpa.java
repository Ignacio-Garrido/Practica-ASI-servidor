package es.udc.asi.restexample.model.repository;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import es.udc.asi.restexample.model.domain.Categoria;
import es.udc.asi.restexample.model.domain.Usuario;
import es.udc.asi.restexample.model.repository.util.GenericDaoJpa;

@Repository
public class UsuarioDaoJpa extends GenericDaoJpa implements UsuarioDao {

  public List<Usuario> findAll(String filter, String cat) {
    String queryStr = "select u from Usuario u";
    if (filter != null && cat != null) {
      queryStr += " where (lower(u.correo) like lower(:filter) or lower(CONCAT(u.nombre, ' ', u.apellido1, ' ', u.apellido2)) like lower(:filter)) AND (categoria = :cat)";
    } else if (filter != null ) {
    	queryStr += " where lower(u.correo) like lower(:filter) or lower(CONCAT(u.nombre, ' ', u.apellido1, ' ', u.apellido2)) like lower(:filter)";
    } else if (cat != null) {
    	queryStr += " where categoria = :cat";
    };

    TypedQuery<Usuario> query = entityManager.createQuery(queryStr, Usuario.class);
    if (filter != null && cat != null) {
      query.setParameter("filter", "%" + filter + "%").setParameter("cat", Categoria.valueOf(cat));
    } else if (filter != null ) {
    	query.setParameter("filter", "%" + filter + "%");
    } else if (cat != null) {
    	query.setParameter("cat", Categoria.valueOf(cat));
    };

    return query.getResultList();
  }


  @Override
  public Usuario findById(Long id) {
    return entityManager.find(Usuario.class, id);
  }

  @Override
  public Usuario findByCorreo(String correo) {
    TypedQuery<Usuario> query = entityManager.createQuery("from Usuario u where u.correo = :correo", Usuario.class)
        .setParameter("correo", correo);
    return DataAccessUtils.singleResult(query.getResultList());
  }

  @Override
  public void create(Usuario usuario) {
    entityManager.persist(usuario);
  }
  
  @Override
  public void update(Usuario usuario) {
	entityManager.merge(usuario);
  }
  
  private void delete(Usuario usuario) {
	entityManager.remove(usuario);
  }
	
  @Override
  public void deleteById(Long id) {
	Usuario usuario = findById(id);
	delete(usuario);
  }
}
