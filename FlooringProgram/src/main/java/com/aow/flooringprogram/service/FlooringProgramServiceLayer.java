/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram.service;

import com.aow.flooringprogram.dao.FlooringProgramPersistenceException;
import com.aow.flooringprogram.dto.Order;
import com.aow.flooringprogram.dto.Product;
import com.aow.flooringprogram.dto.Tax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlooringProgramServiceLayer {
    void loadFiles(boolean isProdMode) throws FlooringProgramPersistenceException;
    Order addOrder(Order newOrder) throws FlooringProgramPersistenceException;
    Order removeOrder(Order order) throws FlooringProgramPersistenceException;
    Order editCustomerName(String newCustomerName, Order order);
    Order editState(String newState, Order order);
    Order editProductType(String newProductType, Order order);
    Order editArea(BigDecimal area, Order order);
    Order editDate(LocalDate date, Order order);
    Order getAvailableOrderNumber(int orderNumber);
    List<Order> getAllOrders();
    List<Order> getOrdersByDate(LocalDate date);
    List<Tax> getAllTaxes();
    List<Product> getAllProducts();
    int getCurrentOrderNumber();
    void saveToFile(boolean mode) throws FlooringProgramPersistenceException;
    BigDecimal calculateCosts(Order order);
}
