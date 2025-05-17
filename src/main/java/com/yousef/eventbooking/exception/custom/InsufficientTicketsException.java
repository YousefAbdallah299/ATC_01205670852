package com.yousef.eventbooking.exception.custom;

public class InsufficientTicketsException extends RuntimeException{
    public InsufficientTicketsException(String message){
        super(message);
    }
}