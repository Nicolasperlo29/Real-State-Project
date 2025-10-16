package org.example.usuarios.exception;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(String message) { super(message); }
}
