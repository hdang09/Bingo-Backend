package hdang09.dtos.requests;

import hdang09.constants.AvatarConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters")
    @Schema(example = "hdang09")
    private String fullName;

    private String avatar = AvatarConstant.DEFAULT_AVATAR;
}