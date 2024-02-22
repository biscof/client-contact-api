package biscof.clientcontactapi.service;

import biscof.clientcontactapi.dto.EmailDto;
import biscof.clientcontactapi.dto.PhoneDto;
import biscof.clientcontactapi.dto.ClientDto;
import biscof.clientcontactapi.dto.response.ContactsRespDto;
import biscof.clientcontactapi.dto.response.ClientResponseDto;
import biscof.clientcontactapi.dto.response.EmailRespDto;
import biscof.clientcontactapi.dto.response.PhoneRespDto;
import biscof.clientcontactapi.exception.exceptions.ClientNotFoundException;
import biscof.clientcontactapi.model.Email;
import biscof.clientcontactapi.model.Phone;
import biscof.clientcontactapi.model.Client;
import biscof.clientcontactapi.repository.EmailRepository;
import biscof.clientcontactapi.repository.PhoneRepository;
import biscof.clientcontactapi.repository.ClientRepository;
import biscof.clientcontactapi.service.mapper.ContactMapper;
import biscof.clientcontactapi.service.mapper.ClientMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ApiService {

    private final ClientRepository clientRepository;
    private final PhoneRepository phoneRepository;
    private final EmailRepository emailRepository;
    private final ClientMapper clientMapper;
    private final ContactMapper contactMapper;

    public ClientResponseDto createClient(ClientDto clientDto) {
        Client client = clientMapper.clientDtoToClient(clientDto);
        return clientMapper.clientToClientResponseDto(clientRepository.save(client));
    }

    public List<ClientResponseDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::clientToClientResponseDto)
                .toList();
    }

    public ClientResponseDto getClientById(Long clientId) {
        return clientMapper.clientToClientResponseDto(getClientSafe(clientId));
    }

    public ContactsRespDto getAllContactsByClientId(Long clientId) {
        List<Phone> phones = phoneRepository.findByClientId(clientId);
        List<Email> emails = emailRepository.findByClientId(clientId);
        return ContactsRespDto.builder()
                .phones(phones.stream().map(contactMapper::phoneToResponseDto).toList())
                .emails(emails.stream().map(contactMapper::emailToResponseDto).toList())
                .build();
    }

    public List<PhoneRespDto> getPhonesByClientId(Long clientId) {
        List<Phone> phones = phoneRepository.findByClientId(clientId);
        return phones.stream().map(contactMapper::phoneToResponseDto).toList();
    }

    public List<EmailRespDto> getEmailsByClientId(Long clientId) {
        List<Email> emails = emailRepository.findByClientId(clientId);
        return emails.stream().map(contactMapper::emailToResponseDto).toList();
    }

    public PhoneRespDto addPhone(Long clientId, PhoneDto phoneDto) {
        Client client = getClientSafe(clientId);
        Phone phone = contactMapper.phoneDtoToPhone(phoneDto, client);

        return contactMapper.phoneToResponseDto(phoneRepository.save(phone));
    }

    public EmailRespDto addEmail(Long clientId, EmailDto emailDto) {
        Client client = getClientSafe(clientId);
        Email email = contactMapper.emailDtoToEmail(emailDto, client);

        return contactMapper.emailToResponseDto(emailRepository.save(email));
    }

    public void deleteClient(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new ClientNotFoundException(clientId)
        );
        clientRepository.delete(client);
    }

    private Client getClientSafe(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new ClientNotFoundException(clientId)
        );

        if (client.getPhones() == null) {
            client.setPhones(new ArrayList<>());
        }
        if (client.getEmails() == null) {
            client.setEmails(new ArrayList<>());
        }

        return client;
    }

}
