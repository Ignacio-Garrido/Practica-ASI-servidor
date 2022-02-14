package es.udc.asi.restexample.model.service.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import es.udc.asi.restexample.model.domain.Usuario;

public class UsuarioDTOPrivado {
  private Long id;
  
  @NotEmpty
  @Size(min = 6)
  private String correo;
  @NotEmpty
  private String contrasena;
  @NotEmpty
  private String nombre;
  @NotEmpty
  private String apellido1;
  private String apellido2; 
  @NotEmpty
  private String categoria;
  private boolean imagen;
  private List<TitulacionDTO> titulaciones = new ArrayList<>();

  public UsuarioDTOPrivado() {
  }

  public UsuarioDTOPrivado(Usuario usuario) {
    this.id = usuario.getId();
    this.correo = usuario.getCorreo();
    // la contraseña no se rellena, nunca se envía al cliente
    this.nombre = usuario.getNombre();
    this.apellido1 = usuario.getApellido1();
    this.apellido2 = usuario.getApellido2();
    this.categoria = usuario.getCategoria().toString();
    if (usuario.getImagen() != null) {
    	this.imagen = true;
    };
    usuario.getTitulaciones().forEach(t -> {
		this.titulaciones.add(new TitulacionDTO(t));
	});
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getContrasena() {
    return contrasena;
  }

  public void setContrasena(String contrasena) {
    this.contrasena = contrasena;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

	public String getNombre() {
		return nombre;
	}
	
	public String getApellido1() {
		return apellido1;
	}
	
	public String getApellido2() {
		return apellido2;
	}
	
	public boolean getImagen() {
		return imagen;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	
	public void setImagen(boolean imagen) {
		this.imagen = imagen;
	}

	public List<TitulacionDTO> getTitulaciones() {
		return titulaciones;
	}

	public void setTitulaciones(List<TitulacionDTO> titulaciones) {
		this.titulaciones = titulaciones;
	}
  
}
