package com.kp.weatherAPI.Exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class WeatherNotFoundException extends RuntimeException{
    public WeatherNotFoundException() {
        super("Weather location doesn't exists!");

    }
}
