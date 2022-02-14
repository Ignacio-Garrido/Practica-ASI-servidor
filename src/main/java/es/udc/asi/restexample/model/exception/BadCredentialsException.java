package es.udc.asi.restexample.model.exception;

public class BadCredentialsException extends ModelException {
  public BadCredentialsException(String id, Class<?> clazz) {
    super("La contraseña del " + clazz.getSimpleName() + " con id " + id + " no es correcta");
  }
}