package com.sharebook.demo.common;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.HashMap;
import java.util.Map;

//afin de cacher tous les détails de l'exception au client, nous devons intercepter les exceptions des controllers de
//l application et les gérer de manière centralisée.

//Pour cela, nous allons créer une classe ExceptionResolver qui interceptera les exceptions et les gérera de manière centralisée.
//Pour cela, nous allons utiliser l'annotation @ControllerAdvice qui permet de définir des méthodes de gestion des exceptions
//qui s'appliquent à tous les contrôleurs de l'application.

//Créez une classe ExceptionResolver dans le package common et ajoutez l'annotation @ControllerAdvice.
@ControllerAdvice
public class ExceptionResolver {
    @ExceptionHandler
    public ResponseEntity handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
    }

}
