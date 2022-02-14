package es.udc.asi.restexample.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.udc.asi.restexample.model.exception.ModelException;
import es.udc.asi.restexample.model.exception.NotFoundException;
import es.udc.asi.restexample.web.exceptions.CredentialsAreNotValidException;
import es.udc.asi.restexample.web.exceptions.ResourceException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
  private final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ErrorDTO notFoundExceptionHandler(NotFoundException e) {
    logger.info(e.getMessage(), e);
    return new ErrorDTO(e.getMessage());
  }

  @ExceptionHandler(CredentialsAreNotValidException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public ErrorDTO badCredentialsExceptionHandler(CredentialsAreNotValidException e) {
    logger.info(e.getMessage(), e);
    return new ErrorDTO("Bad Credentials");
  }

  @ExceptionHandler({ ModelException.class, ResourceException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorDTO badRequestExceptionHandler(Exception e) {
    logger.info(e.getMessage(), e);
    return new ErrorDTO(e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ErrorDTO exceptionHandler(Exception e) {
    logger.error(e.getMessage(), e);
    return new ErrorDTO(e.getMessage());
  }
}
