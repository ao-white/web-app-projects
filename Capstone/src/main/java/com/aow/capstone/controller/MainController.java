/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aow.capstone.controller;

import com.aow.capstone.entities.LogEntry;
import com.aow.capstone.entities.Park;
import com.aow.capstone.entities.ParkVisit;
import com.aow.capstone.entities.Picture;
import com.aow.capstone.entities.UserAccount;
import com.aow.capstone.service.CapstoneServiceLayer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Alex White
 */
@Controller
@EnableJpaRepositories(basePackages = {"com.aow.capstone.data"})
public class MainController {
    
    @Autowired
    @Qualifier("capstoneServiceLayer")
    CapstoneServiceLayer service;
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/map")
    public String getMap() {
        return "map";
    }
    
    @GetMapping("/news")
    public String getNews(Model model) {
        List<Park> parks = service.findAllParks();
        parks.sort(Comparator.comparing(Park::getName));
        model.addAttribute("parks", parks);
        return "news";
    }
    
    @GetMapping("/parks")
    public String getParks(Model model) {
        List<Park> parks = service.findAllParks();
        parks.sort(Comparator.comparing(Park::getName));
        model.addAttribute("parks", parks);
        return "parks";
    }
    
    @GetMapping("/journal")
    public String getProfile(Model model, Authentication authentication) {
        String currentUserName = "";
        UserAccount user;
        try {
            currentUserName = authentication.getName();
            user = service.findUserByUserName(currentUserName);
            if(user.getPictureUrl().isEmpty()) {
            user.setPictureUrl("images/defaultProfile.jpg");
        }
        } catch(NullPointerException e) {
            user = new UserAccount();
        }
        List<ParkVisit> visits = service.findVisitsByUserId(user.getUserId());
        List<LogEntry> entries = service.findAllEntries();
        List<Picture> pictures = service.findAllPictures();
        List<Integer> entryAmount = new ArrayList<>();
        for(ParkVisit visit : visits) {
            int i = 0;
            for(LogEntry entry : entries) {
                if(entry.getParkVisit().getVisitId() == visit.getVisitId()) {
                    i++;
                }
            }
            entryAmount.add(i);
        }
        List<Integer> entryNumber = new ArrayList<>();
        for (int i = 1; i <= entries.size(); i++) {
            entryNumber.add(i);
        }
        model.addAttribute("user", user);
        model.addAttribute("visits", visits);
        model.addAttribute("entries", entries);
        model.addAttribute("pictures", pictures);
        model.addAttribute("entryAmount", entryAmount);
        model.addAttribute("entryNumber", entryNumber);
        return "profile";
    }
    
    @GetMapping("/login")
    public String getLogin(Model model) {
        return "login";
    }
    
    @GetMapping("/createAccount")
    public String getAccount(Model model) {
        UserAccount user = new UserAccount();
        List<Park> parks = service.findAllParks();
        parks.sort(Comparator.comparing(Park::getName));
        model.addAttribute("user", user);
        model.addAttribute("parks", parks);
        return "createAccount";
    }
    
    @GetMapping("/addTrip")
    public String getTrip(int id, Model model) {
        ParkVisit visit = new ParkVisit();
        List<Park> parks = service.findAllParks();
        parks.sort(Comparator.comparing(Park::getName));
        model.addAttribute("userId", id);
        model.addAttribute("visit", visit);
        model.addAttribute("parks", parks);
        return "addVisit";
    }
    
    @GetMapping("/editUser")
    public String getEditUser(int id, Model model) {
        UserAccount user = service.findUserById(id);
        List<Park> parks = service.findAllParks();
        parks.sort(Comparator.comparing(Park::getName));
        model.addAttribute("user", user);
        model.addAttribute("parks", parks);
        return "editAccount";
    }
    
    @GetMapping("/editTrip")
    public String getEditTrip(int id, Model model) {
        ParkVisit visit = service.findVisitById(id);
        List<Park> parks = service.findAllParks();
        parks.sort(Comparator.comparing(Park::getName));
        model.addAttribute("visit", visit);
        model.addAttribute("parks", parks);
        return "editVisit";
    }
    
    @GetMapping("/addEntry")
    public String getEntry(int id, Model model) {
        LogEntry entry = new LogEntry();
        model.addAttribute("visitId", id);
        model.addAttribute("newEntry", entry);
        return "addEntry";
    }
    
    @GetMapping("/editEntry")
    public String getEditEntry(int id, Model model) {
        LogEntry entry = service.findEntryById(id);
        model.addAttribute("editEntry", entry);
        return "editEntry";
    }
    
    @GetMapping("/addPicture")
    public String getPicture(int id, Model model) {
        Picture picture = new Picture();
        model.addAttribute("visitId", id);
        model.addAttribute("picture", picture);
        return "addPic";
    }
    
    @GetMapping("/editPicture")
    public String getEditPicture(int id, Model model) {
        Picture picture = service.findPictureById(id);
        model.addAttribute("picture", picture);
        return "editPic";
    }
    
    @PostMapping("/addUserPost")
    public String getAccountPost(@Valid @ModelAttribute("user") UserAccount user, BindingResult result, @RequestParam("file") MultipartFile file, Model model) {
        List<String> userNames = service.findAllUserNamesExceptForOne(0);
        for(String userName : userNames) {
            if(user.getUserName().equals(userName)) {
                result.rejectValue("userName", "", "Username already exists.");
                break;
            }
        }
        if(user.getPassword().length() < 8) {
            result.rejectValue("password", "", "Password cannot be less than 8 characters.");
        } else if(user.getPassword().length() > 30) {
            result.rejectValue("password", "", "Password cannot be greater than 30 characters.");
        }
        
        String fileType= file.getContentType();
        if (file.isEmpty()) {
            result.rejectValue("pictureUrl", "", "File is empty or does not exist.");
        } else if(!(fileType.equals("image/jpeg") || fileType.equals("image/png"))) {
            result.rejectValue("pictureUrl", "", "Invalid file type, must be jpeg or png.");
        } else {
            try {
                String filePath="C:\\Users\\AlexanderPC\\Documents\\NetBeansProjects\\Capstone\\src\\main\\webapp\\images\\";
                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                Path path = Paths.get(filePath + file.getOriginalFilename());
                Files.write(path, bytes);

            } catch (IOException e) {
                System.out.println(e);
                result.rejectValue("pictureUrl", "", "Failed to upload file.");
            }
        }
        
        if(result.hasErrors()) {
            List<Park> parks = service.findAllParks();
            parks.sort(Comparator.comparing(Park::getName));
            model.addAttribute("parks", parks);
            return "createAccount";
        }
        user.setPictureUrl("images/" + file.getOriginalFilename());
        service.addUserAccount(user);
        return "redirect:/login";
    }
    
    @PostMapping("/addTripPost")
    public String addVisit(int id, @Valid @ModelAttribute("visit") ParkVisit visit, BindingResult result, Model model) {
        visit.setUserAccount(service.findUserById(id));
        LocalDate startDate = visit.getStartDate();
        LocalDate endDate = visit.getEndDate();
        if(startDate != null) {
            if(startDate.isBefore(LocalDate.parse("1900-01-01"))) {
                result.rejectValue("startDate", "", "Start Date cannot be before 01-01-1900");
            }
            if(endDate != null) {
                if(endDate.isBefore(startDate)) {
                    result.rejectValue("endDate", "", "End date cannot be before Start Date.");
                }
            }
        }
        
        if(result.hasErrors()) {
            List<Park> parks = service.findAllParks();
            parks.sort(Comparator.comparing(Park::getName));
            model.addAttribute("userId", id);
            model.addAttribute("parks", parks);
            return "addVisit";
        }
        service.addParkVisit(visit);
        return "redirect:/journal";
    }
    
    @PostMapping("/addEntryPost")
    public String addEntry(int id, @Valid @ModelAttribute("newEntry") LogEntry entry, BindingResult result, Model model) {
        entry.setParkVisit(service.findVisitById(id));
        LocalDate entryDate = entry.getEntryDate();
        LocalDate startDate = entry.getParkVisit().getStartDate();
        LocalDate endDate = entry.getParkVisit().getEndDate();
        if(entryDate != null) {
            if(entryDate.isBefore(startDate) || entryDate.isAfter(endDate)) {
                result.rejectValue("entryDate", "", "Date must be between trip Start Date and End Date.");
            }
        }
        if(result.hasErrors()) {
            model.addAttribute("visitId", id);
            return "addEntry";
        }
        service.addLogEntry(entry);
        return "redirect:/journal";
    }
    
    @PostMapping("/addPicturePost")
    public String addPicture(int id, @Valid @ModelAttribute Picture picture, BindingResult result, @RequestParam("file") MultipartFile file, Model model) {
        picture.setParkVisit(service.findVisitById(id));
        LocalDate picDate = picture.getPictureDate();
        LocalDate startDate = picture.getParkVisit().getStartDate();
        LocalDate endDate = picture.getParkVisit().getEndDate();
        if(picDate != null) {
            if(picDate.isBefore(startDate) || picDate.isAfter(endDate)) {
                result.rejectValue("pictureDate", "", "Date must be between trip Start Date and End Date.");
            }
        }
        
        String fileType= file.getContentType();
        if (file.isEmpty()) {
            result.rejectValue("pictureUrl", "", "File is empty or does not exist.");
        } else if(!(fileType.equals("image/jpeg") || fileType.equals("image/png"))) {
            result.rejectValue("pictureUrl", "", "Invalid file type, must be jpeg or png.");
        } else {
            try {
                String filePath="C:\\Users\\AlexanderPC\\Documents\\NetBeansProjects\\Capstone\\src\\main\\webapp\\images\\";
                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                Path path = Paths.get(filePath + file.getOriginalFilename());
                Files.write(path, bytes);

            } catch (IOException e) {
                System.out.println(e);
                result.rejectValue("pictureUrl", "", "Failed to upload file.");
            }
        }
       
        if(result.hasErrors()) {
            model.addAttribute("visitId", id);
            return "addPic";
        }
        picture.setPictureUrl("images/" + file.getOriginalFilename());
        service.addPicture(picture);
        return "redirect:/journal";
    }
    
    @PostMapping("/editUserPost")
    public String editUser(int id, @Valid @ModelAttribute("user") UserAccount user, BindingResult result, @RequestParam("file") MultipartFile file, Model model) {
        List<String> userNames = service.findAllUserNamesExceptForOne(id);
        for(String userName : userNames) {
            if(user.getUserName().equals(userName)) {
                result.rejectValue("userName", "", "Username already exists.");
                break;
            }
        }
        boolean passwordChange = true;
        if(user.getPassword().length() == 0) {
            user.setPassword(service.findUserById(id).getPassword());
            passwordChange = false;
        } else if(user.getPassword().length() < 8) {
            result.rejectValue("password", "", "Password cannot be less than 8 characters.");
        } else if(user.getPassword().length() > 30) {
            result.rejectValue("password", "", "Password cannot be greater than 30 characters.");
        }
        
        String fileType= file.getContentType();
        if (file.isEmpty()) {
            user.setPictureUrl(service.findUserById(id).getPictureUrl());
        } else if(!(fileType.equals("image/jpeg") || fileType.equals("image/png"))) {
            result.rejectValue("pictureUrl", "", "Invalid file type, must be jpeg or png.");
        } else {
            try {
                String filePath="C:\\Users\\AlexanderPC\\Documents\\NetBeansProjects\\Capstone\\src\\main\\webapp\\images\\";
                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                Path path = Paths.get(filePath + file.getOriginalFilename());
                Files.write(path, bytes);
                user.setPictureUrl("images/" + file.getOriginalFilename());
            } catch (IOException e) {
                System.out.println(e);
                result.rejectValue("pictureUrl", "", "Failed to upload file.");
            }
        }
        
        if(result.hasErrors()) {
            List<Park> parks = service.findAllParks();
            parks.sort(Comparator.comparing(Park::getName));
            user.setUserId(id);
            model.addAttribute("user", user);
            model.addAttribute("parks", parks);
            return "editAccount";
        }
        service.editUserAccount(id, passwordChange, user);
        return "redirect:/journal";
    }
    
    @PostMapping("/editTripPost")
    public String doEditTrip(int id, @Valid @ModelAttribute("visit") ParkVisit visit, BindingResult result, Model model) {
        LocalDate startDate = visit.getStartDate();
        LocalDate endDate = visit.getEndDate();
        if(startDate != null) {
            if(startDate.isBefore(LocalDate.parse("1900-01-01"))) {
                result.rejectValue("startDate", "", "Start Date cannot be before 01-01-1900");
            }
            if(endDate != null) {
                if(endDate.isBefore(startDate)) {
                    result.rejectValue("endDate", "", "End date cannot be before Start Date.");
                }
            }
        }
        if(result.hasErrors()) {
            List<Park> parks = service.findAllParks();
            parks.sort(Comparator.comparing(Park::getName));
            visit.setVisitId(id);
            model.addAttribute("visit", visit);
            model.addAttribute("parks", parks);
            return "editVisit";
        }
        service.editParkVisit(id, visit);
        return "redirect:/journal";
    }
    
    @PostMapping("/editEntryPost")
    public String doEditEntry(int id, @Valid @ModelAttribute("editEntry") LogEntry entry, BindingResult result, Model model) {
        LogEntry currentEntry = service.findEntryById(id);
        LocalDate entryDate = entry.getEntryDate();
        LocalDate startDate = currentEntry.getParkVisit().getStartDate();
        LocalDate endDate = currentEntry.getParkVisit().getEndDate();
        if(entryDate != null) {
            if(entryDate.isBefore(startDate) || entryDate.isAfter(endDate)) {
                result.rejectValue("entryDate", "", "Date must be between trip Start Date and End Date.");
            }
        }
        if(result.hasErrors()) {
            entry.setEntryId(id);
            model.addAttribute("editEntry", entry);
            return "editEntry";
        }
        service.editLogEntry(id, entry);
        return "redirect:/journal";
    }
    
    @PostMapping("/editPicturePost")
    public String doEditPicture(int id, @Valid @ModelAttribute Picture picture, BindingResult result, @RequestParam("file") MultipartFile file, Model model) {
        Picture currentPicture = service.findPictureById(id);
        LocalDate picDate = picture.getPictureDate();
        LocalDate startDate = currentPicture.getParkVisit().getStartDate();
        LocalDate endDate = currentPicture.getParkVisit().getEndDate();
        if(picDate != null) {
            if(picDate.isBefore(startDate) || picDate.isAfter(endDate)) {
                result.rejectValue("pictureDate", "", "Date must be between trip Start Date and End Date.");
            }
        }
        
        String fileType= file.getContentType();
        if (file.isEmpty()) {
            picture.setPictureUrl(currentPicture.getPictureUrl());
        } else if(!(fileType.equals("image/jpeg") || fileType.equals("image/png"))) {
            result.rejectValue("pictureUrl", "", "Invalid file type, must be jpeg or png.");
        } else {
            try {
                String filePath="C:\\Users\\AlexanderPC\\Documents\\NetBeansProjects\\Capstone\\src\\main\\webapp\\images\\";
                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                Path path = Paths.get(filePath + file.getOriginalFilename());
                Files.write(path, bytes);
                picture.setPictureUrl("images/" + file.getOriginalFilename());
            } catch (IOException e) {
                System.out.println(e);
                result.rejectValue("pictureUrl", "", "Failed to upload file.");
            }
        }
        
        if(result.hasErrors()) {
            picture.setPictureId(id);
            model.addAttribute("picture", picture);
            return "editPic";
        }
        service.editPicture(id, picture);
        return "redirect:/journal";
    }
    
    @GetMapping("/confirmDeleteUser")
    public String confirmDeleteUser(int id, Model model) {
        model.addAttribute("userId", id);
        return "confirmation";
    }
    
    @GetMapping("/deleteUser")
    public String deleteUser(int id) {
        SecurityContextHolder.getContext().setAuthentication(null);
        service.deleteUserAccount(id);
        return "redirect:/";
    }
    
    @GetMapping("/deleteTrip")
    public String deleteTrip(int id) {
        service.deleteParkVisit(id);
        return "redirect:/journal";
    }
    
    @GetMapping("/deleteEntry")
    public String deleteEntry(int id) {
        service.deleteLogEntry(id);
        return "redirect:/journal";
    }
    
    @GetMapping("/deletePicture")
    public String deletePicture(int id) {
        service.deletePicture(id);
        return "redirect:/journal";
    }
}
