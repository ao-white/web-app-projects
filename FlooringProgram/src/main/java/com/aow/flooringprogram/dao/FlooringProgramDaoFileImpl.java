/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram.dao;

import com.aow.flooringprogram.dto.Order;
import com.aow.flooringprogram.dto.Product;
import com.aow.flooringprogram.dto.Tax;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FlooringProgramDaoFileImpl implements FlooringProgramDao {
    private Map<Integer, Order> orders = new HashMap<>();
    private Map<String, Tax> taxes = new HashMap<>();
    private Map<String, Product> products = new HashMap<>();
    private int currentOrderNumber;
    public static final String FLOORINGPROGRAM_FILE =  "flooringProgram.txt";
    public static final String TRAINING_FILE =  "training.txt";
    public static final String TAXES_FILE = "taxes.txt";
    public static final String PRODUCTS_FILE = "products.txt";
    public static final String ORDERNUMBER_FILE = "orderNumber.txt";
    public static final String TRAININGORDERNUMBER_FILE = "trainingOrderNumber.txt";
    public static final String DELIMITER = "::";
    
    
    @Override
    public Order addOrder(int orderNumber, Order order) {
        Order newOrder = orders.put(orderNumber, order);
        currentOrderNumber++;
        return newOrder;
    }
    
    @Override
    public Order removeOrder(Order order) {
        return orders.remove(order.getOrderNumber());
    }
    
    @Override
    public Order editCustomerName(String newCustomerName, Order order) {
        orders.get(order.getOrderNumber()).setCustomerName(newCustomerName);
        return order;
    }
    
    @Override
    public Order editState(String newState, Order order) {
        orders.get(order.getOrderNumber()).setState(newState);
        return order;
    }
    
    @Override
    public Order editProductType(String newProductType, Order order) {
        orders.get(order.getOrderNumber()).setProductType(newProductType);
        return order;
    }
    
    @Override
    public Order editArea(BigDecimal area, Order order) {
        orders.get(order.getOrderNumber()).setArea(area);
        return order;
    }
    
    @Override
    public Order editDate(LocalDate date, Order order) {
        orders.get(order.getOrderNumber()).setDate(date);
        return order;
    }
    
    @Override
    public boolean getAvailableStates(String state) {
        return taxes.values()    //Check if state is available
                .stream()
                .anyMatch(s -> s.getState().equals(state));
    }
    
    @Override
    public Order getAvailableOrderNumber(int orderNumber) {
        return orders.values()    //Check if order number is available
                .stream()
                .filter(n -> n.getOrderNumber() == orderNumber)
                .findAny()
                .orElse(null);
    }
    
    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }
    
    @Override
    public List<Order> getOrdersByDate(LocalDate date) {
        return orders.values()
                .stream()
                .filter(d -> d.getDate().equals(date))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Tax> getAllTaxes() {
        return new ArrayList<>(taxes.values());
    }
    
    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }
    
    @Override
    public Map<String, Tax> getTaxMap() {
        return taxes;
    }
    
    @Override
    public Map<String, Product> getProductMap() {
        return products;
    }
    
    @Override
    public int getCurrentOrderNumber() {
        return currentOrderNumber;
    }
    
    private Order unmarshallOrder(String OrderAsText){
        String[] orderTokens = OrderAsText.split(DELIMITER);
        int orderNumber = Integer.parseInt(orderTokens[0]);
        Order orderFromFile = new Order(orderNumber);
        orderFromFile.setCustomerName(orderTokens[1]);
        orderFromFile.setState(orderTokens[2]);
        orderFromFile.setTaxRate(new BigDecimal(orderTokens[3]));
        orderFromFile.setProductType(orderTokens[4]);
        orderFromFile.setArea(new BigDecimal(orderTokens[5]));
        orderFromFile.setCostSqFoot(new BigDecimal(orderTokens[6]));
        orderFromFile.setLaborCostSqFoot(new BigDecimal(orderTokens[7]));
        orderFromFile.setMaterialCost(new BigDecimal(orderTokens[8]));
        orderFromFile.setLaborCost(new BigDecimal(orderTokens[9]));
        orderFromFile.setTax(new BigDecimal(orderTokens[10]));
        orderFromFile.setTotal(new BigDecimal(orderTokens[11]));
        orderFromFile.setDate(LocalDate.parse(orderTokens[12]));
        return orderFromFile;
    }
    
    private Tax unmarshallTax(String TaxAsText) {
        String[] taxTokens = TaxAsText.split(DELIMITER);
        String state = taxTokens[0];
        BigDecimal taxRate = new BigDecimal(taxTokens[1]);
        Tax taxFromFile = new Tax(state, taxRate);
        return taxFromFile;
    }
    
    private Product unmarshallProduct(String ProductAsText) {
        String[] productTokens = ProductAsText.split(DELIMITER);
        String state = productTokens[0];
        BigDecimal costSqFoot = new BigDecimal(productTokens[1]);
        BigDecimal laborCostSqFoot = new BigDecimal(productTokens[2]);
        Product productFromFile = new Product(state, costSqFoot, laborCostSqFoot);
        return productFromFile;
    }
    
    @Override
    public void loadFlooringProgram(boolean mode) throws FlooringProgramPersistenceException {
        Scanner scanner;
        Scanner scanner2;
        Scanner scanner3;
        Scanner scanner4;
        
        try{
            if(mode){
                scanner = new Scanner(new BufferedReader(new FileReader(FLOORINGPROGRAM_FILE)));
                scanner2 = new Scanner(new BufferedReader(new FileReader(TAXES_FILE)));
                scanner3 = new Scanner(new BufferedReader(new FileReader(PRODUCTS_FILE)));
                scanner4= new Scanner(new BufferedReader(new FileReader(ORDERNUMBER_FILE)));
            } else {
                scanner = new Scanner(new BufferedReader(new FileReader(TRAINING_FILE)));
                scanner2 = new Scanner(new BufferedReader(new FileReader(TAXES_FILE)));
                scanner3 = new Scanner(new BufferedReader(new FileReader(PRODUCTS_FILE)));
                scanner4= new Scanner(new BufferedReader(new FileReader(TRAININGORDERNUMBER_FILE)));
            }
        } catch (FileNotFoundException e){
            throw new FlooringProgramPersistenceException("-_- Could not load file data into memory.", e);
        }
        String currentLine;
        Order currentOrder;
        Tax currentTax;
        Product currentProduct;
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            orders.put(currentOrder.getOrderNumber(), currentOrder);
        }
        while(scanner2.hasNextLine()){
            currentLine = scanner2.nextLine();
            currentTax = unmarshallTax(currentLine);
            taxes.put(currentTax.getState(), currentTax);
        }
        while(scanner3.hasNextLine()){
            currentLine = scanner3.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            products.put(currentProduct.getProductType(), currentProduct);
        }
        currentOrderNumber = Integer.parseInt(scanner4.nextLine());
        scanner.close();
        scanner2.close();
        scanner3.close();
    }
    
    private String marshallOrder(Order order){
        String OrderAsText = order.getOrderNumber() + DELIMITER;
        OrderAsText += order.getCustomerName() + DELIMITER;
        OrderAsText += order.getState() + DELIMITER;
        OrderAsText += order.getTaxRate() + DELIMITER;
        OrderAsText += order.getProductType() + DELIMITER;
        OrderAsText += order.getArea() + DELIMITER;
        OrderAsText += order.getCostSqFoot() + DELIMITER;
        OrderAsText += order.getLaborCostSqFoot() + DELIMITER;
        OrderAsText += order.getMaterialCost() + DELIMITER;
        OrderAsText += order.getLaborCost() + DELIMITER;
        OrderAsText += order.getTax() + DELIMITER;
        OrderAsText += order.getTotal() + DELIMITER;
        OrderAsText += order.getDate();
        return OrderAsText;
    }
    
    /* Only saves order info to file if in "Prod" mode */
    @Override
    public void writeFlooringProgram(boolean mode) throws FlooringProgramPersistenceException {
        PrintWriter out;
        PrintWriter out2;
        
        String OrderAsText;
        List<Order> OrderList = this.getAllOrders();
        
        try{
            if(mode){
                out = new PrintWriter(new FileWriter(FLOORINGPROGRAM_FILE));
                out2 = new PrintWriter(new FileWriter(ORDERNUMBER_FILE));
                
                for (Order currentOrder : OrderList){
                    OrderAsText = marshallOrder(currentOrder);
                    out.println(OrderAsText);
                    out.flush();
                }
                out.close();
            } else {
                //out = new PrintWriter(new FileWriter(TRAINING_FILE));
                out2 = new PrintWriter(new FileWriter(TRAININGORDERNUMBER_FILE));
            }
        } catch (IOException e) {
            throw new FlooringProgramPersistenceException("Could not save Order data.", e);
        }
        
        out2.println(currentOrderNumber);
        out2.flush();
        
        out2.close();
    }
    
}
