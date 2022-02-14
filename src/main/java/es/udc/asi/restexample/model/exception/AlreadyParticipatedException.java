package es.udc.asi.restexample.model.exception;

public class AlreadyParticipatedException extends ModelException {
  public AlreadyParticipatedException(String accion, String id, Class<?> clazz) {
    super("Ya " + accion + " en la " + clazz.getSimpleName() + " con id " + id);
  }
}