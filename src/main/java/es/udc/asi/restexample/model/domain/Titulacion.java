package es.udc.asi.restexample.model.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Titulacion {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "titulacion_generator")
	@SequenceGenerator(name = "titulacion_generator", sequenceName = "titulacion_seq")
	private Long id;
	
	@Column(unique = true)
	private String nombre;
	
	public Titulacion() {
	}
	
	public Titulacion(String nombre) {
		this.nombre = nombre;
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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Titulacion other = (Titulacion) obj;
		return Objects.equals(id, other.id);
	}
	
}
