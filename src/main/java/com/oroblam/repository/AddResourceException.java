package com.oroblam.repository;


import java.io.IOException;

public class AddResourceException extends Exception {
    public AddResourceException(IOException e) {
        super(e);
    }
}
