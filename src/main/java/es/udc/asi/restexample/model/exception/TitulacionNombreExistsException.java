package es.udc.asi.restexample.model.exception;

public class TitulacionNombreExistsException extends ModelException {
  public TitulacionNombreExistsException(String nombre) {
    super("La titulación " + nombre + " ya existe");
  }
}
