package com.kp.weatherAPI.Advices;

import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Exceptions.WeatherAlreadyExistsException;
import com.kp.weatherAPI.Exceptions.WeatherFetchException;
import com.kp.weatherAPI.Exceptions.WeatherNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WeatherAdvice {

    @ResponseBody
    @ExceptionHandler(WeatherNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String WeatherNotFoundHandler(WeatherNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(WeatherFetchException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public String WeatherFetchHandler(WeatherFetchException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(WeatherAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String WeatherExistsHandler(WeatherAlreadyExistsException ex) {
        return ex.getMessage();
    }
}
