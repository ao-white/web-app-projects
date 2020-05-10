/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram.dao;

public class FlooringProgramPersistenceException extends Exception {
    public FlooringProgramPersistenceException(String message) {
        super(message);
    }
    
    public FlooringProgramPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
