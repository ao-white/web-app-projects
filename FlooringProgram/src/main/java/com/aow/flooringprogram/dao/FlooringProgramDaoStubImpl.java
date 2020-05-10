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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlooringProgramDaoStubImpl implements FlooringProgramDao {
    
    Order onlyOrder;
    Tax onlyTax;
    Product onlyProduct;
    List<Order> orderList = new ArrayList<>();
    List<Tax> taxList = new ArrayList<>();
    List<Product> productList = new ArrayList<>();
    Map<String, Tax> taxMap = new HashMap<>();
    Map<String, Product> productMap = new HashMap<>();
    
    public FlooringProgramDaoStubImpl() {
        onlyOrder = new Order(1);
        onlyOrder.setCustomerName("Joe Smith");
        onlyOrder.setState("IN");
        onlyOrder.setProductType("Wood");
        onlyOrder.setArea(new BigDecimal("50"));
        onlyOrder.setDate(LocalDate.parse("2020-01-21"));
        
        orderList.add(onlyOrder);
        
        onlyTax = new Tax("IN", new BigDecimal("6.00"));
        
        taxList.add(onlyTax);
        taxMap.put("IN", onlyTax);
        
        onlyProduct = new Product("Wood", new BigDecimal("5.15"), new BigDecimal("4.75"));
        
        productList.add(onlyProduct);
        productMap.put("Wood", onlyProduct);
    }

    @Override
    public Order addOrder(int orderNumber, Order order) {
        if (orderNumber == onlyOrder.getOrderNumber()) {
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public Order removeOrder(Order order) {
        if (order.getOrderNumber() == onlyOrder.getOrderNumber()) {
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public Order editCustomerName(String newCustomerName, Order order) {
        if (order.getOrderNumber() == onlyOrder.getOrderNumber()) {
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public Order editState(String newState, Order order) {
        if (order.getOrderNumber() == onlyOrder.getOrderNumber()) {
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public Order editProductType(String newProductType, Order order) {
        if (order.getOrderNumber() == onlyOrder.getOrderNumber()) {
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public Order editArea(BigDecimal area, Order order) {
        if (order.getOrderNumber() == onlyOrder.getOrderNumber()) {
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public Order editDate(LocalDate date, Order order) {
        if (order.getOrderNumber() == onlyOrder.getOrderNumber()) {
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public boolean getAvailableStates(String state) {
        if (state.equals(onlyOrder.getState())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Order getAvailableOrderNumber(int orderNumber) {
        if (orderNumber == onlyOrder.getOrderNumber()) {
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return orderList;
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) {
        if (date.equals(onlyOrder.getDate())) {
            return orderList;
        } else {
            return null;
        }
    }

    @Override
    public List<Tax> getAllTaxes() {
        return taxList;
    }

    @Override
    public List<Product> getAllProducts() {
        return productList;
    }

    @Override
    public Map<String, Tax> getTaxMap() {
        return taxMap;
    }

    @Override
    public Map<String, Product> getProductMap() {
        return productMap;
    }

    @Override
    public int getCurrentOrderNumber() {
        return 1;
    }

    @Override
    public void loadFlooringProgram(boolean mode) throws FlooringProgramPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void writeFlooringProgram(boolean mode) throws FlooringProgramPersistenceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
