package com.netflix.api.controller.advice;

import com.netflix.api.exception.MovieDuplicateException;
import com.netflix.api.exception.MovieException;
import com.netflix.api.utils.Messages;
import jakarta.persistence.EntityNotFoundException;
import java.time.ZoneId;
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
                java.time.LocalDateTime.now(ZoneId.systemDefault()),
                HttpStatus.BAD_REQUEST.value(),
                messageSource.getMessage(e.getMessage(), null, LocaleContextHolder.getLocale())));
  }

  /**
   * Appelée quand une entité demandée est introuvable en base de données. Renvoie un 404 NOT_FOUND.
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorDto> entityNotFoundsException(EntityNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            new ErrorDto(
                java.time.LocalDateTime.now(ZoneId.systemDefault()),
                HttpStatus.NOT_FOUND.value(),
                messageSource.getMessage(e.getMessage(), null, LocaleContextHolder.getLocale())));
  }

  /**
   * Appelée quand le JSON reçu est invalide ou ne peut pas être converti. Exemple : un champ
   * attendu en entier reçoit une chaîne de caractères. Renvoie un 400 BAD_REQUEST avec un message
   * explicite.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> notReadableException(HttpMessageNotReadableException e) {
    String message = "Json invalide (probleme de conversion de type) : " + e.getMessage();

    if (e.getMessage() != null && e.getMessage().contains("EGenre"))
      message =
          messageSource.getMessage(
              Messages.MOVIE_GENRE_INVALID, null, LocaleContextHolder.getLocale());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new ErrorDto(
                java.time.LocalDateTime.now(ZoneId.systemDefault()),
                HttpStatus.BAD_REQUEST.value(),
                message));
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
            java.time.LocalDateTime.now(ZoneId.systemDefault()),
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
   * Appelée lorsqu'une tentative est faite de créer un film déjà existant. Renvoie un 409 CONFLICT
   * indiquant qu'un film avec le même titre et la même date de sortie est déjà présent en base.
   */
  @ExceptionHandler(MovieDuplicateException.class)
  public ResponseEntity<ErrorDto> movieDuplicateException(MovieDuplicateException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(
            new ErrorDto(
                java.time.LocalDateTime.now(ZoneId.systemDefault()),
                HttpStatus.CONFLICT.value(),
                e.getMessage()));
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
                java.time.LocalDateTime.now(ZoneId.systemDefault()),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage()));
  }
}
