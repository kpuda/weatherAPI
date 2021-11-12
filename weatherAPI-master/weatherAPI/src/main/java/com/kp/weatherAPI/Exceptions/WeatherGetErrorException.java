package com.kp.weatherAPI.Exceptions;

public class WeatherGetErrorException extends RuntimeException{
    public WeatherGetErrorException() {
        super("Could not get the list.");
    }
}
