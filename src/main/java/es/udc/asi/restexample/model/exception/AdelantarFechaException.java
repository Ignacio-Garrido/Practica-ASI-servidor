package es.udc.asi.restexample.model.exception;

public class AdelantarFechaException extends ModelException {
  public AdelantarFechaException() {
    super("No se pueden adelantar las fechas límite");
  }
}