package ua.com.brdo.business.constructor.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import ua.com.brdo.business.constructor.exception.NotFoundException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ControllerAdvice
public class ControllerAdviceHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = UNPROCESSABLE_ENTITY)
    @ResponseBody
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<ObjectError> objectErrors = e.getBindingResult().getGlobalErrors();
        final String fieldErrorsString = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(" "));
        final String objectErrorsString = objectErrors.stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(" "));
        String error = String.join(" ", fieldErrorsString, objectErrorsString).trim();
        return Collections.singletonMap("message", error);
    }

    @ExceptionHandler(value = {JsonParseException.class, JsonMappingException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(value = UNPROCESSABLE_ENTITY)
    @ResponseBody
    public Map<String, String> handleJsonParseException(Exception e) {
        return Collections.singletonMap("message", "Received malformed JSON.");
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = UNPROCESSABLE_ENTITY)
    @ResponseBody
    public Map<String, String> handleIllegalArgumentException(Exception e) {

        return Collections.singletonMap("message", e.getMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(value = UNPROCESSABLE_ENTITY)
    @ResponseBody
    public Map<String, String> handleConstraintViolationExceptionException(ConstraintViolationException e) {
        StringBuilder error = new StringBuilder();
        for(ConstraintViolation c : e.getConstraintViolations()){
            error.append(c.getMessageTemplate()).append(" ");
        }
        return Collections.singletonMap("message", error.toString());
    }

    @ExceptionHandler(value = NestedRuntimeException.class)
    @ResponseStatus(value = INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map<String, String> handleRuntimeException(Exception e) {
        return Collections.singletonMap("message", e.getMessage());
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(value = NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleNotFoundException(Exception e) {
        return Collections.singletonMap("message", e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleMethodArgumentTypeMismatchException(Exception e) {
        return Collections.singletonMap("message", "Received malformed URL.");
    }

    @ExceptionHandler(JpaObjectRetrievalFailureException.class)
    @ResponseStatus(value = UNPROCESSABLE_ENTITY)
    @ResponseBody
    public Map<String, String> handleJpaObjectRetrievalFailureException(JpaObjectRetrievalFailureException e) {
        return Collections.singletonMap("message", e.getCause().getMessage().replace("ua.com.brdo.business.constructor.model.",""));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = UNPROCESSABLE_ENTITY)
    @ResponseBody
    public Map<String, String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return Collections.singletonMap("message", "Illegal id of related object.");
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseStatus(value = UNPROCESSABLE_ENTITY)
    @ResponseBody
    public Map<String, String> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException e) {
        return Collections.singletonMap("message", e.getCause().getMessage());
    }
}
