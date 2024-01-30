package hdang09.services;

import hdang09.dtos.responses.PlayerDetailResponseDTO;
import hdang09.entities.Player;
import hdang09.enums.ResponseStatus;
import hdang09.mappers.PlayerMapper;
import hdang09.models.Response;
import hdang09.repositories.PlayerRepository;
import hdang09.utils.AuthorizationUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final AuthorizationUtil authorizationUtil;


    @Autowired
    public PlayerService(PlayerRepository playerRepository, AuthorizationUtil authorizationUtil) {
        this.playerRepository = playerRepository;
        this.authorizationUtil = authorizationUtil;
    }

    public ResponseEntity<Response<PlayerDetailResponseDTO>> getCurrentPlayer(HttpServletRequest request) {
        // Get player id
        UUID playerId = authorizationUtil.getPlayerIdFromHeader(request);

        // Get player
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            Response<PlayerDetailResponseDTO> response = new Response<>(ResponseStatus.ERROR, "Player not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Map to response DTO
        PlayerDetailResponseDTO playerDetailResponseDTO = PlayerMapper.INSTANCE.toDetailDTO(player);

        // Return response
        Response<PlayerDetailResponseDTO> response = new Response<>(ResponseStatus.SUCCESS, "Get current player successfully", playerDetailResponseDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
