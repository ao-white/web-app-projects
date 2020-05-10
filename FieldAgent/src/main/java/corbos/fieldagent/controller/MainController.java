/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package corbos.fieldagent.controller;

import corbos.fieldagent.data.AgencyRepository;
import corbos.fieldagent.data.AgentRepository;
import corbos.fieldagent.data.AssignmentRepository;
import corbos.fieldagent.data.CountryRepository;
import corbos.fieldagent.data.SecurityClearanceRepository;
import corbos.fieldagent.entities.Agency;
import corbos.fieldagent.entities.Agent;
import corbos.fieldagent.entities.Assignment;
import corbos.fieldagent.entities.Country;
import corbos.fieldagent.entities.SecurityClearance;
import corbos.fieldagent.service.LookupService;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 * @author Alex White
 */
@Controller
public class MainController {
    
    @Autowired
    LookupService service;
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("agents", service.findAllAgents());
        return "index";
    }
    
    @GetMapping("/addAgentForm")
    public String displayAddAgentForm(Model model) {
        Agent agent = new Agent();
        model.addAttribute("agent", agent);
        boolean isActive = agent.isActive();
        model.addAttribute("active", isActive);
        return "addForm";
    }
    
    @GetMapping("/cancelAddAgent")
    public String cancel() {
        return "redirect:/";
    }
    
    @GetMapping("/deleteAgentForm")
    public String displayDeleteAgentForm(String id, Model model) {
        Agent agent = service.findAgentById(id);
        List<Assignment> assignmentList = service.findAssignmentsByAgent(agent);
        model.addAttribute("agent", agent);
        model.addAttribute("assignmentAmount", assignmentList.size());
        return "deleteForm";
    }
    
    @GetMapping("/viewAgentForm")
    public String displayAgentForm(String id, Model model, HttpSession session) {
        Agent agent = service.findAgentById(id);
        List<Agency> agencyList = service.findAllAgencies();
        List<SecurityClearance> clearanceList = service.findAllSecurityClearances();
        List<Assignment> assignmentList = service.findAssignmentsByAgent(agent);
        assignmentList.sort(Comparator.comparing(Assignment::getStartDate));
        model.addAttribute("agent", agent);
        model.addAttribute("agencies", agencyList);
        model.addAttribute("clearances", clearanceList);
        model.addAttribute("assignments", assignmentList);
        session.setAttribute("agent", agent);
        session.setAttribute("agencies", agencyList);
        session.setAttribute("clearances", clearanceList);
        session.setAttribute("assignments", assignmentList);
        return "agentForm";
    }
    
    @GetMapping("/addAssignmentForm")
    public String displayAddAssignmentForm(String agentId, Integer assignmentId, boolean toHome, boolean forEdit, Model model, HttpSession session) {
        Agent agent = service.findAgentById(agentId);
        Assignment assignment = service.findAssignmentsById(assignmentId);
        if(assignment == null) {
            assignment = new Assignment();
            assignment.setCountry(new Country());
            assignment.setAgent(agent);
        }
        List<Agent> agentList = service.findAllAgents();
        List<Country> countryList = service.findAllCountries();
        model.addAttribute("agent", agent);
        model.addAttribute("assignment", assignment);
        model.addAttribute("agents", agentList);
        model.addAttribute("countries", countryList);
        session.setAttribute("agent", agent);
        session.setAttribute("agents", agentList);
        session.setAttribute("countries", countryList);
        
        String returnAddress;
        if(toHome) {
            returnAddress = "";
        } else {
            returnAddress = "viewAgentForm?id=" + agent.getIdentifier();
        }
        model.addAttribute("return", returnAddress);
        session.setAttribute("return", returnAddress);
        
        String actionToDo;
        if(forEdit) {
            actionToDo = "editAssignment?id=" + assignment.getAssignmentId();
        } else {
            actionToDo = "addAssignment?returnAddress=" + returnAddress;
        }
        model.addAttribute("actionToDo", actionToDo);
        session.setAttribute("actionToDo", actionToDo);
        
        return "assignmentForm";
    }
    
    @PostMapping("/addAgent")
    public String addAgent(@Valid @ModelAttribute Agent agent, BindingResult result) {
        LocalDate birthDate = agent.getBirthDate();
        LocalDate activationDate = agent.getActivationDate();
        List<Agent> agents = service.findAllAgents();
        if(birthDate != null) {
            if(birthDate.isBefore(LocalDate.parse("1900-01-01"))) {
                result.rejectValue("birthDate", "","Birth date must start from 1/1/1900.");
            } else if(birthDate.isAfter(LocalDate.now().minusYears(10).plusDays(1))) {
                result.rejectValue("birthDate", "","Birth date must be 10 years from current day.");
            } 
        }
        if(activationDate != null) {
            if(birthDate == null) {
                result.rejectValue("activationDate", "","Activation date must 10 years after birth date.");
            }
            else if(activationDate.isBefore(birthDate.plusYears(10))) {
                result.rejectValue("activationDate", "","Activation date must 10 years after birth date.");
            }
        }
        if(agent.getIdentifier() != null) {
            for(Agent agentCompare : agents) {
                if(agent.getIdentifier().equals(agentCompare.getIdentifier())) {
                    result.rejectValue("identifier", "", "Identifier already exists.");
                    break;
                }
            }
        }
        
        if(result.hasErrors()) {
            return "addForm";
        }
        
        service.addAgent(agent);
        return "redirect:/";
    }
    
    @PostMapping("/addAssignment")
    public String addAssignment(@Valid @ModelAttribute Assignment assignment, BindingResult result, String returnAddress, 
            @SessionAttribute("agent") Agent agent,
            @SessionAttribute("agents") List<Agent> agentList,
            @SessionAttribute("countries") List<Country> countryList,
            @SessionAttribute("return") String returnAddressOther,
            @SessionAttribute("actionToDo") String actionToDo,
                         Model model, SessionStatus sessionStatus) {
        LocalDate startDate = assignment.getStartDate();
        LocalDate projectedEndDate = assignment.getProjectedEndDate();
        LocalDate actualEndDate = assignment.getActualEndDate();
        List<Assignment> assignmentList = service.findAssignmentsByAgent(agent);
        
        if(startDate != null){
            if(!assignmentList.isEmpty()) {
                assignmentList.sort(Comparator.comparing(Assignment::getStartDate));
                for(Assignment assignmentCompare : assignmentList) {
                    System.out.println(assignmentCompare.getStartDate());
                    if(actualEndDate != null) {
                        if(assignmentCompare.getActualEndDate() != null) {
                            if(startDate.minusDays(1).isBefore(assignmentCompare.getActualEndDate()) && assignmentCompare.getStartDate().minusDays(1).isBefore(actualEndDate)) {
                                result.rejectValue("startDate", "", "Dates overlap with previous assignment.");
                                result.rejectValue("projectedEndDate", "", "Dates overlap with previous assignment.");
                                result.rejectValue("actualEndDate", "", "Dates overlap with previous assignment.");
                                break;
                            }
                        } else {
                            if(startDate.minusDays(1).isBefore(assignmentCompare.getProjectedEndDate()) && assignmentCompare.getStartDate().minusDays(1).isBefore(actualEndDate)) {
                                result.rejectValue("startDate", "", "Dates overlap with previous assignment.");
                                result.rejectValue("projectedEndDate", "", "Dates overlap with previous assignment.");
                                result.rejectValue("actualEndDate", "", "Dates overlap with previous assignment.");
                                break;
                            }
                        }
                    } else if(projectedEndDate != null) {
                        if(assignmentCompare.getActualEndDate() != null) {
                            if(startDate.minusDays(1).isBefore(assignmentCompare.getActualEndDate()) && assignmentCompare.getStartDate().minusDays(1).isBefore(projectedEndDate)) {
                                if(actualEndDate == null) {
                                    result.rejectValue("startDate", "", "Dates overlap with previous assignment.");
                                }
                                result.rejectValue("projectedEndDate", "", "Dates overlap with previous assignment.");
                                break;
                            }
                        } else {
                            if(startDate.minusDays(1).isBefore(assignmentCompare.getProjectedEndDate()) && assignmentCompare.getStartDate().minusDays(1).isBefore(projectedEndDate)) {
                                if(actualEndDate == null) {
                                    result.rejectValue("startDate", "", "Dates overlap with previous assignment.");
                                }
                                result.rejectValue("projectedEndDate", "", "Dates overlap with previous assignment.");
                                break;
                            }
                        }
                    }
                } 
            }
            if(actualEndDate != null && projectedEndDate != null) {
                if(startDate.plusDays(1).isAfter(projectedEndDate) && startDate.isAfter(actualEndDate)){
                    result.rejectValue("startDate", "", "Start date must be before projected end date and actual end date.");
                    result.rejectValue("projectedEndDate", "", "Projected end date must be after start date.");
                    result.rejectValue("actualEndDate", "", "Actual end date must be after start date.");
                } else if(startDate.plusDays(1).isAfter(projectedEndDate)) {
                    result.rejectValue("startDate", "", "Start date must be before projected end date.");
                    result.rejectValue("projectedEndDate", "", "Projected end date must be after start date.");
                } else if(startDate.plusDays(1).isAfter(actualEndDate)) {
                    result.rejectValue("startDate", "", "Start date must be before actual end date.");
                    result.rejectValue("actualEndDate", "", "Actual end date must be after start date.");
                }
            } else if(projectedEndDate != null){
                if(startDate.plusDays(1).isAfter(projectedEndDate)) {
                    result.rejectValue("startDate", "", "Start date must be before projected end date.");
                    result.rejectValue("projectedEndDate", "", "Projected end date must be after start date.");
                }
            } else if(actualEndDate != null){
                if(startDate.plusDays(1).isAfter(actualEndDate)) {
                    result.rejectValue("startDate", "", "Start date must be before actual end date.");
                    result.rejectValue("actualEndDate", "", "Actual end date must be after start date.");
                }
            }
        }
        
        if(result.hasErrors()) {
            model.addAttribute("agent", agent);
            model.addAttribute("assignment", assignment);
            model.addAttribute("agents", agentList);
            model.addAttribute("countries", countryList);
            model.addAttribute("agents", agentList);
            model.addAttribute("return", returnAddressOther);
            model.addAttribute("actionToDo", actionToDo);
            return "assignmentForm";
        }
        
        service.addAssignment(assignment);
        sessionStatus.setComplete();
        return "redirect:/"  + returnAddress;
    }
    
    @PostMapping("/editAssignment")
    public String editAssignment(@Valid @ModelAttribute Assignment assignment, BindingResult result, Integer id,
            @SessionAttribute("agent") Agent agent,
            @SessionAttribute("agents") List<Agent> agentList,
            @SessionAttribute("countries") List<Country> countryList,
            @SessionAttribute("return") String returnAddressOther,
            @SessionAttribute("actionToDo") String actionToDo,
                         Model model, SessionStatus sessionStatus) {
        LocalDate startDate = assignment.getStartDate();
        LocalDate projectedEndDate = assignment.getProjectedEndDate();
        LocalDate actualEndDate = assignment.getActualEndDate();
        List<Assignment> assignmentList = service.findAssignmentsByAgent(assignment.getAgent());
        if(startDate != null){
            if(!assignmentList.isEmpty()) {
                assignmentList.sort(Comparator.comparing(Assignment::getStartDate));
                for(Assignment assignmentCompare : assignmentList) {
                    if(assignmentCompare.getAssignmentId() == id) {
                        continue;
                    }
                    if(actualEndDate != null) {
                        if(assignmentCompare.getActualEndDate() != null) {
                            if(startDate.minusDays(1).isBefore(assignmentCompare.getActualEndDate()) && assignmentCompare.getStartDate().minusDays(1).isBefore(actualEndDate)) {
                                result.rejectValue("startDate", "", "Dates overlap with previous assignment.");
                                result.rejectValue("projectedEndDate", "", "Dates overlap with previous assignment.");
                                result.rejectValue("actualEndDate", "", "Dates overlap with previous assignment.");
                                break;
                            }
                        } else {
                            if(startDate.minusDays(1).isBefore(assignmentCompare.getProjectedEndDate()) && assignmentCompare.getStartDate().minusDays(1).isBefore(actualEndDate)) {
                                result.rejectValue("startDate", "", "Dates overlap with previous assignment.");
                                result.rejectValue("projectedEndDate", "", "Dates overlap with previous assignment.");
                                result.rejectValue("actualEndDate", "", "Dates overlap with previous assignment.");
                                break;
                            }
                        }
                    } else if(projectedEndDate != null) {
                        if(assignmentCompare.getActualEndDate() != null) {
                            if(startDate.minusDays(1).isBefore(assignmentCompare.getActualEndDate()) && assignmentCompare.getStartDate().minusDays(1).isBefore(projectedEndDate)) {
                                if(actualEndDate == null) {
                                    result.rejectValue("startDate", "", "Dates overlap with previous assignment.");
                                }
                                result.rejectValue("projectedEndDate", "", "Dates overlap with previous assignment.");
                                break;
                            }
                        } else {
                            if(startDate.minusDays(1).isBefore(assignmentCompare.getProjectedEndDate()) && assignmentCompare.getStartDate().minusDays(1).isBefore(projectedEndDate)) {
                                if(actualEndDate == null) {
                                    result.rejectValue("startDate", "", "Dates overlap with previous assignment.");
                                }
                                result.rejectValue("projectedEndDate", "", "Dates overlap with previous assignment.");
                                break;
                            }
                        }
                    }
                } 
            }
            if(actualEndDate != null && projectedEndDate != null) {
                if(startDate.plusDays(1).isAfter(projectedEndDate) && startDate.isAfter(actualEndDate)){
                    result.rejectValue("startDate", "", "Start date must be before projected end date and actual end date.");
                    result.rejectValue("projectedEndDate", "", "Projected end date must be after start date.");
                    result.rejectValue("actualEndDate", "", "Actual end date must be after start date.");
                } else if(startDate.plusDays(1).isAfter(projectedEndDate)) {
                    result.rejectValue("startDate", "", "Start date must be before projected end date.");
                    result.rejectValue("projectedEndDate", "", "Projected end date must be after start date.");
                } else if(startDate.plusDays(1).isAfter(actualEndDate)) {
                    result.rejectValue("startDate", "", "Start date must be before actual end date.");
                    result.rejectValue("actualEndDate", "", "Actual end date must be after start date.");
                }
            } else if(projectedEndDate != null){
                if(startDate.plusDays(1).isAfter(projectedEndDate)) {
                    result.rejectValue("startDate", "", "Start date must be before projected end date.");
                    result.rejectValue("projectedEndDate", "", "Projected end date must be after start date.");
                }
            } else if(actualEndDate != null){
                if(startDate.plusDays(1).isAfter(actualEndDate)) {
                    result.rejectValue("startDate", "", "Start date must be before actual end date.");
                    result.rejectValue("actualEndDate", "", "Actual end date must be after start date.");
                }
            }
        }
        
        if(result.hasErrors()) {
            model.addAttribute("agent", agent);
            model.addAttribute("assignment", assignment);
            model.addAttribute("agents", agentList);
            model.addAttribute("countries", countryList);
            model.addAttribute("agents", agentList);
            model.addAttribute("return", returnAddressOther);
            model.addAttribute("actionToDo", actionToDo);
            return "assignmentForm";
        }
        
        service.editAssignment(assignment, id);
        sessionStatus.setComplete();
        return "redirect:/viewAgentForm?id=" + assignment.getAgent().getIdentifier();
    }
    
    @GetMapping("/deleteAssignment")
    public String deleteAssignment(Integer id, String agentId) {
        service.deleteAssignment(id);
        return "redirect:/viewAgentForm?id=" + agentId;
    }
    
    @PostMapping("/editAgent")
    public String editAgent(@Valid @ModelAttribute Agent agent, BindingResult result, String id,
            @SessionAttribute("agent") Agent agentHere,
            @SessionAttribute("agencies") List<Agency> agencyList,
            @SessionAttribute("clearances") List<SecurityClearance> clearanceList,
            @SessionAttribute("assignments") List<Assignment> assignmentList,
                         Model model, SessionStatus sessionStatus) {
        LocalDate birthDate = agent.getBirthDate();
        LocalDate activationDate = agent.getActivationDate();
        if(birthDate != null) {
            if(birthDate.isBefore(LocalDate.parse("1900-01-01"))) {
                result.rejectValue("birthDate", "","Birth date must start from 1/1/1900.");
            } else if(birthDate.isAfter(LocalDate.now().minusYears(10).plusDays(1))) {
                result.rejectValue("birthDate", "","Birth date must be 10 years from current day.");
            } 
        }
        if(activationDate != null) {
            if(birthDate != null) {
                if(activationDate.isBefore(birthDate.plusYears(10))) {
                    result.rejectValue("activationDate", "","Activation date must 10 years after birth date.");
                }
            }
        }
        
        if(result.hasErrors()) {
            model.addAttribute("agent", agent);
            model.addAttribute("agencies", agencyList);
            model.addAttribute("clearances", clearanceList);
            model.addAttribute("assignments", assignmentList);
            return "agentForm";
        }
        
        service.editAgent(agent, id);
        sessionStatus.setComplete();
        return "redirect:/viewAgentForm?id=" + agent.getIdentifier();
    }
    
    @GetMapping("/deleteAgent") 
    public String deleteAgent(String agentId) {
        Agent agent = service.findAgentById(agentId);
        List<Assignment> assignmentList = service.findAssignmentsByAgent(agent);
        
        for(Assignment assignment : assignmentList) {
            service.deleteAssignment(assignment.getAssignmentId());
        }
        service.deleteAgent(agentId);
        
        return "redirect:/";
    }
}
