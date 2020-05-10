/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Order {
    private int orderNumber;
    private String customerName;
    private String state;
    private BigDecimal taxRate;
    private String productType;
    private BigDecimal area;
    private BigDecimal costSqFoot;
    private BigDecimal laborCostSqFoot;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;
    private LocalDate date;

    public Order(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getCostSqFoot() {
        return costSqFoot;
    }

    public void setCostSqFoot(BigDecimal costSqFoot) {
        this.costSqFoot = costSqFoot;
    }

    public BigDecimal getLaborCostSqFoot() {
        return laborCostSqFoot;
    }

    public void setLaborCostSqFoot(BigDecimal laborCostSqFoot) {
        this.laborCostSqFoot = laborCostSqFoot;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.orderNumber;
        hash = 29 * hash + Objects.hashCode(this.customerName);
        hash = 29 * hash + Objects.hashCode(this.state);
        hash = 29 * hash + Objects.hashCode(this.taxRate);
        hash = 29 * hash + Objects.hashCode(this.productType);
        hash = 29 * hash + Objects.hashCode(this.area);
        hash = 29 * hash + Objects.hashCode(this.costSqFoot);
        hash = 29 * hash + Objects.hashCode(this.laborCostSqFoot);
        hash = 29 * hash + Objects.hashCode(this.materialCost);
        hash = 29 * hash + Objects.hashCode(this.laborCost);
        hash = 29 * hash + Objects.hashCode(this.tax);
        hash = 29 * hash + Objects.hashCode(this.total);
        hash = 29 * hash + Objects.hashCode(this.date);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (this.orderNumber != other.orderNumber) {
            return false;
        }
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.productType, other.productType)) {
            return false;
        }
        if (!Objects.equals(this.taxRate, other.taxRate)) {
            return false;
        }
        if (!Objects.equals(this.area, other.area)) {
            return false;
        }
        if (!Objects.equals(this.costSqFoot, other.costSqFoot)) {
            return false;
        }
        if (!Objects.equals(this.laborCostSqFoot, other.laborCostSqFoot)) {
            return false;
        }
        if (!Objects.equals(this.materialCost, other.materialCost)) {
            return false;
        }
        if (!Objects.equals(this.laborCost, other.laborCost)) {
            return false;
        }
        if (!Objects.equals(this.tax, other.tax)) {
            return false;
        }
        if (!Objects.equals(this.total, other.total)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        return true;
    }
}
