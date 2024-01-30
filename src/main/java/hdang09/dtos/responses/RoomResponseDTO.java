package hdang09.dtos.responses;

import hdang09.enums.RoomStatus;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RoomResponseDTO {
    private UUID roomId;
    private String roomName;
    private int betMoney;
    private RoomStatus status;
    private int width;
    private int height;
    private int maxNumberEachRow;
    private int numberOfPlayers;
    private List<PlayerResponseDTO> players;
}
