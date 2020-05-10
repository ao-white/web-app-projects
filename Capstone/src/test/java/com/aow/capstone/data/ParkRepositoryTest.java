package com.aow.capstone.data;

import com.aow.capstone.TestApplicationConfiguration;
import com.aow.capstone.entities.Park;
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
public class ParkRepositoryTest {
    
    @Autowired
    private ParkRepository repo;
    
    public ParkRepositoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void findAllParks() {
        assertEquals(62, repo.findAll().size());
    }
    
    @Test
    public void findParkById() {
        Park park = repo.findById("YELL").orElse(null);
        assertEquals("Yellowstone", park.getName());
    }
    
}
