package com.exception;

import java.io.IOException;

public class QuestionNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QuestionNotFoundException(String message, IOException e) {
        super(message, e);
    }

}
