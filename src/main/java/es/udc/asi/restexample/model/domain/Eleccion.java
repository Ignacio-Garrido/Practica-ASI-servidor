package es.udc.asi.restexample.model.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class Eleccion {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eleccion_generator")
	@SequenceGenerator(name = "eleccion_generator", sequenceName = "eleccion_seq")
	private Long id;
	
	private String nombre;
	
	private String centro;
	
	private Grupos grupoCandidatos;
	
	private Grupos grupoVotantes;
	
	private LocalDate fechaLimitePresentacion;
	
	private LocalDate fechaLimiteVotacion;
	
	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Titulacion> titulaciones = new HashSet<>();
	
	public Eleccion() {
	}

	public Eleccion(String nombre, String centro, Grupos grupoCandidatos, Grupos grupoVotantes,
			LocalDate fechaLimitePresentacion, LocalDate fechaLimiteVotacion, Set<Titulacion> titulaciones) {
		super();
		this.nombre = nombre;
		this.centro = centro;
		this.grupoCandidatos = grupoCandidatos;
		this.grupoVotantes = grupoVotantes;
		this.fechaLimitePresentacion = fechaLimitePresentacion;
		this.fechaLimiteVotacion = fechaLimiteVotacion;
		this.titulaciones = titulaciones;
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getCentro() {
		return centro;
	}

	public Grupos getGrupoCandidatos() {
		return grupoCandidatos;
	}

	public Grupos getGrupoVotantes() {
		return grupoVotantes;
	}

	public LocalDate getFechaLimitePresentacion() {
		return fechaLimitePresentacion;
	}

	public LocalDate getFechaLimiteVotacion() {
		return fechaLimiteVotacion;
	}

	public Set<Titulacion> getTitulaciones() {
		return titulaciones;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setCentro(String centro) {
		this.centro = centro;
	}

	public void setGrupoCandidatos(Grupos grupoCandidatos) {
		this.grupoCandidatos = grupoCandidatos;
	}

	public void setGrupoVotantes(Grupos grupoVotantes) {
		this.grupoVotantes = grupoVotantes;
	}

	public void setFechaLimitePresentacion(LocalDate fechaLimitePresentacion) {
		this.fechaLimitePresentacion = fechaLimitePresentacion;
	}

	public void setFechaLimiteVotacion(LocalDate fechaLimiteVotacion) {
		this.fechaLimiteVotacion = fechaLimiteVotacion;
	}

	public void setTitulaciones(Set<Titulacion> titulaciones) {
		this.titulaciones = titulaciones;
	}
	
	
}
