package es.udc.asi.restexample.model.service.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.sun.istack.NotNull;

import es.udc.asi.restexample.model.domain.Eleccion;

public class EleccionDTO {
	private Long id;
	@NotEmpty
	private String nombre;
	@NotEmpty
	private String centro;
	@NotNull
	private String grupoCandidatos;
	@NotNull
	private String grupoVotantes;
	@NotNull
	private LocalDate fechaLimitePresentacion;
	@NotNull
	private LocalDate fechaLimiteVotacion;
	@NotNull
	private List<TitulacionDTO> titulaciones = new ArrayList<>();
	
	public EleccionDTO() {
	}

	public EleccionDTO(Eleccion eleccion) {
		this.id = eleccion.getId();
		this.nombre = eleccion.getNombre();
		this.centro = eleccion.getCentro();
		this.grupoCandidatos = eleccion.getGrupoCandidatos().name();
		this.grupoVotantes = eleccion.getGrupoVotantes().name();
		this.fechaLimitePresentacion = eleccion.getFechaLimitePresentacion();
		this.fechaLimiteVotacion = eleccion.getFechaLimiteVotacion();
		eleccion.getTitulaciones().forEach(t -> {
			this.titulaciones.add(new TitulacionDTO(t));
		});
		this.titulaciones.sort(Comparator.comparing(TitulacionDTO::getNombre));
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

	public String getGrupoCandidatos() {
		return grupoCandidatos;
	}

	public String getGrupoVotantes() {
		return grupoVotantes;
	}

	public LocalDate getFechaLimitePresentacion() {
		return fechaLimitePresentacion;
	}

	public LocalDate getFechaLimiteVotacion() {
		return fechaLimiteVotacion;
	}

	public List<TitulacionDTO> getTitulaciones() {
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

	public void setGrupoCandidatos(String grupoCandidatos) {
		this.grupoCandidatos = grupoCandidatos;
	}

	public void setGrupoVotantes(String grupoVotantes) {
		this.grupoVotantes = grupoVotantes;
	}

	public void setFechaLimitePresentacion(LocalDate fechaLimitePresentacion) {
		this.fechaLimitePresentacion = fechaLimitePresentacion;
	}

	public void setFechaLimiteVotacion(LocalDate fechaLimiteVotacion) {
		this.fechaLimiteVotacion = fechaLimiteVotacion;
	}

	public void setTitulaciones(List<TitulacionDTO> titulaciones) {
		this.titulaciones = titulaciones;
	}
}
