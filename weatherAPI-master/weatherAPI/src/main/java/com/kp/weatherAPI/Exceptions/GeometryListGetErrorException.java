package com.kp.weatherAPI.Exceptions;

public class GeometryListGetErrorException extends RuntimeException{
    public GeometryListGetErrorException() {
        super("Error while getting geometry list from database");
    }
}
