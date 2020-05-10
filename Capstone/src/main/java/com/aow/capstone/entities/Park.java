/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aow.capstone.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

/**
 *
 * @author Alex White
 */
@Entity
@Data
public class Park {
    
    @Id
    private String parkId;
            
    private String name;
}
