package es.udc.asi.restexample.model.exception;

public class UsuarioSinTitulacionesException extends ModelException {
  public UsuarioSinTitulacionesException(String correo) {
    super("El usuario " + correo + " no puede quedar sin titulaciones asociadas");
  }
}