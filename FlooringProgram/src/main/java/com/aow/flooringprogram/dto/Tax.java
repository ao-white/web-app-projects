/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class Tax {
    private final String state;
    private final BigDecimal taxRate;

    public Tax(String state, BigDecimal taxRate) {
        this.state = state;
        this.taxRate = taxRate;
    }

    public String getState() {
        return state;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.state);
        hash = 83 * hash + Objects.hashCode(this.taxRate);
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
        final Tax other = (Tax) obj;
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.taxRate, other.taxRate)) {
            return false;
        }
        return true;
    }
}
