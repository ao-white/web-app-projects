/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram.service;

import com.aow.flooringprogram.dao.FlooringProgramAuditDao;
import com.aow.flooringprogram.dao.FlooringProgramDao;
import com.aow.flooringprogram.dao.FlooringProgramPersistenceException;
import com.aow.flooringprogram.dto.Order;
import com.aow.flooringprogram.dto.Product;
import com.aow.flooringprogram.dto.Tax;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class FlooringProgramServiceLayerImpl implements FlooringProgramServiceLayer {
    FlooringProgramDao dao;
    private FlooringProgramAuditDao auditDao;
    
    public FlooringProgramServiceLayerImpl(FlooringProgramDao dao, FlooringProgramAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
    }
    
    @Override
    public void loadFiles(boolean isProdMode) throws FlooringProgramPersistenceException {
        dao.loadFlooringProgram(isProdMode);
    }
    
    @Override
    public Order addOrder(Order newOrder) throws FlooringProgramPersistenceException {
        auditDao.writeAuditEntry("Added Order #" + newOrder.getOrderNumber() + " for " + newOrder.getCustomerName() + ".");
        return dao.addOrder(newOrder.getOrderNumber(), newOrder);
    }
    
    @Override
    public Order removeOrder(Order order) throws FlooringProgramPersistenceException {
        auditDao.writeAuditEntry("Removed Order #" + order.getOrderNumber() + " for " + order.getCustomerName() + ".");
        return dao.removeOrder(order);
    }
    
    @Override
    public Order editCustomerName(String newCustomerName, Order order) {
        return dao.editCustomerName(newCustomerName, order);
    }
    
    @Override
    public Order editState(String newState, Order order) {
        return dao.editState(newState, order);
    }
    
    @Override
    public Order editProductType(String newProductType, Order order) {
        return dao.editProductType(newProductType, order);
    }
    
    @Override
    public Order editArea(BigDecimal area, Order order) {
       return dao.editArea(area, order);
    }
    
    @Override
    public Order editDate(LocalDate date, Order order) {
        return dao.editDate(date, order);
    }
    
    @Override
    public Order getAvailableOrderNumber(int orderNumber) {
        return dao.getAvailableOrderNumber(orderNumber);
    }
    
    @Override
    public List<Order> getAllOrders() {
        return dao.getAllOrders();
    }
    
    @Override
    public List<Order> getOrdersByDate(LocalDate date){
        return dao.getOrdersByDate(date);
    }
    
    @Override
    public List<Tax> getAllTaxes() {
        return dao.getAllTaxes();
    }
    
    @Override
    public List<Product> getAllProducts() {
        return dao.getAllProducts();
    }
    
    @Override
    public int getCurrentOrderNumber() {
        return dao.getCurrentOrderNumber();
    }
    
    @Override
    public void saveToFile(boolean mode) throws FlooringProgramPersistenceException {
        dao.writeFlooringProgram(mode);
    }
    
    /* Information for calculated fields done here */
    @Override
    public BigDecimal calculateCosts(Order order) {
        Map<String, Tax> taxes = dao.getTaxMap();
        Map<String, Product> products = dao.getProductMap();
        
        BigDecimal taxRate = taxes.get(order.getState()).getTaxRate();
        BigDecimal costSqFoot = products.get(order.getProductType()).getCostSqFoot();
        BigDecimal laborCostSqFoot = products.get(order.getProductType()).getLaborCostSqFoot();
        BigDecimal materialCost = costSqFoot.multiply(order.getArea()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal laborCost = laborCostSqFoot.multiply(order.getArea()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = materialCost.add(laborCost);
        BigDecimal tax = total.multiply(taxRate).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
        total = total.add(tax);
        
        order.setTaxRate(taxRate);
        order.setCostSqFoot(costSqFoot);
        order.setLaborCostSqFoot(laborCostSqFoot);
        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);
        
        return total;
    }

}
