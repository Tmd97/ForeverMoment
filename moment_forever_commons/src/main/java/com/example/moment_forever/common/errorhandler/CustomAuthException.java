package com.example.moment_forever.common.errorhandler;

public class CustomAuthException extends RuntimeException{
    public CustomAuthException(String msg) {
        super(msg);
    }

}
