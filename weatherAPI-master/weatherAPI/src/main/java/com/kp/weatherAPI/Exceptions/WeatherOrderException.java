package com.kp.weatherAPI.Exceptions;

public class WeatherOrderException extends RuntimeException{
    public WeatherOrderException() {
        super("Error creating order ");
    }
}
