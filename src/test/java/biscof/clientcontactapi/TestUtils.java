package biscof.clientcontactapi;


import biscof.clientcontactapi.model.Client;
import biscof.clientcontactapi.model.Email;
import biscof.clientcontactapi.model.Phone;
import biscof.clientcontactapi.repository.ClientRepository;
import biscof.clientcontactapi.repository.EmailRepository;
import biscof.clientcontactapi.repository.PhoneRepository;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {

    private final ClientRepository clientRepository;
    private final PhoneRepository phoneRepository;
    private final EmailRepository emailRepository;

    public TestUtils(ClientRepository clientRepository, PhoneRepository phoneRepository,
                     EmailRepository emailRepository) {
        this.clientRepository = clientRepository;
        this.phoneRepository = phoneRepository;
        this.emailRepository = emailRepository;
    }

    public void initClients() {
        Client client1 = Client.builder().firstName("Ivan").lastName("Ivanov").build();
        Client client2 = Client.builder().firstName("Olga").lastName("Petrova").build();
        Client client3 = Client.builder().firstName("Elena").lastName("Andreeva").build();
        clientRepository.save(client1);
        clientRepository.save(client2);
        clientRepository.save(client3);
    }

    public void initClientContacts() {
        Client client = clientRepository.findByLastName("Andreeva").orElseThrow();

        Phone phone1 = Phone.builder().phone("+79121234587").client(client).build();
        Phone phone2 = Phone.builder().phone("+78120934956").client(client).build();
        phoneRepository.save(phone1);
        phoneRepository.save(phone2);

        Email email1 = Email.builder().email("elena.andreeva@mail.com").client(client).build();
        Email email2 = Email.builder().email("e.andre@test.net").client(client).build();
        emailRepository.save(email1);
        emailRepository.save(email2);
    }

    public Long getClientIdByLastName(String lastName) {
        return clientRepository.findByLastName(lastName).orElseThrow().getId();
    }
}
