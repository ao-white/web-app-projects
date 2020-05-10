package com.aow.capstone.data;

import com.aow.capstone.TestApplicationConfiguration;
import com.aow.capstone.entities.LogEntry;
import com.aow.capstone.entities.ParkVisit;
import com.aow.capstone.entities.UserAccount;
import java.time.LocalDate;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Alex White
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class LogEntryRepositoryTest {
    
    @Autowired
    private ParkVisitRepository visitRepo;
    
    @Autowired
    private LogEntryRepository entryRepo;
    
    public LogEntryRepositoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        entryRepo.deleteAll();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testAddGetEntry() {
        LogEntry entry = makeEntry1();
        entryRepo.save(entry);
        
        LogEntry fromRepo = entryRepo.findById(entry.getEntryId()).orElse(null);
        assertEquals(fromRepo, entry);
    }
    
    @Test
    public void testGetAllEntries() {
        LogEntry entry1 = makeEntry1();
        LogEntry entry2 = makeEntry2();
        entryRepo.save(entry1);
        entryRepo.save(entry2);
        
        LogEntry fromRepo1 = entryRepo.findById(entry1.getEntryId()).orElse(null);
        LogEntry fromRepo2 = entryRepo.findById(entry2.getEntryId()).orElse(null);
        List<LogEntry> entries = entryRepo.findAll();
        assertEquals(entry1, fromRepo1);
        assertEquals(entry2, fromRepo2);
        assertEquals(2, entries.size());
    }
    
    @Test
    public void testDeleteEntry() {
        LogEntry entry1 = makeEntry1();
        LogEntry entry2 = makeEntry2();
        entryRepo.save(entry1);
        entryRepo.save(entry2);
        
        List<LogEntry> entries = entryRepo.findAll();
        assertEquals(2, entries.size());
        
        entryRepo.deleteById(entry1.getEntryId());
        
        entries = entryRepo.findAll();
        assertEquals(1, entries.size());
        
        entryRepo.deleteById(entry2.getEntryId());
        
        entries = entryRepo.findAll();
        assertEquals(0, entries.size());
    }
    
    @Test
    public void testEditEntry() {
        LogEntry entry = makeEntry1();
        entryRepo.save(entry);
        
        entry.setEntryDate(LocalDate.parse("2020-01-01"));
        entryRepo.save(entry);
        
        LogEntry fromRepo = entryRepo.findById(entry.getEntryId()).orElse(null);
        assertEquals(fromRepo, entry);
    }
    
    private LogEntry makeEntry1() {
        ParkVisit visit = visitRepo.findById(1).orElse(null);
        LogEntry entry = new LogEntry();
        entry.setEntryDate(LocalDate.parse("2020-02-15"));
        entry.setEntry("Lorem ipsum dolor sit amet, consectetur adipiscing elit, "
                + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
                + "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. "
                + "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
        entry.setParkVisit(visit);
        
        return entry;
    }
    
    private LogEntry makeEntry2() {
        ParkVisit visit = visitRepo.findById(1).orElse(null);
        LogEntry entry = new LogEntry();
        entry.setEntryDate(LocalDate.parse("2020-02-20"));
        entry.setEntry("Lorem ipsum dolor sit amet, consectetur adipiscing elit, "
                + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        entry.setParkVisit(visit);
        
        return entry;
    }
}
