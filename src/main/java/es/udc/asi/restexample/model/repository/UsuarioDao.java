package es.udc.asi.restexample.model.repository;

import java.util.List;

import es.udc.asi.restexample.model.domain.Usuario;

public interface UsuarioDao {
  List<Usuario> findAll(String filter, String cat);

  Usuario findById(Long id);

  Usuario findByCorreo(String correo);

  void create(Usuario usuario);
  
  void update(Usuario usuario);

  void deleteById(Long id);
}
