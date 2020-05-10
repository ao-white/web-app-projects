/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram.dao;

import com.aow.flooringprogram.dto.Order;
import com.aow.flooringprogram.dto.Product;
import com.aow.flooringprogram.dto.Tax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface FlooringProgramDao {
    Order addOrder(int orderNumber, Order order);
    Order removeOrder(Order order);
    Order editCustomerName(String newCustomerName, Order order);
    Order editState(String newState, Order order);
    Order editProductType(String newProductType, Order order);
    Order editArea(BigDecimal area, Order order);
    Order editDate(LocalDate date, Order order);
    boolean getAvailableStates(String state);
    Order getAvailableOrderNumber(int orderNumber);
    List<Order> getAllOrders();
    List<Order> getOrdersByDate(LocalDate date);
    List<Tax> getAllTaxes();
    List<Product> getAllProducts();
    Map<String, Tax> getTaxMap();
    Map<String, Product> getProductMap();
    int getCurrentOrderNumber();
    void loadFlooringProgram(boolean mode) throws FlooringProgramPersistenceException;
    void writeFlooringProgram(boolean mode) throws FlooringProgramPersistenceException;
}
