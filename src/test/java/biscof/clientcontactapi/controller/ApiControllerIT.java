package biscof.clientcontactapi.controller;

import biscof.clientcontactapi.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApiControllerIT {

    private final MockMvc mockMvc;
    private final TestUtils testUtils;
    private static final String BASE_TEST_URL = "/api/clients";

    @Autowired
    ApiControllerIT(MockMvc mockMvc, TestUtils testUtils) {
        this.mockMvc = mockMvc;
        this.testUtils = testUtils;
    }

    @BeforeEach
    void setup() {
        testUtils.initClients();
        testUtils.initClientContacts();
    }

    @Test
    void getAllClients() throws Exception {
        mockMvc.perform(get(BASE_TEST_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].firstName").value("Ivan"))
                .andExpect(jsonPath("$[1].lastName").value("Petrova"));
    }

    @Test
    void getClientById() throws Exception {
        Long clientId = testUtils.getClientIdByLastName("Petrova");
        mockMvc.perform(get(BASE_TEST_URL + "/" + clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Olga"))
                .andExpect(jsonPath("$.lastName").value("Petrova"))
                .andExpect(jsonPath("$.addedAt").isNotEmpty())
                .andExpect(jsonPath("$.id").value(clientId));
    }

    @Test
    void getClientByInvalidId() throws Exception {
        long clientId = -1L;
        mockMvc.perform(get(BASE_TEST_URL + "/" + clientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Client with ID -1 not found."))
                .andExpect(jsonPath("$.path").value("/api/clients/-1"));
    }

    @Test
    void getAllContactsByClientId() throws Exception {
        Long clientId = testUtils.getClientIdByLastName("Andreeva");

        mockMvc.perform(get(BASE_TEST_URL + "/" + clientId + "/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phones.length()").value(2))
                .andExpect(jsonPath("$.emails.length()").value(2))
                .andExpect(jsonPath("$.phones[0].phoneNumber").value("+79121234587"))
                .andExpect(jsonPath("$.emails[1].email").value("e.andre@test.net"))
                .andExpect(jsonPath("$.phones[0].clientId").value(clientId));
    }

    @Test
    void getPhonesByClientId() throws Exception {
        Long clientId = testUtils.getClientIdByLastName("Andreeva");

        mockMvc.perform(get(BASE_TEST_URL + "/" + clientId + "/phones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].phoneNumber").value("+79121234587"))
                .andExpect(jsonPath("$[1].phoneNumber").value("+78120934956"))
                .andExpect(jsonPath("$[1].phoneId").isNotEmpty())
                .andExpect(jsonPath("$[0].clientId").value(clientId));
    }

    @Test
    void getEmailsByClientId() throws Exception {
        Long clientId = testUtils.getClientIdByLastName("Andreeva");

        mockMvc.perform(get(BASE_TEST_URL + "/" + clientId + "/emails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].email").value("elena.andreeva@mail.com"))
                .andExpect(jsonPath("$[0].clientId").value(clientId))
                .andExpect(jsonPath("$[1].email").value("e.andre@test.net"))
                .andExpect(jsonPath("$[1].emailId").isNotEmpty());
    }

    @Test
    void createClientValidData() throws Exception {
        final String clientDtoJson = """
                    {
                        "firstName": "Andrei",
                        "lastName": "Ivanov"
                    }
                """;
        mockMvc.perform(post(BASE_TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientDtoJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.firstName").value("Andrei"))
                .andExpect(jsonPath("$.addedAt").isNotEmpty())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void createClientInvalidData() throws Exception {
        final String clientDtoJson = """
                    {
                        "firstName": "",
                        "lastName": " "
                    }
                """;
        mockMvc.perform(post(BASE_TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addPhoneValidData() throws Exception {
        Long clientId = testUtils.getClientIdByLastName("Ivanov");
        final String phoneDtoJson = """
                    {
                        "phone": "+74953459802"
                    }
                """;

        mockMvc.perform(post(BASE_TEST_URL + "/" + clientId + "/phones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(phoneDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(clientId))
                .andExpect(jsonPath("$.phoneId").isNotEmpty())
                .andExpect(jsonPath("$.phoneNumber").value("+74953459802"))
                .andExpect(jsonPath("$.emails").doesNotExist());
    }

    @Test
    void addPhoneInvalidData() throws Exception {
        Long clientId = testUtils.getClientIdByLastName("Ivanov");
        final String phoneDtoJson = """
                    {
                        "phone": "45-802"
                    }
                """;
        mockMvc.perform(post(BASE_TEST_URL + "/" + clientId + "/phones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(phoneDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addEmailValidData() throws Exception {
        Long clientId = testUtils.getClientIdByLastName("Petrova");
        final String emailDtoJson = """
                    {
                        "email": "olga.petrova@test.com"
                    }
                """;

        mockMvc.perform(post(BASE_TEST_URL + "/" + clientId + "/emails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emailDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(clientId))
                .andExpect(jsonPath("$.emailId").isNotEmpty())
                .andExpect(jsonPath("$.email").value("olga.petrova@test.com"))
                .andExpect(jsonPath("$.phones").doesNotExist());
    }

    @Test
    void addEmailInvalidData() throws Exception {
        Long clientId = testUtils.getClientIdByLastName("Petrova");
        final String emailDtoJson = """
                    {
                        "email": "invalid-email"
                    }
                """;
        mockMvc.perform(post(BASE_TEST_URL + "/" + clientId + "/emails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emailDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteExistingClient() throws Exception {
        Long clientId = testUtils.getClientIdByLastName("Andreeva");
        mockMvc.perform(delete(BASE_TEST_URL + "/" + clientId))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_TEST_URL + "/" + clientId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteClientDoesNotExist() throws Exception {
        long clientId = -1;
        mockMvc.perform(delete(BASE_TEST_URL + "/" + clientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Client with ID -1 not found."));
    }

}
