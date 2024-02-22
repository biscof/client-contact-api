package biscof.clientcontactapi.exception.exceptions;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(long id) {
        super(String.format("Client with ID %d not found.", id));
    }

}
