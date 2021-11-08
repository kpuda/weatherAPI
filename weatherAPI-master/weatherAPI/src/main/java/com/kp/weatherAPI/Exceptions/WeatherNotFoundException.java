package com.kp.weatherAPI.Exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class WeatherNotFoundException extends RuntimeException{
    public WeatherNotFoundException(double lat,double lon) {
        super("Weather location doesn't exists! Lat: "+lat+", lon: "+lon);

    }
}
