package es.udc.asi.restexample.model.exception;

public class ElectionVotedException extends ModelException {
  public ElectionVotedException(String id, Class<?> clazz) {
    super(clazz.getSimpleName() + " con id " + id + " no puede ser eliminada porque ya hay votaciones");
  }
}