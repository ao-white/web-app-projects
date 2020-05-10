/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aow.capstone.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Alex White
 */
@Entity
@Data
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    
    @NotEmpty(message = "Username cannot be blank")
    @Size(max = 30, message="Username cannot be greater than 30 characters.")
    private String userName;
    
    private String password;
    
    private String description;
    
    private String pictureUrl;
    
    private String favoritePark;
    
    @Column(columnDefinition = "varchar(255) default 'ROLE_USER'")
    private String role;
    
}
