package hdang09.mappers;

import hdang09.dtos.requests.CreateRoomDTO;
import hdang09.dtos.responses.AllRoomResponseDTO;
import hdang09.dtos.responses.RoomResponseDTO;
import hdang09.entities.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RoomMapper {

    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    Room toEntity(CreateRoomDTO createRoomDTO);

    RoomResponseDTO toDTO(Room room);

    @Mapping(target = "joinedPlayers", expression = "java(room.getPlayers().size())")
    @Mapping(target = "maximumPlayers", source = "numberOfPlayers")
    AllRoomResponseDTO roomToAllRoomResponseDTO(Room room);

    List<AllRoomResponseDTO> roomsToAllRoomResponseDTOs(List<Room> rooms);

}
