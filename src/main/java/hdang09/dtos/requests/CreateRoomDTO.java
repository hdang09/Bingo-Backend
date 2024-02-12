package hdang09.dtos.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateRoomDTO {
    @NotBlank(message = "Room name is required")
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters")
    private String roomName;

    @Min(value = 1000, message = "Bet money must be greater than 1.000")
    @Max(value = 1000000, message = "Bet money must be less than 1.000.000")
    private int betMoney;

    @Min(value = 2, message = "Number of players must be greater than 2")
    @Max(value = 15, message = "Number of players must be less than 15")
    private int numberOfPlayers = 15;

    @Min(value = 2, message = "Width must be greater than 2")
    @Max(value = 7, message = "Width must be less than 7")
    private int width = 7;

    @Min(value = 2, message = "Height must be greater than 2")
    @Max(value = 9, message = "Height must be less than 9")
    private int height = 9;

    @Min(value = 1, message = "Max number of each row must be greater than 1")
    @Max(value = 10, message = "Max number of each row must be less than 10")
    private int maxNumberEachRow = 4;
}
