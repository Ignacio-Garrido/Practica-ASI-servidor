package es.udc.asi.restexample.web.exceptions;

public class IdAndBodyNotMatchingOnUpdateException extends ResourceException {
  public IdAndBodyNotMatchingOnUpdateException(Class<?> clazz) {
    super(
        "On update, the sent item and the id on the request must be the same. Happening with " + clazz.getSimpleName());
  }
}
