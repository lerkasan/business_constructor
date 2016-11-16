package ua.com.brdo.business.constructor.controller.advice;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ControllerAdvice
public class RegisterControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = UNPROCESSABLE_ENTITY)
    @ResponseBody
    public  Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
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

    @ExceptionHandler(value = NestedRuntimeException.class)
    @ResponseStatus(value = INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map<String, String> handleRuntimeException(Exception e) {
        return Collections.singletonMap("message", e.getMessage());
    }
}