package com.yousef.eventbooking.exception.custom;

public class InvalidOldPasswordException extends RuntimeException{
    public InvalidOldPasswordException(String message){
        super(message);
    }
}