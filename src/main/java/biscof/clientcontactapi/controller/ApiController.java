package biscof.clientcontactapi.controller;

import biscof.clientcontactapi.dto.EmailDto;
import biscof.clientcontactapi.dto.PhoneDto;
import biscof.clientcontactapi.dto.ClientDto;
import biscof.clientcontactapi.dto.response.ContactsRespDto;
import biscof.clientcontactapi.dto.response.ClientResponseDto;
import biscof.clientcontactapi.dto.response.EmailRespDto;
import biscof.clientcontactapi.dto.response.PhoneRespDto;
import biscof.clientcontactapi.exception.ErrorResponse;
import biscof.clientcontactapi.service.ApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("${base-url}/clients")
@AllArgsConstructor
public class ApiController {

    private final ApiService apiService;

    @Operation(summary = "Get all clients")
    @ApiResponse(responseCode = "200", description = "Clients found",
            content = { @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ClientResponseDto.class))) }
    )
    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> getAllClients() {
        return ResponseEntity.ok().body(apiService.getAllClients());
    }

    @Operation(summary = "Get a client by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client successfully found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientResponseDto.class)) }),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content) }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDto> getClientById(
            @Parameter(description = "Client's ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(apiService.getClientById(id));
    }

    @Operation(summary = "Get all contacts by client's ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client contacts found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactsRespDto.class))
                    })
    })
    @GetMapping("/{id}/contacts")
    public ResponseEntity<ContactsRespDto> getAllContactsByClientId(
            @Parameter(description = "Client's ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(apiService.getAllContactsByClientId(id));
    }

    @Operation(summary = "Get all phone numbers of a client by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client's phone numbers found",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PhoneRespDto.class)))
                    })
    })
    @GetMapping("/{id}/phones")
    public ResponseEntity<List<PhoneRespDto>> getPhonesByClientId(
            @Parameter(description = "Client's ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(apiService.getPhonesByClientId(id));
    }

    @Operation(summary = "Get all emails of a client by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client's emails found",
            content = { @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = EmailRespDto.class)))
            })
    })
    @GetMapping("/{id}/emails")
    public ResponseEntity<List<EmailRespDto>> getEmailsByClientId(
            @Parameter(description = "Client's ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(apiService.getEmailsByClientId(id));
    }

    @Operation(summary = "Add a new client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Client successfully added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientResponseDto.class))}),
        @ApiResponse(responseCode = "400", description = "Wrong data provided",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))
                    })
    })
    @PostMapping
    public ResponseEntity<ClientResponseDto> createClient(
            @Schema(implementation = ClientDto.class) @Valid @RequestBody ClientDto clientDto
    ) {
        ClientResponseDto client = apiService.createClient(clientDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.getId())
                .toUri();
        return ResponseEntity.created(location).body(client);
    }

    @Operation(summary = "Add a new phone number by client's ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Phone number successfully added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PhoneRespDto.class))}),
        @ApiResponse(responseCode = "400", description = "Wrong data provided",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))
                    })
    })
    @PostMapping("/{id}/phones")
    public ResponseEntity<PhoneRespDto> addPhone(
            @Schema(implementation = PhoneDto.class) @Valid @RequestBody PhoneDto phoneDto,
            @Parameter(description = "Client's ID") @PathVariable(name = "id") Long clientId
    ) {
        return ResponseEntity.ok().body(apiService.addPhone(clientId, phoneDto));
    }

    @Operation(summary = "Add a new email by client's ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email successfully added",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = EmailRespDto.class))}),
        @ApiResponse(responseCode = "400", description = "Wrong data provided",
                content = {@Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))
                })
    })
    @PostMapping("/{id}/emails")
    public ResponseEntity<EmailRespDto> addEmail(
            @Schema(implementation = EmailDto.class) @Valid @RequestBody EmailDto emailDto,
            @Parameter(description = "Client's ID") @PathVariable(name = "id") Long clientId
    ) {
        return ResponseEntity.ok(apiService.addEmail(clientId, emailDto));
    }

    @Operation(summary = "Delete a client by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client deleted", content = @Content),
        @ApiResponse(responseCode = "404", description = "Client not found",
                    content = { @Content(schema = @Schema(implementation = ErrorResponse.class)) }),
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Client's ID") @PathVariable Long id
    ) {
        apiService.deleteClient(id);
        return ResponseEntity.ok().build();
    }

}
