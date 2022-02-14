package es.udc.asi.restexample.model.exception;

public class FechaPasadaException extends ModelException {
  public FechaPasadaException() {
    super("La elección debe celebrarse en una fecha futura");
  }
}