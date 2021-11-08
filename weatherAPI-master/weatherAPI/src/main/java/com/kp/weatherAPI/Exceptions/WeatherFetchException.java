package com.kp.weatherAPI.Exceptions;

public class WeatherFetchException extends RuntimeException{
    public WeatherFetchException() {
        super("Could not fetch the list.");
    }
}
