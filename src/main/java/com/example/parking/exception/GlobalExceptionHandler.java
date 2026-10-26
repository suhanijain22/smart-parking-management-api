package com.example.parking.exception;

import com.example.parking.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ErrorResponse> body(HttpStatus s, String message, String path, Map<String,String> errors) {
        return ResponseEntity.status(s).body(new ErrorResponse(LocalDateTime.now(), s.value(), s.getReasonPhrase(), message, path, errors));
    }
    @ExceptionHandler(ResourceNotFoundException.class) public ResponseEntity<ErrorResponse> notFound(ResourceNotFoundException e, HttpServletRequest r){return body(HttpStatus.NOT_FOUND,e.getMessage(),r.getRequestURI(),Map.of());}
    @ExceptionHandler(ConflictException.class) public ResponseEntity<ErrorResponse> conflict(ConflictException e,HttpServletRequest r){return body(HttpStatus.CONFLICT,e.getMessage(),r.getRequestURI(),Map.of());}
    @ExceptionHandler({BadRequestException.class, ConstraintViolationException.class}) public ResponseEntity<ErrorResponse> badRequest(Exception e,HttpServletRequest r){return body(HttpStatus.BAD_REQUEST,e.getMessage(),r.getRequestURI(),Map.of());}
    @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException e,HttpServletRequest r){Map<String,String> errors=new LinkedHashMap<>(); e.getBindingResult().getFieldErrors().forEach(x->errors.put(x.getField(),x.getDefaultMessage())); return body(HttpStatus.BAD_REQUEST,"Validation failed",r.getRequestURI(),errors);}
    @ExceptionHandler(HttpMessageNotReadableException.class) public ResponseEntity<ErrorResponse> unreadable(HttpMessageNotReadableException e,HttpServletRequest r){return body(HttpStatus.BAD_REQUEST,"Request body or date format is invalid",r.getRequestURI(),Map.of());}
    @ExceptionHandler(DataIntegrityViolationException.class) public ResponseEntity<ErrorResponse> integrity(DataIntegrityViolationException e,HttpServletRequest r){return body(HttpStatus.CONFLICT,"Request conflicts with existing data",r.getRequestURI(),Map.of());}
}
