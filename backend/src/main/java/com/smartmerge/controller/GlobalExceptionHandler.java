package com.smartmerge.controller;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.smartmerge.exception.ExceptionResponse;
import com.smartmerge.exception.InstallationNotFoundException;
import com.smartmerge.exception.ProfileNotFoundException;
import com.smartmerge.exception.PullRequestNotFoundException;
import com.smartmerge.exception.RepoNotFoundException;

import lombok.extern.slf4j.Slf4j;

/* Handles errors for the whole app in one place so individual controllers don't have to.
   When an endpoint throws, the matching method below turns the exception into a clean JSON
   response with the right HTTP status code.

   "extends ResponseEntityExceptionHandler" gives us Spring's built-in handling for common
   request mistakes for free. e.g. a non numeric path variable, a malformed JSON body, or the
   wrong HTTP method. Spring already knows those should return a 4xx code.
   Without extending it, they'd fall through to our catch-all handleException() below and wrongly
   come back as 500. */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleProfileNotFoundException(ProfileNotFoundException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(PullRequestNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handlePullRequestNotFoundException(PullRequestNotFoundException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(InstallationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleInstallationNotFoundException(InstallationNotFoundException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(RepoNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRepoNotFoundException(RepoNotFoundException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponse> handleResponseStatusException(ResponseStatusException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getReason(), LocalDateTime.now());
        return ResponseEntity.status(e.getStatusCode()).body(exceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        log.error("Unhandled exception", e);
        ExceptionResponse exceptionResponse = new ExceptionResponse("An unexpected error occurred.", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }
}
