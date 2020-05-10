package corbos.fieldagent.service;

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
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LookupService {

    private final AgencyRepository agencyRepo;
    private final CountryRepository countryRepo;
    private final SecurityClearanceRepository securityRepo;
    private final AgentRepository agentRepo;
    private final AssignmentRepository assignmentRepo;

    public LookupService(AgencyRepository agencyRepo,
            CountryRepository countryRepo,
            SecurityClearanceRepository securityRepo,
            AgentRepository agentRepo,
            AssignmentRepository assignmentRepo) {
        this.agencyRepo = agencyRepo;
        this.countryRepo = countryRepo;
        this.securityRepo = securityRepo;
        this.agentRepo = agentRepo;
        this.assignmentRepo = assignmentRepo;
    }

    public List<Agency> findAllAgencies() {
        return agencyRepo.findAll();
    }

    public Agency findAgencyById(int agencyId) {
        return agencyRepo.findById(agencyId)
                .orElse(null);
    }

    public List<Country> findAllCountries() {
        return countryRepo.findAll();
    }

    public Country findCountryByCode(String countryCode) {
        return countryRepo.findById(countryCode)
                .orElse(null);
    }

    public List<SecurityClearance> findAllSecurityClearances() {
        return securityRepo.findAll();
    }

    public SecurityClearance findSecurityClearanceById(int securityClearanceId) {
        return securityRepo.findById(securityClearanceId)
                .orElse(null);
    }
    
    public List<Agent> findAllAgents() {
        return agentRepo.findAll();
    }
    
    public Agent findAgentById(String id) {
        return agentRepo.findById(id).orElse(null);
    }
    
    public List<Assignment> findAssignmentsByAgent(Agent agent) {
        return assignmentRepo.findByAgentIdentifier(agent.getIdentifier());
    }
    
    public Assignment findAssignmentsById(Integer id) {
        return assignmentRepo.findById(id).orElse(null);
    }
    
    public Agent addAgent(Agent agent) {
        return agentRepo.save(agent);
    }
    
    public Assignment addAssignment(Assignment assignment) {
        return assignmentRepo.save(assignment);
    }
    
    public Assignment editAssignment(Assignment assignment, Integer id) {
        Assignment assignmentFromDB = assignmentRepo.findById(id).orElse(null);
        assignmentFromDB.setAgent(assignment.getAgent());
        assignmentFromDB.setCountry(assignment.getCountry());
        assignmentFromDB.setStartDate(assignment.getStartDate());
        assignmentFromDB.setProjectedEndDate(assignment.getProjectedEndDate());
        assignmentFromDB.setActualEndDate(assignment.getActualEndDate());
        assignmentFromDB.setNotes(assignment.getNotes());
        return assignmentRepo.save(assignmentFromDB);
    }
    
    public void deleteAssignment(Integer id) {
        assignmentRepo.deleteById(id);
    }
    
    public Agent editAgent(Agent agent, String id) {
        Agent agentFromDB = agentRepo.findById(id).orElse(null);
        agentFromDB.setIdentifier(agent.getIdentifier());
        agentFromDB.setFirstName(agent.getFirstName());
        agentFromDB.setMiddleName(agent.getMiddleName());
        agentFromDB.setLastName(agent.getLastName());
        agentFromDB.setBirthDate(agent.getBirthDate());
        agentFromDB.setHeight(agent.getHeight());
        agentFromDB.setAgency(agent.getAgency());
        agentFromDB.setSecurityClearance(agent.getSecurityClearance());
        agentFromDB.setActivationDate(agent.getActivationDate());
        agentFromDB.setActive(agent.isActive());
        agentFromDB.setPictureUrl(agent.getPictureUrl());
        return agentRepo.save(agentFromDB);
    }
    
    public void deleteAgent(String id) {
        agentRepo.deleteById(id);
    }
}
