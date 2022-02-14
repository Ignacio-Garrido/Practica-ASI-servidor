package es.udc.asi.restexample.web.exceptions;

public class CredentialsAreNotValidException extends ResourceException {

  public CredentialsAreNotValidException(String errorMsg) {
    super(errorMsg);
  }
}
