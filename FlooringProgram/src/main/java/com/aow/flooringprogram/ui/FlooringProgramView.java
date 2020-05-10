/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram.ui;

import com.aow.flooringprogram.dto.Order;
import com.aow.flooringprogram.dto.Product;
import com.aow.flooringprogram.dto.Tax;
import com.aow.flooringprogram.service.FlooringProgramDataValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FlooringProgramView {
    private UserIO io;
    
    public FlooringProgramView(UserIO io){
        this.io = io;
    }
    
    public boolean displayModes() {
        io.print("1. Training Mode");
        io.print("2. Prod Mode");
        while(true){
            int userInput = io.readInt("Enter which mode to start program in.");
        
            switch(userInput) {
                case 1:
                    return false;
                case 2:
                    return true;
                default:
                    displayUnknownCommandBanner();
            }
        }
    }
    
    public int printMenuAndGetSelection() {
        io.print("\n* * * * * * * * * * * * * *");
        io.print("* <<Flooring Program>>");
        io.print("*1. Display Order");
        io.print("*2. Add an Order");
        io.print("*3. Edit an Order");
        io.print("*4. Remove an Order");
        io.print("*5. Save Current Work");
        io.print("*6. Quit");
        io.print("*");
        io.print("* * * * * * * * * * * * * *");
        return io.readInt("Please select from one of the above options.");
    }
    
    public boolean showOrdersOption() {
        io.print("1. Display All Orders");
        io.print("2. Display Orders From a Date");
        
        while(true){
            int userInput = io.readInt("Select a display option: ");
        
            switch(userInput) {
                case 1:
                    return true;
                case 2:
                    return false;
                default:
                    displayUnknownCommandBanner();
            }
        }
    }
    
    public void displayOrder(Order order) {
        io.print("\nOrder Number : " + Integer.toString(order.getOrderNumber()));
        io.print("Customer Name: " + order.getCustomerName());
        io.print("State        : " + order.getState());
        io.print("Product Type : " + order.getProductType());
        io.print("Area         : " + order.getArea().toString() + " ft²");
        io.print("Date         : " + order.getDate().toString());
        io.print("Total Price  : $" + order.getTotal().toString());
    }
    
    public void displayAllOrders(List<Order> orderList) {
        for(Order currentOrder : orderList){
            displayOrder(currentOrder);
        }
        io.readString("\nDisplay complete. Please hit enter to continue.");
    }
    
    public LocalDate getDateRequested() {
        boolean invalidInput;
        LocalDate date = null;
        do{
            invalidInput = false;
            String dateInput = io.readString("\nPlease enter date for order (yyyy-MM-dd): ");
            try {
                date = LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                invalidInput = true;
                displayErrorMessage("Not a valid input. Must give a date in 'yyyy-MM-dd' format.");
            }
        }while(invalidInput);
        return date;
    }
    
    public void displayDateOrders(List<Order> dateOrders) {
        if(dateOrders.isEmpty()) {
            io.print("\nNo orders exist for given date.");
            io.readString("Display canceled. Please hit enter to continue.");
        } else {
            displayAllOrders(dateOrders);
        }
    }
    
    public Order getOrderInfo(int orderNumber, List<Tax> taxes, List<Product> products) {
        boolean invalidInput;
        String customerName;
        String state;
        String productType;
        BigDecimal area = null;
        LocalDate date = null;
        
        do{
            invalidInput = false;
            customerName = io.readString("Please enter customer name: ");
            try {
                if(customerName == null || customerName.trim().length() == 0){
                    invalidInput = true;
                    throw new FlooringProgramDataValidationException("Not a valid input."
                        + " Must give a name.\n");
                } 
            } catch (FlooringProgramDataValidationException e) {
                displayErrorMessage(e.getMessage());
            }
        }while(invalidInput);
        
        io.print("\nAvailable states: ");
        for(Tax tax : taxes) {
            io.print(tax.getState());
        }
        
        do{
            invalidInput = false;
            state = io.readString("Please enter state initials (all CAPS) for order: ");
            boolean validState = availableState(taxes, state);
            try {
                if(state == null || state.trim().length() == 0 || !validState){
                    invalidInput = true;
                    throw new FlooringProgramDataValidationException("Not a valid input."
                        + " Must give a state company works in.\n");
                }
            } catch (FlooringProgramDataValidationException e) {
                displayErrorMessage(e.getMessage());
            }
        }while(invalidInput);
        
        io.print("\nAvailable products: ");
        for(Product product : products) {
            io.print(product.getProductType());
        }
        
        do{
            invalidInput = false;
            productType = io.readString("Please enter product type for order: ");
            boolean validProduct = availableProduct(products, productType);
            try {
                if(productType == null || productType.trim().length() == 0 || !validProduct){
                    invalidInput = true;
                    throw new FlooringProgramDataValidationException("Not a valid input."
                        + " Must give a company product type.\n");
                }
            } catch (FlooringProgramDataValidationException e) {
                displayErrorMessage(e.getMessage());
            }
        }while(invalidInput);
        
        do{
            invalidInput = false;
            String areaInput = io.readString("\nPlease enter area of material for order: ");
            try {
                area = new BigDecimal(areaInput).setScale(2, RoundingMode.HALF_UP);
                if(area.compareTo(BigDecimal.ZERO) < 1){
                    throw new FlooringProgramDataValidationException("");
                }
            } catch (FlooringProgramDataValidationException | NumberFormatException e) {
                invalidInput = true;
                displayErrorMessage("Not a valid input. Must give a positive, non-zero numeric value.");
            }
        }while(invalidInput);
        
        do{
            invalidInput = false;
            String dateInput = io.readString("\nPlease enter date for order as 'yyyy-MM-dd': ");
            try {
                date = LocalDate.parse(dateInput);
                if(date.isBefore(LocalDate.now())) {
                    throw new FlooringProgramDataValidationException("");
                }
            } catch (FlooringProgramDataValidationException | DateTimeParseException e) {
                invalidInput = true;
                displayErrorMessage("Not a valid input. Must give a present/future date in 'yyyy-MM-dd' format.");
            }
        }while(invalidInput);
        
        Order newOrder = new Order(orderNumber + 1);
        newOrder.setCustomerName(customerName);
        newOrder.setState(state);
        newOrder.setProductType(productType);
        newOrder.setArea(area);
        newOrder.setDate(date);
        
        return newOrder;
    }    
    
    public boolean confirmOrder(char command, Order newOrder) {
        displayOrder(newOrder);
        
        String userInput = "";
        while(true) {
            if(command == 'a'){
                userInput = io.readString("Commit this order? (y/n) "); 
            } else if(command == 'r') {
                userInput = io.readString("Remove this order? (y/n) ");
            }
            if(userInput.equalsIgnoreCase("y")){
                return true;
            } else if(userInput.equalsIgnoreCase("n")) {
                return false;
            } else {
                io.print("Please respond with 'y' for Yes or 'n' for No.");
            }
        }
    }
    
    public int getEditOrderSelection(){                              
        io.print("\nPlease select the order info to edit.");
        io.print("  1. Customer Name");
        io.print("  2. State");
        io.print("  3. Product Type");
        io.print("  4. Area");
        io.print("  5. Date");
        return io.readInt("  6. Back to Main Menu");
    }
    
    public String editCustomerName(Order order) {
        String customerName;
        
        customerName = io.readString("Please enter new customer name (" + order.getCustomerName() + "): ");
        if(customerName == null || customerName.trim().length() == 0){
            customerName = order.getCustomerName();
        }
        
        return customerName;
    }
    
    public String editState(List<Tax> taxes, Order order) {
        boolean invalidInput;
        String state;
        
        io.print("\nAvailable states: ");
        for(Tax tax : taxes) {
            io.print(tax.getState());
        }
        
        do{
            invalidInput = false;
            state = io.readString("Please enter new state initials (all CAPS) for order (" + order.getState() + "): ");
            boolean validState = availableState(taxes, state);
            try {
                if(state == null || state.trim().length() == 0){
                    state = order.getState();
                } else if(!validState){
                    invalidInput = true;
                    throw new FlooringProgramDataValidationException("Not a valid input."
                        + " Must give a state company works in.\n");
                }
            } catch (FlooringProgramDataValidationException e) {
                displayErrorMessage(e.getMessage());
            }
        }while(invalidInput);
        
        return state;
    }
    
    public String editProductType(List<Product> products, Order order) {
        boolean invalidInput;
        String productType;
        
        io.print("\nAvailable products: ");
        for(Product product : products) {
            io.print(product.getProductType());
        }
        
        do{
            invalidInput = false;
            productType = io.readString("Please enter new product type for order (" + order.getProductType() + "): ");
            boolean validProduct = availableProduct(products, productType);
            try {
                if(productType == null || productType.trim().length() == 0){
                    productType = order.getProductType();
                } else if(!validProduct){
                    invalidInput = true;
                    throw new FlooringProgramDataValidationException("Not a valid input."
                        + " Must give a company product type.\n");
                }
            } catch (FlooringProgramDataValidationException e) {
                displayErrorMessage(e.getMessage());
            }
        }while(invalidInput);
        
        return productType;
    }
    
    public BigDecimal editArea(Order order) {
        boolean invalidInput;
        BigDecimal area = null;
        
        do{
            invalidInput = false;
            String areaInput = io.readString("\nPlease enter new area of material for order (" + order.getArea() + "): ");
            if(areaInput == null || areaInput.trim().length() == 0) {
                area = order.getArea();
            } else {
                try {
                    area = new BigDecimal(areaInput).setScale(2, RoundingMode.HALF_UP);
                    if(area.compareTo(BigDecimal.ZERO) < 1){
                        throw new FlooringProgramDataValidationException("");
                    }
                } catch (FlooringProgramDataValidationException | NumberFormatException e) {
                    invalidInput = true;
                    displayErrorMessage("Not a valid input. Must give a positive, non-zero numeric value.");
                }
            }
        }while(invalidInput);
        
        return area;
    }
    
    public LocalDate editDate(Order order) {
        boolean invalidInput;
        LocalDate date = null;
        
        do{
            invalidInput = false;
            String dateInput = io.readString("\nPlease enter date for order as 'yyyy-MM-dd' (" + order.getDate() + "): ");
            if(dateInput == null || dateInput.trim().length() == 0) {
                date = order.getDate();
            } else {
                try {
                    date = LocalDate.parse(dateInput);
                    if(date.isBefore(LocalDate.now())) {
                        throw new FlooringProgramDataValidationException("");
                    }
                } catch (FlooringProgramDataValidationException | DateTimeParseException e) {
                    invalidInput = true;
                    displayErrorMessage("Not a valid input. Must give a present/future date in 'yyyy-MM-dd' format.");
                }
            }
        }while(invalidInput);
        
        return date;
    }
    
    public int orderRequest(char command) {
        int userInput = 0;
        while(true) {
            if(command == 'r') {
                userInput = io.readInt("Enter order number to remove: ");
            } else if(command == 'e') {
                userInput = io.readInt("Enter order number to edit: ");
            }
            
            if(userInput <= 0) {
                io.print("Not a valid input. Must enter a number greater than 0.");
            } else {
                return userInput;
            }
        }
    }
    
    public boolean confirmSave() {
        while(true) {
            String userInput = io.readString("Save changes to file? (y/n) ");
            if(userInput.equalsIgnoreCase("y")){
                return true;
            } else if(userInput.equalsIgnoreCase("n")) {
                return false;
            } else {
                io.print("Please respond with 'y' for Yes or 'n' for No.");
            }
        }
    }
    
    public void displayShowOrdersBanner() {
        io.print("\n=== Display Order ===");
    }
    
    public void displayAddOrderBanner(){
        io.print("=== Add Order ===");
    }
    
    public void displayAddSuccessBanner(){
        io.print("Order sucessfully added.");
    }
    
    public void displayAddCanceledBanner(){
        io.readString("Order canceled. Please hit enter to continue.");
    }
    
    public void displayEditOrderBanner(){
        io.print("\n=== Edit Order ===");
    }
    
    public void displayRemoveOrderBanner(){
        io.print("=== Remove Order ===");
    }
    
    public void displayRemoveSuccessBanner() {
        io.print("Order sucessfully removed.");
    }
    
    public void displayNoOrderExistsBanner() {
        io.print("No order exists for given order number.");
    }
    
    public void displayNoEditBanner() {
        io.print("No edit made to this field.");
    }
    
    public void displayEditCompleteBanner() {
        io.print("Edit completed.");
    }
    
    public void displayEditExitBanner() {
        io.print("\nLeaving edit selection.");
    }
    
    public void displayRemoveCanceledBanner() {
        io.readString("Remove canceled. Please hit enter to cotintue.");
    }
    
    public void displaySaveSuccessBanner(){
        io.readString("Save complete. Please hit enter to continue.");
    }
    
    public void displaySaveCanceledBanner(){
        io.readString("Save canceled. Please hit enter to continue.");
    }
    
    private boolean availableState(List<Tax> taxes, String state) {
        return taxes            //Check if state is available
                .stream()
                .anyMatch(s -> s.getState().equals(state));
    }
    
    private boolean availableProduct(List<Product> products, String product) {
        return products            //Check if state is available
                .stream()
                .anyMatch(s -> s.getProductType().equals(product));
    }
    
    public void displayExitBanner(){
        io.print("\nGood Bye!");
    }
    
    public void displayUnknownCommandBanner(){
        io.print("Unknown Command!");
    }
    
    public void displayErrorMessage(String errorMsg) {
        io.print("\n=== ERROR ===");
        io.print(errorMsg);
    }
}
