package com.aow.capstone.data;

import com.aow.capstone.TestApplicationConfiguration;
import com.aow.capstone.entities.Park;
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
public class ParkVisitRepositoryTest {
    
    @Autowired
    private ParkVisitRepository visitRepo;
    
    @Autowired
    private UserAccountRepository userRepo;
    
    @Autowired
    private ParkRepository parkRepo;
    
    public ParkVisitRepositoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        List<ParkVisit> visits = visitRepo.findAll();
        for(ParkVisit visit : visits) {
            if(visit.getVisitId() != 1) {
                visitRepo.deleteById(visit.getVisitId());
            }
        }
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testAddGetParkVisit() {
        ParkVisit visit = makeParkVisit1();
        visitRepo.save(visit);
        
        ParkVisit fromRepo = visitRepo.findById(visit.getVisitId()).orElse(null);
        assertEquals(fromRepo, visit);
    }
    
    @Test
    public void testGetAllVisits() {
        ParkVisit visit1 = makeParkVisit1();
        ParkVisit visit2 = makeParkVisit2();
        visitRepo.save(visit1);
        visitRepo.save(visit2);
        
        ParkVisit fromRepo1 = visitRepo.findById(visit1.getVisitId()).orElse(null);
        ParkVisit fromRepo2 = visitRepo.findById(visit2.getVisitId()).orElse(null);
        List<ParkVisit> visits = visitRepo.findAll();
        assertEquals(fromRepo1, visit1);
        assertEquals(fromRepo2, visit2);
        assertEquals(3, visits.size());     //One visit already added in MySQL
    }
    
    @Test
    public void testDeleteVisit() {
        ParkVisit visit1 = makeParkVisit1();
        ParkVisit visit2 = makeParkVisit2();
        visitRepo.save(visit1);
        visitRepo.save(visit2);
        
        List<ParkVisit> visits = visitRepo.findAll();
        assertEquals(3, visits.size());
        
        visitRepo.deleteById(visit1.getVisitId());
        
        visits = visitRepo.findAll();
        assertEquals(2, visits.size());
        
        visitRepo.deleteById(visit2.getVisitId());
        
        visits = visitRepo.findAll();
        assertEquals(1, visits.size());
    }
    
    @Test
    public void testEditVisit() {
        ParkVisit visit = makeParkVisit1();
        visitRepo.save(visit);
        
        visit.setStartDate(LocalDate.parse("2020-01-01"));
        visitRepo.save(visit);
        
        ParkVisit fromRepo = visitRepo.findById(visit.getVisitId()).orElse(null);
        assertEquals(LocalDate.parse("2020-01-01"), fromRepo.getStartDate());
    }
    
    private ParkVisit makeParkVisit1() {
        UserAccount user = userRepo.findById(1).orElse(null);
        Park park = parkRepo.findById("YELL").orElse(null);
        ParkVisit visit = new ParkVisit();
        visit.setStartDate(LocalDate.parse("2020-02-21"));
        visit.setEndDate(LocalDate.parse("2020-02-24"));
        visit.setUserAccount(user);
        visit.setPark(park);
        
        return visit;
    }
    
    private ParkVisit makeParkVisit2() {
        UserAccount user = userRepo.findById(1).orElse(null);
        Park park = parkRepo.findById("GRSM").orElse(null);
        ParkVisit visit = new ParkVisit();
        visit.setStartDate(LocalDate.parse("2020-02-25"));
        visit.setEndDate(LocalDate.parse("2020-02-28"));
        visit.setUserAccount(user);
        visit.setPark(park);
        
        return visit;
    }
    
}
