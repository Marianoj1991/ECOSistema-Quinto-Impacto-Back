package com.quintoimpacto.ecosistema.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ProductoYServicioNotFoundException extends RuntimeException{

    public ProductoYServicioNotFoundException(String message) {
        super(message);
    }
}
