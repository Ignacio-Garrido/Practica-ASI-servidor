package es.udc.asi.restexample.model.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class Candidatura {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidatura_generator")
	@SequenceGenerator(name = "candidatura_generator", sequenceName = "candidatura_seq")
	private Long id;
	
	private String motivo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Eleccion eleccion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario candidato;
	
	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Usuario> votantes = new HashSet<>();
	
	private int numVotos;
	
	public Candidatura() {
	}

	public Candidatura(String motivo, Eleccion eleccion, Usuario candidato) {
		this.motivo = motivo;
		this.eleccion = eleccion;
		this.candidato = candidato;
		this.numVotos = 0;
	}

	public Candidatura(String motivo, Eleccion eleccion, Usuario candidato, Set<Usuario> votantes, int numVotos) {
		this.motivo = motivo;
		this.eleccion = eleccion;
		this.candidato = candidato;
		this.votantes = votantes;
		this.numVotos = numVotos;
	}

	public Long getId() {
		return id;
	}

	public String getMotivo() {
		return motivo;
	}

	public Eleccion getEleccion() {
		return eleccion;
	}

	public Usuario getCandidato() {
		return candidato;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public void setEleccion(Eleccion eleccion) {
		this.eleccion = eleccion;
	}

	public void setCandidato(Usuario candidato) {
		this.candidato = candidato;
	}

	public Set<Usuario> getVotantes() {
		return votantes;
	}

	public void setVotantes(Set<Usuario> votantes) {
		this.votantes = votantes;
	}

	public int getNumVotos() {
		return numVotos;
	}

	public void setNumVotos(int numVotos) {
		this.numVotos = numVotos;
	}
	
}
