/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aow.capstone.data;

import com.aow.capstone.TestApplicationConfiguration;
import com.aow.capstone.entities.ParkVisit;
import com.aow.capstone.entities.Picture;
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
 * @author AlexanderPC
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class PictureRepositoryTest {
    
    @Autowired
    private ParkVisitRepository visitRepo;
    
    @Autowired
    private PictureRepository picRepo;
    
    public PictureRepositoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        picRepo.deleteAll();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testAddGetPicture() {
        Picture pic = makePicture1();
        picRepo.save(pic);
        
        Picture fromRepo = picRepo.findById(pic.getPictureId()).orElse(null);
        assertEquals(fromRepo, pic);
    }
    
    @Test
    public void testGetAllPictures() {
        Picture pic1 = makePicture1();
        Picture pic2 = makePicture2();
        picRepo.save(pic1);
        picRepo.save(pic2);
        
        Picture fromRepo1 = picRepo.findById(pic1.getPictureId()).orElse(null);
        Picture fromRepo2 = picRepo.findById(pic2.getPictureId()).orElse(null);
        List<Picture> pics = picRepo.findAll();
        assertEquals(fromRepo1, pic1);
        assertEquals(fromRepo2, pic2);
        assertEquals(2, pics.size());
    }
    
    @Test
    public void testDeletePicture() {
        Picture pic1 = makePicture1();
        Picture pic2 = makePicture2();
        picRepo.save(pic1);
        picRepo.save(pic2);
        
        List<Picture> pics = picRepo.findAll();
        assertEquals(2, pics.size());
        
        picRepo.deleteById(pic1.getPictureId());
        
        pics = picRepo.findAll();
        assertEquals(1, pics.size());
        
        picRepo.deleteById(pic2.getPictureId());
        
        pics = picRepo.findAll();
        assertEquals(0, pics.size());
    }
    
    @Test
    public void testEditPicture() {
        Picture pic = makePicture1();
        picRepo.save(pic);
        
        pic.setPictureUrl("images/edit.jpg");
        picRepo.save(pic);
        
        Picture fromRepo = picRepo.findById(pic.getPictureId()).orElse(null);
        assertEquals("images/edit.jpg", fromRepo.getPictureUrl());
    }
    
    private Picture makePicture1() {
        ParkVisit visit = visitRepo.findById(1).orElse(null);
        Picture pic = new Picture();
        pic.setPictureUrl("images/test.jpg");
        pic.setTitle("Test Picture Title");
        pic.setPictureDate(LocalDate.parse("2020-02-02"));
        pic.setParkVisit(visit);
        
        return pic;
    }
    
    private Picture makePicture2() {
        ParkVisit visit = visitRepo.findById(1).orElse(null);
        Picture pic = new Picture();
        pic.setPictureUrl("images/testOther.jpg");
        pic.setTitle("Test Other Picture Title");
        pic.setPictureDate(LocalDate.parse("2020-03-03"));
        pic.setParkVisit(visit);
        
        return pic;
    }
    
}
