package hdang09.dtos.responses;

import lombok.Data;

import java.util.UUID;

@Data
public class PlayerResponseDTO {
    private UUID playerId;
    private String avatarUrl;
    private String fullName;
    private boolean host;
    private String email;
    private long balance;
}
