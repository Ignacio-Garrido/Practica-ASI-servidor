package es.udc.asi.restexample.model.service.dto;

import javax.validation.constraints.NotEmpty;

import es.udc.asi.restexample.model.domain.Titulacion;

public class TitulacionDTO {
	private Long id;
	@NotEmpty
	private String nombre;
	  
	public TitulacionDTO() {
	}

	public TitulacionDTO(Titulacion titulacion) {
		this.id = titulacion.getId();
		this.nombre = titulacion.getNombre();
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
