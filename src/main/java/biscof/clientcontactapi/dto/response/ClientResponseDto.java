package biscof.clientcontactapi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class ClientResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private Instant addedAt;

}
