package es.udc.asi.restexample.model.exception;

public class InvalidGroupException extends ModelException {
  public InvalidGroupException() {
    super("No perteneces al grupo permitido de esta elección");
  }
}
