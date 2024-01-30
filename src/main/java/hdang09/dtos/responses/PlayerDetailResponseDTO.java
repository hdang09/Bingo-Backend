package hdang09.dtos.responses;

import hdang09.entities.Room;
import lombok.Data;

import java.util.UUID;

@Data
public class PlayerDetailResponseDTO {
    private UUID playerId;
    private String avatarUrl;
    private String fullName;
    private Room currentRoom;
    private boolean host;
    private String email;
    private long balance;
}
