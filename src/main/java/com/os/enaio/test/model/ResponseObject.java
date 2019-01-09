package com.os.enaio.test.model;

public class ResponseObject {
    public ResponseObject()
    {
        Type = "content";
    }

    public String Type;

    public Object PayloadOrMessage;

    public Object FullException;
}
