package com.devrajs.practice.oyo.exception;

/**
 * Created by devraj.singh on 10/21/15.
 */
public class HttpResponseFailedException extends Exception {

    private String message;
    public HttpResponseFailedException(String message)
    {
        super(message);
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}
