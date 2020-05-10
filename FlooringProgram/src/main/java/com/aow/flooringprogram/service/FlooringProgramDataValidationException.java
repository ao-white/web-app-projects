/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram.service;

public class FlooringProgramDataValidationException extends Exception {
    public FlooringProgramDataValidationException(String message) {
        super(message);
    }
    
    public FlooringProgramDataValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
