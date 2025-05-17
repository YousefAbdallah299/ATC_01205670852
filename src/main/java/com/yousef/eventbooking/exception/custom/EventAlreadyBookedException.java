package com.yousef.eventbooking.exception.custom;

public class EventAlreadyBookedException extends RuntimeException{
    public EventAlreadyBookedException(String message){
        super(message);
    }
}