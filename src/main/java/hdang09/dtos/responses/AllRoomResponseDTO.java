package hdang09.dtos.responses;

import hdang09.enums.RoomStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class AllRoomResponseDTO {
    private UUID roomId;
    private String roomName;
    private int joinedPlayers;
    private int maximumPlayers;
    private int betMoney;
    private RoomStatus status;
}
