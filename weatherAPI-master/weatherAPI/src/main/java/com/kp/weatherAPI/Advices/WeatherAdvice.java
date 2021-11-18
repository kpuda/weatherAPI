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
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String NotFoundHandler(NotFoundException ex) {
        return ex.getMessage();
    }


    @ResponseBody
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String ConflictExistsHandler(ConflictException ex) {
        return ex.getMessage();
    }

}
