package es.udc.asi.restexample.model.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "Usuario")
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_generator")
  @SequenceGenerator(name = "usuario_generator", sequenceName = "usuario_seq")
  private Long id;

  @Column(unique = true)
  private String correo;

  private String contrasena;
  
  private String nombre;
  
  private String apellido1;
  
  private String apellido2;

  @Enumerated(EnumType.STRING)
  private Categoria categoria;
  
  private String imagen;
  
  @ManyToMany(fetch = FetchType.LAZY)
  private List<Titulacion> titulaciones = new ArrayList<>();
  
  @OneToMany(mappedBy = "candidato")
  private List<Candidatura> candidaturas = new ArrayList<>();
  
  @ManyToMany(mappedBy = "votantes")
  private List<Candidatura> votos = new ArrayList<>();

  public Usuario() {
  }
  
  public Usuario(String correo, String contrasena, String nombre, String apellido1, String apellido2, Categoria categoria) {
		super();
		this.correo = correo;
		this.contrasena = contrasena;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.categoria = categoria;
	}

  public Usuario(String correo, String contrasena, String nombre, String apellido1, String apellido2, Categoria categoria,
		String imagen) {
	super();
	this.correo = correo;
	this.contrasena = contrasena;
	this.nombre = nombre;
	this.apellido1 = apellido1;
	this.apellido2 = apellido2;
	this.categoria = categoria;
	this.imagen = imagen;
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
  
  public String getNombre() {
	  return nombre;
  }

  public void setNombre(String nombre) {
	  this.nombre = nombre;
  }

  public String getApellido1() {
	  return apellido1;
  }

  public void setApellido1(String apellido1) {
	  this.apellido1 = apellido1;
  }

  public String getApellido2() {
	  return apellido2;
  }

  public void setApellido2(String apellido2) {
	  this.apellido2 = apellido2;
  }

  public Categoria getCategoria() {
    return categoria;
  }

  public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
  }
  
  public String getImagen() {
	  return imagen;
  }

  public void setImagen(String imagen) {
	  this.imagen = imagen;
  }
  
  public List<Titulacion> getTitulaciones() {
	  return titulaciones;
  }

  public void setTitulaciones(List<Titulacion> titulaciones) {
	  this.titulaciones = titulaciones;
  }

  public List<Candidatura> getCandidaturas() {
	  return candidaturas;
  }

  public void setCandidaturas(List<Candidatura> candidaturas) {
	  this.candidaturas = candidaturas;
  }

  public List<Candidatura> getVotos() {
	  return votos;
  }
	
  public void setVotos(List<Candidatura> votos) {
	  this.votos = votos;
  }
  

}
