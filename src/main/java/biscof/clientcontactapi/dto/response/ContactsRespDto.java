package biscof.clientcontactapi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ContactsRespDto {

    private List<PhoneRespDto> phones;

    private List<EmailRespDto> emails;

}
