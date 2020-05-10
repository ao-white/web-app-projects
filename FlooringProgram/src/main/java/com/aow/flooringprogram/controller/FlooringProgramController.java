/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram.controller;

import com.aow.flooringprogram.dao.FlooringProgramPersistenceException;
import com.aow.flooringprogram.dto.Order;
import com.aow.flooringprogram.service.FlooringProgramServiceLayer;
import com.aow.flooringprogram.ui.FlooringProgramView;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FlooringProgramController {
    FlooringProgramView view;
    FlooringProgramServiceLayer service;
    
    public FlooringProgramController(FlooringProgramServiceLayer service, FlooringProgramView view){
        this.service = service;
        this.view = view;
    }
    
    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        int orderNumber = 0;
        
        boolean isProdMode = programMode();
        try{
            loadFiles(isProdMode);

            /* Main Menu Loop */
            while (keepGoing) {

                menuSelection = getMenuSelection();
                orderNumber = getCurrentOrderNumber();
                
                switch (menuSelection) {
                    case 1:
                        displayOrder();
                        break;
                    case 2:
                        addOrder(isProdMode, orderNumber);
                        break;
                    case 3:
                        editOrder(isProdMode);
                        break;
                    case 4:
                        removeOrder(isProdMode);
                        break;
                    case 5:
                        saveToFile(isProdMode);
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }

            }
        }
        catch (FlooringProgramPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
        exitCommand();
    }
    
    private boolean programMode() {
        return view.displayModes();
    }
    
    private void loadFiles(boolean isProdMode) throws FlooringProgramPersistenceException {
        service.loadFiles(isProdMode);
    }
    
    private int getCurrentOrderNumber() {
        return service.getCurrentOrderNumber();
    }
    
    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }
    
    private void displayOrder() {
        view.displayShowOrdersBanner();
        boolean showAll = view.showOrdersOption();
        /* Show all orders or show orders by date */
        if(showAll){
            view.displayAllOrders(service.getAllOrders());
        } else {
            LocalDate date = view.getDateRequested();
            List<Order> dateOrders = service.getOrdersByDate(date);
            view.displayDateOrders(dateOrders);
        }
    }
    
    private void addOrder(boolean mode, int orderNumber) throws FlooringProgramPersistenceException {
        view.displayAddOrderBanner();
        Order newOrder = view.getOrderInfo(orderNumber, service.getAllTaxes(), service.getAllProducts());
        service.calculateCosts(newOrder);
        boolean confirm = view.confirmOrder('a', newOrder);
        /* Confirm user wants to add order */
        if(confirm) {
            service.addOrder(newOrder);
            view.displayAddSuccessBanner();
            saveToFile(mode);
        } else {
            view.displayAddCanceledBanner();
        }
    }
    
    private void editOrder(boolean mode) throws FlooringProgramPersistenceException {
        view.displayEditOrderBanner();
        int requestedOrderNumber = view.orderRequest('e');
        Order availableOrder = service.getAvailableOrderNumber(requestedOrderNumber);
        boolean editing = true;
        boolean saveAndRecalculate = false;
        boolean changesMade = false;
        
        if(availableOrder != null) {
            /* Edit Order Menu Loop */
            while(editing){
                int editSelection = view.getEditOrderSelection();

                switch (editSelection) {
                    case 1:
                        changesMade = editCustomerName(availableOrder);
                        break;
                    case 2:
                        changesMade = editState(availableOrder);
                        break;
                    case 3:
                        changesMade = editProductType(availableOrder);
                        break;
                    case 4:
                        changesMade = editArea(availableOrder);
                        break;
                    case 5:
                        changesMade = editDate(availableOrder);
                        break;
                    case 6:
                        view.displayEditExitBanner();
                        if(saveAndRecalculate) {
                            service.calculateCosts(availableOrder);
                            saveToFile(mode);
                        }
                        editing = false;
                        break;
                    default:
                        unknownCommand();
                }
                
                if(saveAndRecalculate == false){
                    saveAndRecalculate = changesMade;
                }
            }
        } else {
            view.displayNoOrderExistsBanner();
            view.displayEditExitBanner();
        }
    }
    
    private void removeOrder(boolean mode) throws FlooringProgramPersistenceException {
        view.displayRemoveOrderBanner();
        int requestedOrderNumber = view.orderRequest('r');
        Order availableOrder = service.getAvailableOrderNumber(requestedOrderNumber);
        if(availableOrder != null) {
            boolean confirm = view.confirmOrder('r', availableOrder);
            /* Confirm user wants to remove order */
            if(confirm) {
                service.removeOrder(availableOrder);
                view.displayRemoveSuccessBanner();
                saveToFile(mode);
            }
            else {
                view.displayRemoveCanceledBanner();
            }
        } else {
            view.displayNoOrderExistsBanner();
            view.displayRemoveCanceledBanner();
        }
    }
    
    private void saveToFile(boolean mode) throws FlooringProgramPersistenceException {
        boolean confirm = view.confirmSave();
        if(confirm) {
            service.saveToFile(mode);
            view.displaySaveSuccessBanner();
        } else {
            view.displaySaveCanceledBanner();
        }
    }
    
    private boolean editCustomerName(Order order) {
        String newCustomerName = view.editCustomerName(order);
        if(newCustomerName.equals(order.getCustomerName())) {
            view.displayNoEditBanner();
            return false;
        }
        else {
            service.editCustomerName(newCustomerName, order);
            view.displayEditCompleteBanner();
            return true;
        }
    }
    
    private boolean editState(Order order) {
        String newState = view.editState(service.getAllTaxes(), order);
        if(newState.equals(order.getState())) {
            view.displayNoEditBanner();
            return false;
        }
        else {
            service.editState(newState, order);
            view.displayEditCompleteBanner();
            return true;
        }
    }
    
    private boolean editProductType(Order order) {
        String newProductType = view.editProductType(service.getAllProducts(), order);
        if(newProductType.equals(order.getProductType())) {
            view.displayNoEditBanner();
            return false;
        }
        else {
            service.editProductType(newProductType, order);
            view.displayEditCompleteBanner();
            return true;
        }
    }
    
    private boolean editArea(Order order) {
        BigDecimal newArea = view.editArea(order);
        if(newArea.compareTo(order.getArea()) == 0) {
            view.displayNoEditBanner();
            return false;
        }
        else {
            service.editArea(newArea, order);
            view.displayEditCompleteBanner();
            return true;
        }
    }
    
    private boolean editDate(Order order) {
        LocalDate newDate = view.editDate(order);
        if(newDate.isEqual(order.getDate())) {
            view.displayNoEditBanner();
            return false;
        }
        else {
            service.editDate(newDate, order);
            view.displayEditCompleteBanner();
            return true;
        }
    }
    
    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }
    
    private void exitCommand() {
        view.displayExitBanner();
    }
    
}
