package es.udc.asi.restexample.model.exception;

public class SelfUserException extends ModelException {
  public SelfUserException() {
    super("You can't delete yourself");
  }
}