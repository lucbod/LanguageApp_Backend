package com.example.LanguageAppMongoDb.auth;

public class CustomAuthenticationException extends Throwable {
    public CustomAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }
}
