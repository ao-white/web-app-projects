/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aow.capstone.service;

import com.aow.capstone.data.LogEntryRepository;
import com.aow.capstone.data.ParkRepository;
import com.aow.capstone.data.ParkVisitRepository;
import com.aow.capstone.data.PictureRepository;
import com.aow.capstone.data.UserAccountRepository;
import com.aow.capstone.entities.LogEntry;
import com.aow.capstone.entities.Park;
import com.aow.capstone.entities.ParkVisit;
import com.aow.capstone.entities.Picture;
import com.aow.capstone.entities.UserAccount;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alex White
 */
@Service
public class CapstoneServiceLayer {
    
    private final UserAccountRepository userRepo;
    private final ParkRepository parkRepo;
    private final ParkVisitRepository visitRepo;
    private final LogEntryRepository entryRepo;
    private final PictureRepository picRepo;
    
    @Autowired
    PasswordEncoder encoder;
    
    public CapstoneServiceLayer(UserAccountRepository userRepo, ParkRepository parkRepo, 
            ParkVisitRepository visitRepo, LogEntryRepository entryRepo, PictureRepository picRepo) {
        this.userRepo = userRepo;
        this.parkRepo = parkRepo;
        this.visitRepo = visitRepo;
        this.entryRepo = entryRepo;
        this.picRepo = picRepo;
    }
    
    public UserAccount findUserById(int id) {
        return userRepo.findById(id).orElse(null);
    }
    
    public UserAccount findUserByUserName(String name) {
        return userRepo.findByUserName(name);
    }
    
    public List<String> findAllUserNamesExceptForOne(int id) {
        List<String> userNames = new ArrayList<>();
        List<UserAccount> users = userRepo.findAll();
        for(UserAccount user : users) {
            if(user.getUserId() != id) {
                userNames.add(user.getUserName());
            }
        }
        return userNames;
    }
    
    public List<Park> findAllParks() {
        return parkRepo.findAll();
    }
    
    public ParkVisit findVisitById(int id) {
        return visitRepo.findById(id).orElse(null);
    }
    
    public List<ParkVisit> findVisitsByUserId(int id) {
        return visitRepo.findByUserAccountUserId(id);
    }
    
    public List<LogEntry> findAllEntries() {
        return entryRepo.findAll();
    }
    
    public LogEntry findEntryById(int id) {
        return entryRepo.findById(id).orElse(null);
    }
    
    public List<LogEntry> findEntriesByVisitId(int id) {
        return entryRepo.findByParkVisitVisitId(id);
    }
    
    public Picture findPictureById(int id) {
        return picRepo.findById(id).orElse(null);
    }
    
    public List<Picture> findAllPictures() {
        return picRepo.findAll();
    }
    
    public UserAccount addUserAccount(UserAccount user) {
        if(user.getPictureUrl().isBlank()) {
            user.setPictureUrl("images/defaultProfile.jpg");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return userRepo.save(user);
    }
    
    public ParkVisit addParkVisit(ParkVisit visit) {
        return visitRepo.save(visit);
    }
    
    public LogEntry addLogEntry(LogEntry entry) {
        return entryRepo.save(entry);
    }
    
    public Picture addPicture(Picture picture) {
        return picRepo.save(picture);
    }
    
    public UserAccount editUserAccount(int id, boolean passwordChange, UserAccount user) {
        UserAccount fromRepo = userRepo.findById(id).orElse(null);
        String password = "";
        if(passwordChange) {
            password = encoder.encode(user.getPassword());
        } else {
            password = fromRepo.getPassword();
        }
        if(!user.getUserName().equals(fromRepo.getUserName())) {
            Collection<SimpleGrantedAuthority> nowAuthorities =(Collection<SimpleGrantedAuthority>)SecurityContextHolder
                        .getContext().getAuthentication().getAuthorities();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUserName(), password, nowAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        fromRepo.setUserName(user.getUserName());
        fromRepo.setPassword(password);
        fromRepo.setPictureUrl(user.getPictureUrl());
        fromRepo.setFavoritePark(user.getFavoritePark());
        fromRepo.setDescription(user.getDescription());
        return userRepo.save(fromRepo);
    }
    
    public ParkVisit editParkVisit(int id, ParkVisit visit) {
        ParkVisit fromRepo = visitRepo.findById(id).orElse(null);
        fromRepo.setPark(visit.getPark());
        fromRepo.setStartDate(visit.getStartDate());
        fromRepo.setEndDate(visit.getEndDate());
        return visitRepo.save(fromRepo);
    }
    
    public LogEntry editLogEntry(int id, LogEntry entry) {
        LogEntry fromRepo = entryRepo.findById(id).orElse(null);
        fromRepo.setEntryDate(entry.getEntryDate());
        fromRepo.setEntry(entry.getEntry());
        return entryRepo.save(fromRepo);
    }
    
    public Picture editPicture(int id, Picture picture) {
        Picture fromRepo = picRepo.findById(id).orElse(null);
        fromRepo.setTitle(picture.getTitle());
        fromRepo.setPictureDate(picture.getPictureDate());
        fromRepo.setPictureUrl(picture.getPictureUrl());
        return picRepo.save(fromRepo);
    }
    
    public void deleteUserAccount(int id) {
        List<ParkVisit> visits = visitRepo.findByUserAccountUserId(id);
        for(ParkVisit visit : visits) {
            deleteParkVisit(visit.getVisitId());
        }
        userRepo.deleteById(id);
    }
    
    public void deleteParkVisit(int id) {
        List<LogEntry> entries = findEntriesByVisitId(id);
        List<Picture> pictures = picRepo.findByParkVisitVisitId(id);
        for(LogEntry entry : entries) {
            deleteLogEntry(entry.getEntryId());
        }
        for(Picture picture : pictures) {
            deletePicture(picture.getPictureId());
        }
        visitRepo.deleteById(id);
    }
    
    public void deleteLogEntry(int id) {
        entryRepo.deleteById(id);
    }
    
    public void deletePicture(int id) {
        picRepo.deleteById(id);
    }
}
