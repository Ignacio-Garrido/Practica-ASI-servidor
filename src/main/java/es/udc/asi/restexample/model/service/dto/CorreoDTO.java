package es.udc.asi.restexample.model.service.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CorreoDTO {
  @NotEmpty
  @Size(min = 6)
  private String correo;

  @NotEmpty
  private String contrasena;

  private String nuevaContrasena;

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
	
	public String getNuevaContrasena() {
		return nuevaContrasena;
	}
	
	public void setNuevaContrasena(String nuevaContrasena) {
		this.nuevaContrasena = nuevaContrasena;
	}
}
