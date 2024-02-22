package biscof.clientcontactapi.service.mapper;


import biscof.clientcontactapi.dto.ClientDto;
import biscof.clientcontactapi.dto.response.ClientResponseDto;
import biscof.clientcontactapi.model.Client;
import org.mapstruct.Mapper;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
public abstract class ClientMapper {

    public abstract ClientResponseDto clientToClientResponseDto(Client client);

    public Client clientDtoToClient(ClientDto clientDto) {
        return Client.builder()
                .firstName(clientDto.getFirstName())
                .lastName(clientDto.getLastName())
                .phones(new ArrayList<>())
                .emails(new ArrayList<>())
                .build();
    }

}
