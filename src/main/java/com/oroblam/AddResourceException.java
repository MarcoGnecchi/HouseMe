package com.oroblam;


import java.io.IOException;

public class AddResourceException extends Exception {
    public AddResourceException(IOException e) {
        super(e);
    }
}
