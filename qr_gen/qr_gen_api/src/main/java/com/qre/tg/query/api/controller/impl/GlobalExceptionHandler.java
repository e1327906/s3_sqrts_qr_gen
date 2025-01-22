
package com.qre.tg.query.api.controller.impl;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.qre.cmel.exception.*;
import com.qre.tg.dto.base.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.qre.cmel.exception.ExceptionMsg;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles creation of new user with an existing username in database
     */
    @ExceptionHandler({UserAlreadyExistsException.class})
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        return getResponseEntity(HttpStatus.CONFLICT, exception);
    }

    /**
     * Handles exceptions for invalid password change request
     */
    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception) {
        return getResponseEntity(HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler({OldPasswordIncorrectException.class})
    public ResponseEntity<Object> handleOldPasswordIncorrectException(OldPasswordIncorrectException exception) {
        return getResponseEntity(HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler({PasswordMismatchException.class})
    public ResponseEntity<Object> handlePasswordMismatchException(PasswordMismatchException exception) {
        return getResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, exception);
    }

    /**
     * Handles wrong date input format
     */
    @ExceptionHandler({DateParseException.class})
    public ResponseEntity<Object> handleDateParseException(DateParseException exception) {
        return getResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, exception);
    }

    /**
     * Handles invalid login
     */
    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exception) {
        exception.printStackTrace();
        logger.error(exception.getMessage());
        APIResponse apiResponse = new APIResponse(String.valueOf(HttpStatus.UNAUTHORIZED.value()),
                ExceptionMsg.INVALID_LOGIN, HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
    }

    /**
     * Handle exception in query of train or bus fare due to fare not present in table
     */
    @ExceptionHandler({FareNotFoundException.class})
    public ResponseEntity<Object> handleFareNotFoundException(FareNotFoundException exception) {
        return getResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, exception);
    }

    /**
     * Handles invalid input validation errors
     */
    @ExceptionHandler({TransactionSystemException.class})
    public ResponseEntity<Object> handleTransactionSystemException(TransactionSystemException exception) {
        exception.printStackTrace();
        logger.error(exception.getMessage());
        APIResponse apiResponse = new APIResponse(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                ExceptionMsg.FAILED_VALIDATION, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(apiResponse);
    }

    /**
     * Handle Invalid Json parsing (string -> integer) errors
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        logger.error("Error in one of the JSON entries");
        return getResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, exception);
    }

    /**
     * Handles other invalid Json errors
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException exception) {
        logger.error("Error in JSON format");
        return getResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, exception);
    }

    /**
     * Handle other invalid input errors
     */
    @ExceptionHandler({InvalidInputException.class})
    public ResponseEntity<Object> handleInvalidInputException(InvalidInputException exception) {
        logger.error("Error invalid input");
        return getResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, exception);
    }

    @ExceptionHandler({InvalidFormatException.class})
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException exception) {
        logger.error("Error invalid input format");
        return getResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, exception);
    }

    @ExceptionHandler({RequesterNotFoundException.class})
    public ResponseEntity<Object> handleRequesterNotFoundException(RequesterNotFoundException exception) {
        logger.error("Requester not found");
        return getResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, exception);
    }

    /**
     * Catches all remaining generic exceptions
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception exception) {
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    private static ResponseEntity<Object> getResponseEntity(HttpStatus status, Exception exception) {
        exception.printStackTrace();
        logger.error(exception.getMessage());
        APIResponse apiResponse = new APIResponse(String.valueOf(status.value()), exception.getMessage(), status.getReasonPhrase());
        return ResponseEntity.status(status).body(apiResponse);
    }

}
