package es.udc.asi.restexample.model.service.dto;

import java.io.InputStream;

public class ImagenDTO {
	private InputStream inputStream;
	private String tipo;
	private String nombreFichero;
  
	public ImagenDTO(InputStream inputStream, String tipo, String nombreFichero) {
		this.inputStream = inputStream;
		this.tipo = tipo;
		this.nombreFichero = nombreFichero;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public String getTipo() {
		return tipo;
	}

	public String getNombreFichero() {
		return nombreFichero;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}
	

}
