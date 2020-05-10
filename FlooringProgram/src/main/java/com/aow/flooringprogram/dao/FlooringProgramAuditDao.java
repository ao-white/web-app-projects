/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram.dao;

public interface FlooringProgramAuditDao {
    public void writeAuditEntry(String entry) throws FlooringProgramPersistenceException;
}
