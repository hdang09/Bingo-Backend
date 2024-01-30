package hdang09.mappers;

import hdang09.dtos.requests.LoginDTO;
import hdang09.dtos.responses.PlayerDetailResponseDTO;
import hdang09.dtos.responses.PlayerResponseDTO;
import hdang09.entities.Player;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PlayerMapper {
    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    Player toPlayer(LoginDTO loginDTO);

    List<PlayerResponseDTO> toDTOs(List<Player> players);

    PlayerResponseDTO toDTO(Player player);

    PlayerDetailResponseDTO toDetailDTO(Player player);
}
