package com.netflix.api.exception;

public class MovieDuplicateException extends RuntimeException {
  public MovieDuplicateException(String message) {
    super(message);
  }
}
