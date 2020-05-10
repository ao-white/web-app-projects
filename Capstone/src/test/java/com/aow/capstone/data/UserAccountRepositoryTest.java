package com.aow.capstone.data;

import com.aow.capstone.TestApplicationConfiguration;
import com.aow.capstone.entities.UserAccount;
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
public class UserAccountRepositoryTest {
    
    @Autowired
    private UserAccountRepository repo;
    
    public UserAccountRepositoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        List<UserAccount> users = repo.findAll();
        for(UserAccount user : users) {
            if(user.getUserId() != 1) {
                repo.deleteById(user.getUserId());
            }
        }
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testAddGetUserAccount() {
        UserAccount user = makeUserAccount1();
        repo.save(user);
       
        UserAccount fromRepo = repo.findById(user.getUserId()).orElse(null);
        assertEquals(user, fromRepo);
    }
    
    @Test
    public void testGetAllUserAccounts() {
        UserAccount user1 = makeUserAccount1();
        UserAccount user2 = makeUserAccount2();
        repo.save(user1);
        repo.save(user2);
        
        
        UserAccount fromRepo1 = repo.findById(user1.getUserId()).orElse(null);
        UserAccount fromRepo2 = repo.findById(user2.getUserId()).orElse(null);
        List<UserAccount> users = repo.findAll();
        assertEquals(fromRepo1, user1);
        assertEquals(fromRepo2, user2);
        assertEquals(3, users.size());  //One user is already added in MySQL
    }
    
    @Test
    public void testDeleteUserAccount() {
        UserAccount user1 = makeUserAccount1();
        UserAccount user2 = makeUserAccount2();
        repo.save(user1);
        repo.save(user2);
        
        List<UserAccount> users = repo.findAll();
        assertEquals(3, users.size());
        
        repo.deleteById(user1.getUserId());
        
        users = repo.findAll();
        assertEquals(2, users.size());
        
        repo.deleteById(user2.getUserId());
        
        users = repo.findAll();
        assertEquals(1, users.size());
    }
    
    @Test
    public void testEditUserAccount() {
        UserAccount user = makeUserAccount1();
        repo.save(user);
        
        user.setFavoritePark("Yosemite");
        repo.save(user);
        
        UserAccount fromRepo = repo.findById(user.getUserId()).orElse(null);
        assertEquals("Yosemite", fromRepo.getFavoritePark());
    }
    
    private UserAccount makeUserAccount1() {
        UserAccount user = new UserAccount();
        user.setUserName("Name");
        user.setPassword("Password1");
        user.setDescription("This is a description of the first test user.");
        user.setPictureUrl("images/test.jpg");
        user.setFavoritePark("Yellowstone");
        return user;
    }
    
    private UserAccount makeUserAccount2() {
        UserAccount user = new UserAccount();
        user.setUserName("Name2");
        user.setPassword("Password2");
        user.setDescription("This is a description of the second test user.");
        user.setPictureUrl("images/test2.jpg");
        user.setFavoritePark("Grand Canyon");
        return user;
    }
    
}
