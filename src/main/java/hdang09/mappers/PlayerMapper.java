package hdang09.mappers;

import hdang09.dtos.requests.LoginDTO;
import hdang09.dtos.responses.PlayerDetailResponseDTO;
import hdang09.entities.Player;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface PlayerMapper {
    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    Player toPlayer(LoginDTO loginDTO);

    PlayerDetailResponseDTO toDetailDTO(Player player);
}
