package biscof.clientcontactapi.service.mapper;


import biscof.clientcontactapi.dto.EmailDto;
import biscof.clientcontactapi.dto.PhoneDto;
import biscof.clientcontactapi.dto.response.EmailRespDto;
import biscof.clientcontactapi.dto.response.PhoneRespDto;
import biscof.clientcontactapi.model.Email;
import biscof.clientcontactapi.model.Phone;
import biscof.clientcontactapi.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ContactMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addedAt", ignore = true)
    @Mapping(target = "client", source = "client")
    public abstract Phone phoneDtoToPhone(PhoneDto phoneDto, Client client);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addedAt", ignore = true)
    @Mapping(target = "client", source = "client")
    public abstract Email emailDtoToEmail(EmailDto emailDto, Client client);

    @Mapping(target = "phoneId", source = "phone.id")
    @Mapping(target = "phoneNumber", source = "phone.phone")
    @Mapping(target = "clientId", source = "phone.client.id")
    public abstract PhoneRespDto phoneToResponseDto(Phone phone);

    @Mapping(target = "emailId", source = "email.id")
    @Mapping(target = "clientId", source = "email.client.id")
    public abstract EmailRespDto emailToResponseDto(Email email);

}
