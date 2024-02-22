package biscof.clientcontactapi.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDto {

    @Pattern(regexp = "^(\\+7)\\d{10}$", message = "Please use format +7**********")
    private String phone;

}
