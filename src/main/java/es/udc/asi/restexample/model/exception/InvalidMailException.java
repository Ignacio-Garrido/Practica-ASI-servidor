package es.udc.asi.restexample.model.exception;

public class InvalidMailException extends ModelException {
  public InvalidMailException(String correo) {
    super(correo + " no es un correo válido");
  }
}