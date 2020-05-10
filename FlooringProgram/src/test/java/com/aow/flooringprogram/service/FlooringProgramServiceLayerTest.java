/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram.service;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FlooringProgramServiceLayerTest {
    
    FlooringProgramServiceLayer service;
        
    public FlooringProgramServiceLayerTest() {
        /*
        FlooringProgramDao dao = new FlooringProgramDaoStubImpl();
        FlooringProgramAuditDao auditDao = new FlooringProgramAuditDaoStubImpl();
        
        service = new FlooringProgramServiceLayerImpl(dao, auditDao);
        */
        
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        service = ctx.getBean("serviceLayer", FlooringProgramServiceLayer.class);
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of addOrder method, of class FlooringProgramServiceLayer.
     */
    @Test
    public void testAddOrder() throws Exception {
        Order order = new Order(1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-21"));
        
        Order fromService = service.addOrder(order);
    }

    /**
     * Test of removeOrder method, of class FlooringProgramServiceLayer.
     */
    @Test
    public void testRemoveOrder() throws Exception {
        Order order = new Order(1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-21"));
        
        Order fromService = service.removeOrder(order);
        assertNotNull(fromService);
        Order order2 = new Order(99);
        fromService = service.removeOrder(order2);
        assertNull(fromService);
    }

    /**
     * Test of editCustomerName method, of class FlooringProgramServiceLayer.
     */
    @Test
    public void testEditCustomerName() throws Exception {
        Order order = new Order(1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-21"));
        
        Order fromService = service.editCustomerName("Billy Bob", order);
        assertNotNull(fromService);
        Order order2 = new Order(99);
        fromService = service.editCustomerName("Billy Bob", order2);
        assertNull(fromService);
    }

    /**
     * Test of editState method, of class FlooringProgramServiceLayer.
     */
    @Test
    public void testEditState() {
        Order order = new Order(1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-21"));
        
        Order fromService = service.editState("MI", order);
        assertNotNull(fromService);
        Order order2 = new Order(99);
        fromService = service.editState("MI", order2);
        assertNull(fromService);
    }

    /**
     * Test of editProductType method, of class FlooringProgramServiceLayer.
     */
    @Test
    public void testEditProductType() {
        Order order = new Order(1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-21"));
        
        Order fromService = service.editProductType("Carpet", order);
        assertNotNull(fromService);
        Order order2 = new Order(99);
        fromService = service.editProductType("Carpet", order2);
        assertNull(fromService);
    }

    /**
     * Test of editArea method, of class FlooringProgramServiceLayer.
     */
    @Test
    public void testEditArea() {
        Order order = new Order(1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-21"));
        
        Order fromService = service.editArea(new BigDecimal("60"), order);
        assertNotNull(fromService);
        Order order2 = new Order(99);
        fromService = service.editArea(new BigDecimal("60"), order2);
        assertNull(fromService);
    }

    /**
     * Test of editDate method, of class FlooringProgramServiceLayer.
     */
    @Test 
    public void testEditDate() {
        Order order = new Order(1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-21"));
        
        Order fromService = service.editDate(LocalDate.parse("2020-01-30"), order);
        assertNotNull(fromService);
        Order order2 = new Order(99);
        fromService = service.editDate(LocalDate.parse("2020-01-30"), order2);
        assertNull(fromService);
    }

    /**
     * Test of getAvailableOrderNumber method, of class FlooringProgramServiceLayer.
     */
    @Test
    public void testGetAvailableOrderNumber() {
        Order fromService = service.getAvailableOrderNumber(1);
        assertNotNull(fromService);
        fromService = service.getAvailableOrderNumber(99);
        assertNull(fromService);
    }

    /**
     * Test of getAllOrders method, of class FlooringProgramServiceLayer.
     */
    @Test
    public void testGetAllOrders() {
        assertEquals(1, service.getAllOrders().size());
    }

    /**
     * Test of getOrdersByDate method, of class FlooringProgramServiceLayer.
     */
    @Test
    public void testGetOrdersByDate() {
        List<Order> orderList = service.getOrdersByDate(LocalDate.parse("2020-01-21"));
        assertNotNull(orderList);
        orderList = service.getOrdersByDate(LocalDate.parse("2020-12-12"));
        assertNull(orderList);
    }

    /**
     * Test of getAllTaxes method, of class FlooringProgramServiceLayer.
     */
    @Test
    public void testGetAllTaxes() {
        assertEquals(1, service.getAllTaxes().size());
    }

    /**
     * Test of getAllProducts method, of class FlooringProgramServiceLayer.
     */
    @Test
    public void testGetAllProducts() {
        assertEquals(1, service.getAllProducts().size());
    }

    /**
     * Test of getCurrentOrderNumber method, of class FlooringProgramServiceLayer.
     */
    @Test
    public void testGetCurrentOrderNumber() {
        assertEquals(1, service.getCurrentOrderNumber());
    }

    /**
     * Test of calculateCosts method, of class FlooringProgramServiceLayer.
     */
    @Test
    public void testCalculateCosts() {
        Order order = new Order(1);
        order.setCustomerName("Joe Smith");
        order.setState("IN");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("50"));
        order.setDate(LocalDate.parse("2020-01-21"));
        
        BigDecimal total = service.calculateCosts(order);
        assertEquals(new BigDecimal("524.70"), total);
    }
    
}
