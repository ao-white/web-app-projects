/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private final String productType;
    private final BigDecimal costSqFoot;
    private final BigDecimal laborCostSqFoot;

    public Product(String productType, BigDecimal costSqFoot, BigDecimal laborCostSqFoot) {
        this.productType = productType;
        this.costSqFoot = costSqFoot;
        this.laborCostSqFoot = laborCostSqFoot;
    }

    public String getProductType() {
        return productType;
    }

    public BigDecimal getCostSqFoot() {
        return costSqFoot;
    }

    public BigDecimal getLaborCostSqFoot() {
        return laborCostSqFoot;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.productType);
        hash = 83 * hash + Objects.hashCode(this.costSqFoot);
        hash = 83 * hash + Objects.hashCode(this.laborCostSqFoot);
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.productType, other.productType)) {
            return false;
        }
        if (!Objects.equals(this.costSqFoot, other.costSqFoot)) {
            return false;
        }
        if (!Objects.equals(this.laborCostSqFoot, other.laborCostSqFoot)) {
            return false;
        }
        return true;
    }
}
