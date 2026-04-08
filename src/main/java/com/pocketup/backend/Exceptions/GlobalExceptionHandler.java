package com.pocketup.backend.Exceptions;

import com.pocketup.backend.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones.
 * * Esta clase actúa como un filtro que intercepta las excepciones
 * lanzadas por cualquier controlador de la aplicación.
 * Objetivo evitar
 * que el servidor devuelva trazas de error genéricas (StackTraces) o texto plano,
 * garantiza que el cliente siempre reciba un JSON
 * estructurado con el formato {@link ApiError}.
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Intercepta las excepciones de tipo RuntimeException
     * @param ex La excepción capturada en tiempo de ejecución.
     *  De aquí se extrae el mensaje de error definido en el Service.
     * @return ResponseEntity que contiene el objeto {@link ApiError} con el mensaje
     * y un código de estado HTTP 400 (Bad Request).
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex) {
        ApiError error = new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
