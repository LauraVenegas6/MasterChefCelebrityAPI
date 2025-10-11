package com.dosw.masterchef.exception;

/**
 * Exception thrown when a recipe is not found in the database.
 */
public class RecipeNotFoundException extends RuntimeException {
    
    /**
     * Constructor with message
     * @param message Message of the error
     */
    public RecipeNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause
     * @param message Message of the error
     * @param cause Cause of the error
     */
    public RecipeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}