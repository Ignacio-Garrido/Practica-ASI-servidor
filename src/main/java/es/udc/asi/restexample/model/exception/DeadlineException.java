package es.udc.asi.restexample.model.exception;

public class DeadlineException extends ModelException {
  public DeadlineException(String id, Class<?> clazz, String accion) {
    super(clazz.getSimpleName() + " con id " + id + " no se puede " + accion + " porque no está dentro del plazo");
  }
}
