/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aow.capstone.service;

import com.aow.capstone.data.UserAccountRepository;
import com.aow.capstone.entities.UserAccount;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alex White
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserAccountRepository users;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = users.findByUserName(username);
        
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
        
        
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), grantedAuthorities);
    }
    
}
