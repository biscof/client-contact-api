package biscof.clientcontactapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    @NotBlank(message = "First name must contain at least one character.")
    private String firstName;

    @NotBlank(message = "Last name must contain at least one character.")
    private String lastName;

}
