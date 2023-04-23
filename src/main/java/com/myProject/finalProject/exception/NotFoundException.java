package com.myProject.finalProject.exception;

import java.util.function.Supplier;

public class NotFoundException extends Exception{

    public NotFoundException(String message) {
        super(message);
    }
}
