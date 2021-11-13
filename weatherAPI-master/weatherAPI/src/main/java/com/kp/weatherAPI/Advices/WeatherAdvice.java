package com.kp.weatherAPI.Advices;

import com.kp.weatherAPI.Exceptions.*;
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
    @ExceptionHandler(WeatherGetErrorException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public String WeatherGetHandler(WeatherGetErrorException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(WeatherAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String WeatherExistsHandler(WeatherAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(GeometryListGetErrorException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public String GeometryListGetErrorHandler(GeometryListGetErrorException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(WeatherOrderException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String WeatherOrderExceptionHandler(WeatherOrderException ex) {
        return ex.getMessage();
    }
}
