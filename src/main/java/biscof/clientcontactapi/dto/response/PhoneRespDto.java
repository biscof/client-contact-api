package biscof.clientcontactapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PhoneRespDto {

    private Long phoneId;

    private String phoneNumber;

    private Long clientId;

}
