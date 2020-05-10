package corbos.fieldagent.entities;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
public class Agent {

    @Id
    @NotBlank(message = "Identifier must not be empty.")
    @Size(max = 25, message="Identifier must be 25 or less characters.")
    private String identifier;
    
    @NotBlank(message = "First name must not be empty.")
    @Size(max = 25, message="First name must be 25 or less characters.")
    private String firstName;
    
    private String middleName;
    
    @NotBlank(message = "Last name must not be empty.")
    @Size(max = 25, message="Last name must be 25 or less characters.")
    private String lastName;
    
    private String pictureUrl;
    
    @NotNull(message = "Birth date must not be empty.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    
    @NotNull(message = "Must enter a height between 36 and 96 inches.")
    @Min(value = 36, message = "Height must not be less than 36 inches.")
    @Max(value = 96, message = "Height must not be more than 96 inches.")
    private int height;
    
    @NotNull(message = "Activation date must not be empty.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate activationDate;
    
    private boolean isActive;

    @NotNull(message = "Must select an agency.")
    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @NotNull(message = "Must select a security clearance.")
    @ManyToOne
    @JoinColumn(name = "security_clearance_id")
    private SecurityClearance securityClearance;

}
