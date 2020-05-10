/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aow.capstone.entities;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Alex White
 */
@Entity
@Data
public class ParkVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int visitId;
    
    @NotNull(message = "Must give a start date.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @NotNull(message = "Must give an end date.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
          
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;
    
    @ManyToOne
    @JoinColumn(name = "park_id")
    private Park park;
}
