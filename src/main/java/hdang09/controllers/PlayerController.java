package hdang09.controllers;

import hdang09.dtos.responses.PlayerDetailResponseDTO;
import hdang09.models.Response;
import hdang09.services.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/player")
@Tag(name = "Player")
@SecurityRequirement(name = "bearerAuth")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService){
        this.playerService = playerService;
    }

    @Operation(summary = "Get current player info")
    @GetMapping("/me")
    public ResponseEntity<Response<PlayerDetailResponseDTO>> getCurrentPlayer(HttpServletRequest request){
        return playerService.getCurrentPlayer(request);
    }
}
