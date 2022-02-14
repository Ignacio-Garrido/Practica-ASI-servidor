package es.udc.asi.restexample.model.service.dto;

import javax.validation.constraints.NotEmpty;

import com.sun.istack.NotNull;

import es.udc.asi.restexample.model.domain.Candidatura;

public class CandidaturaDTO {
	private Long id;
	@NotEmpty
	private String motivo;
	private EleccionDTO eleccion;
	@NotNull
	private UsuarioDTOPublico candidato;
	private int numVotos;

	public CandidaturaDTO() {
	}

	public CandidaturaDTO(Candidatura candidatura) {
		this.id = candidatura.getId();
		this.motivo = candidatura.getMotivo();
		this.eleccion = new EleccionDTO(candidatura.getEleccion());
		this.candidato = new UsuarioDTOPublico(candidatura.getCandidato());
		// la lista de votantes no se rellena, ya que no se deben conocer los votos
		this.numVotos = candidatura.getNumVotos();
	}

	public Long getId() {
		return id;
	}

	public String getMotivo() {
		return motivo;
	}

	public EleccionDTO getEleccion() {
		return eleccion;
	}

	public UsuarioDTOPublico getCandidato() {
		return candidato;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public void setEleccion(EleccionDTO eleccion) {
		this.eleccion = eleccion;
	}

	public void setCandidato(UsuarioDTOPublico candidato) {
		this.candidato = candidato;
	}

	public int getNumVotos() {
		return numVotos;
	}

	public void setNumVotos(int numVotos) {
		this.numVotos = numVotos;
	}
	
}
