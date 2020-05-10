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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Alex White
 */
@Entity
@Data
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int entryId;
    
    @NotNull(message = "Must give a date.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate entryDate;
    
    @NotBlank(message = "Entry cannot be blank.")
    @Size(max = 500, message = "Cannot be greater than 500 characters.")
    private String entry;
    
    @ManyToOne
    @JoinColumn(name = "visit_id")
    private ParkVisit parkVisit;
}
