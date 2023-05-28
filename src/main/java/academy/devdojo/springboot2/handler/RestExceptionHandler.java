package academy.devdojo.springboot2.handler;

import java.io.ObjectInputStream.GetField;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.exception.BadRequestExceptionDetails;
import academy.devdojo.springboot2.exception.ValidationExceptionDetails;
import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class RestExceptionHandler {
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException bre) {
    return new ResponseEntity<>(
        BadRequestExceptionDetails.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .title("Bad Request Exception, Check the documentation")
            .details(bre.getMessage())
            .developerMessage(bre.getClass().getName())
            .build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationExceptionDetails> handlerMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {

    List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
    String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
    String fieldsMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

    return new ResponseEntity<>(
        ValidationExceptionDetails.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .title("Invalid Fields Exception, Check the documentation")
            .details("Check the field(s) error")
            .developerMessage(exception.getClass().getName())
            .fields(fields)
            .fieldsMessage(fieldsMessages)
            .build(),
        HttpStatus.BAD_REQUEST);
  }
}
