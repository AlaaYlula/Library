package com.library.library.excepion;

import org.springframework.data.crossstore.ChangeSetPersister;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }

}
