package com.myProject.finalProject.exception;

public class BalanceNotFoundException extends Exception{

    public BalanceNotFoundException(long id) {
        super("Balance with id " + id + " doesn't exist");
    }
}
