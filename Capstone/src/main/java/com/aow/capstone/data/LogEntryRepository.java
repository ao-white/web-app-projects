package com.aow.capstone.data;

import com.aow.capstone.entities.LogEntry;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Alex White
 */
@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Integer> {
    List<LogEntry> findByParkVisitVisitId(int id);
}
