/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram.dao;

import com.aow.flooringprogram.dto.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FlooringProgramDaoTest {
    
    private FlooringProgramDao dao = new FlooringProgramDaoFileImpl();
    
    public FlooringProgramDaoTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() throws Exception {
        dao.loadFlooringProgram(false);
        List<Order> orderList = dao.getAllOrders();
        
        for (Order order : orderList) {
            dao.removeOrder(order);
        }
        
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of addOrder method, of class FlooringProgramDao.
     */
    @Test
    public void testAddGetOrder() {
        Order order = new Order(dao.getCurrentOrderNumber() + 1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-01"));
        
        dao.addOrder(order.getOrderNumber(), order);
        
        Order fromDao = dao.getAvailableOrderNumber(order.getOrderNumber());
        
        assertEquals(order, fromDao);
    }

    /**
     * Test of removeOrder method, of class FlooringProgramDao.
     */
    @Test
    public void testRemoveOrder() {
        Order order = new Order(dao.getCurrentOrderNumber() + 1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-28"));
        
        dao.addOrder(order.getOrderNumber(), order);
        
        Order order2 = new Order(dao.getCurrentOrderNumber() + 1);
        order2.setCustomerName("Billy Bob");
        order2.setState("MI");
        order2.setProductType("Carpet");
        order2.setArea(new BigDecimal("45"));
        order2.setDate(LocalDate.parse("2020-01-29"));
        
        dao.addOrder(order2.getOrderNumber(), order2);
        
        dao.removeOrder(order);
        assertEquals(1, dao.getAllOrders().size());
        assertNull(dao.getAvailableOrderNumber(order.getOrderNumber()));
        
        dao.removeOrder(order2);
        assertEquals(0, dao.getAllOrders().size());
        assertNull(dao.getAvailableOrderNumber(order2.getOrderNumber()));
    }

    /**
     * Test of editCustomerName method, of class FlooringProgramDao.
     */
    @Test
    public void testEditCustomerName() {
        Order order = new Order(dao.getCurrentOrderNumber() + 1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-28"));
        
        dao.addOrder(order.getOrderNumber(), order);
        
        dao.editCustomerName("Billy Bob", order);
        Order fromDao = dao.getAvailableOrderNumber(order.getOrderNumber());
        assertEquals("Billy Bob", fromDao.getCustomerName());
    }

    /**
     * Test of editState method, of class FlooringProgramDao.
     */
    @Test
    public void testEditState() {
        Order order = new Order(dao.getCurrentOrderNumber() + 1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-28"));
        
        dao.addOrder(order.getOrderNumber(), order);
        
        dao.editState("MI", order);
        Order fromDao = dao.getAvailableOrderNumber(order.getOrderNumber());
        assertEquals("MI", fromDao.getState());
    }

    /**
     * Test of editProductType method, of class FlooringProgramDao.
     */
    @Test
    public void testEditProductType() {
        Order order = new Order(dao.getCurrentOrderNumber() + 1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-28"));
        
        dao.addOrder(order.getOrderNumber(), order);
        
        dao.editProductType("Carpet", order);
        Order fromDao = dao.getAvailableOrderNumber(order.getOrderNumber());
        assertEquals("Carpet", fromDao.getProductType());
    }

    /**
     * Test of editArea method, of class FlooringProgramDao.
     */
    @Test
    public void testEditArea() {
        Order order = new Order(dao.getCurrentOrderNumber() + 1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-28"));
        
        dao.addOrder(order.getOrderNumber(), order);
        
        dao.editArea(new BigDecimal("60"), order);
        Order fromDao = dao.getAvailableOrderNumber(order.getOrderNumber());
        assertEquals(new BigDecimal("60"), fromDao.getArea());
    }

    /**
     * Test of editDate method, of class FlooringProgramDao.
     */
    @Test
    public void testEditDate() {
        Order order = new Order(dao.getCurrentOrderNumber() + 1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-28"));
        
        dao.addOrder(order.getOrderNumber(), order);
        
        dao.editDate(LocalDate.now(), order);
        Order fromDao = dao.getAvailableOrderNumber(order.getOrderNumber());
        assertEquals(LocalDate.now(), fromDao.getDate());
    }

    /**
     * Test of getAvailableStates method, of class FlooringProgramDao.
     */
    @Test
    public void testGetAvailableStates() {
        Order order = new Order(dao.getCurrentOrderNumber() + 1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-28"));
        
        dao.addOrder(order.getOrderNumber(), order);
        
        assertTrue(dao.getAvailableStates(order.getState()));
    }

    /**
     * Test of getAllOrders method, of class FlooringProgramDao.
     */
    @Test
    public void testGetAllOrders() {
        Order order = new Order(dao.getCurrentOrderNumber() + 1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-28"));
        
        dao.addOrder(order.getOrderNumber(), order);
        
        Order order2 = new Order(dao.getCurrentOrderNumber() + 1);
        order2.setCustomerName("Billy Bob");
        order2.setState("MI");
        order2.setProductType("Carpet");
        order2.setArea(new BigDecimal("45"));
        order2.setDate(LocalDate.parse("2020-01-29"));
        
        dao.addOrder(order2.getOrderNumber(), order2);
        
        assertEquals(2, dao.getAllOrders().size());
    }

    /**
     * Test of getOrdersByDate method, of class FlooringProgramDao.
     */
    @Test
    public void testGetOrdersByDate() {
        Order order = new Order(dao.getCurrentOrderNumber() + 1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-29"));
        
        dao.addOrder(order.getOrderNumber(), order);
        
        Order order2 = new Order(dao.getCurrentOrderNumber() + 1);
        order2.setCustomerName("Billy Bob");
        order2.setState("MI");
        order2.setProductType("Carpet");
        order2.setArea(new BigDecimal("45"));
        order2.setDate(LocalDate.parse("2020-01-29"));
        
        dao.addOrder(order2.getOrderNumber(), order2);
        
        assertEquals(2, dao.getOrdersByDate(LocalDate.parse("2020-01-29")).size());
    }

    /**
     * Test of getAllTaxes method, of class FlooringProgramDao.
     */
    @Test
    public void testGetAllTaxes() {
        assertEquals(4, dao.getAllTaxes().size());
    }

    /**
     * Test of getAllProducts method, of class FlooringProgramDao.
     */
    @Test
    public void testGetAllProducts() {
        assertEquals(4, dao.getAllProducts().size());
    }

    /**
     * Test of getTaxMap method, of class FlooringProgramDao.
     */
    @Test
    public void testGetTaxMap() {
        assertEquals(4, dao.getTaxMap().size());
    }

    /**
     * Test of getProductMap method, of class FlooringProgramDao.
     */
    @Test
    public void testGetProductMap() {
        assertEquals(4, dao.getProductMap().size());
    }
    
}
