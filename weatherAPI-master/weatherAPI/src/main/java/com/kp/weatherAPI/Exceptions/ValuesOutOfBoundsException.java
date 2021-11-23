package com.kp.weatherAPI.Exceptions;

public class ValuesOutOfBoundsException extends RuntimeException{
    public ValuesOutOfBoundsException(String message) {
        super(message);
    }
}
