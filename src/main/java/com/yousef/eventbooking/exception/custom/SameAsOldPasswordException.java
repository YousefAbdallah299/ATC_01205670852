package com.yousef.eventbooking.exception.custom;

public class SameAsOldPasswordException extends RuntimeException{
    public SameAsOldPasswordException(String message){
        super(message);
    }
}