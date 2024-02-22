package biscof.clientcontactapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailRespDto {

    private Long emailId;

    private String email;

    private Long clientId;

}
