package com.kp.weatherAPI.Exceptions;

public class WeatherAlreadyExistsException extends RuntimeException{
    public WeatherAlreadyExistsException() {
        super("This location already exists");
    }
}
