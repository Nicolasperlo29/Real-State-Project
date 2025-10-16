package org.example.usuarios.controller;

import org.example.usuarios.DTOS.ApiResponse;
import org.example.usuarios.DTOS.UsuarioYaExistenteException;
import org.example.usuarios.exception.EmailAlreadyExistsException;
import org.example.usuarios.exception.InvalidEmailException;
import org.example.usuarios.exception.InvalidPasswordException;
import org.example.usuarios.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({ EmailAlreadyExistsException.class,
            InvalidPasswordException.class,
            InvalidEmailException.class })
    public ResponseEntity<ApiResponse<Object>> handleBadRequest(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    // Exception genérica
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error interno del servidor", null));
    }
}
