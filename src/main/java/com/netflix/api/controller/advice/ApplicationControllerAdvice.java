package com.netflix.api.controller.advice;

import com.netflix.api.exception.MovieException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Conseils globaux pour la gestion des exceptions des contrôleurs REST. */
@RestControllerAdvice
public class ApplicationControllerAdvice {

  private final MessageSource messageSource;

  public ApplicationControllerAdvice(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Appelée quand une exception métier liée aux radios/émissions survient. Renvoie un 400
   * BAD_REQUEST avec le message métier.
   */
  @ExceptionHandler({MovieException.class})
  public ResponseEntity<ErrorDto> businessException(Exception e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new ErrorDto(
                java.time.LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage()));
  }

  /**
   * Appelée quand une entité demandée est introuvable en base de données. Renvoie un 404 NOT_FOUND.
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorDto> entityNotFoundsException(EntityNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            new ErrorDto(
                java.time.LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), e.getMessage()));
  }

  /**
   * Appelée quand le JSON reçu est invalide ou ne peut pas être converti. Exemple : un champ
   * attendu en entier reçoit une chaîne de caractères. Renvoie un 400 BAD_REQUEST avec un message
   * explicite.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> notReadableException(HttpMessageNotReadableException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new ErrorDto(
                java.time.LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Json invalide (probleme de conversion de type) : " + e.getMessage()));
  }

  /**
   * Appelée quand la validation des arguments (@Valid) échoue. Renvoie un 400 BAD_REQUEST avec la
   * liste des erreurs de champs.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorsDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
    ErrorsDto errorsDto =
        new ErrorsDto(
            java.time.LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            ex.getBindingResult().getAllErrors().stream()
                .map(
                    error ->
                        new ErrorValidDto(
                            ((FieldError) error).getField(),
                            messageSource.getMessage(
                                error.getDefaultMessage(), null, LocaleContextHolder.getLocale())))
                .toList());

    return ResponseEntity.badRequest().body(errorsDto);
  }

  /**
   * Appelée en dernier recours pour toute exception non gérée explicitement. Renvoie un 400
   * BAD_REQUEST générique.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> ex(Exception e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new ErrorDto(
                java.time.LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage()));
  }
}
