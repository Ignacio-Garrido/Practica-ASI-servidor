package es.udc.asi.restexample.model.exception;

public class UserCorreoExistsException extends ModelException {
  public UserCorreoExistsException(String correo) {
    super("El usuario con el correo " + correo + " ya existe");
  }
}
