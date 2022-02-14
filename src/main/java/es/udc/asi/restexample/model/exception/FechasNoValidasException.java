package es.udc.asi.restexample.model.exception;

public class FechasNoValidasException extends ModelException {
  public FechasNoValidasException() {
    super("La fecha límite de votación no puede ser anterior a la de candidaturas");
  }
}