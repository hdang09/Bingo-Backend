package hdang09.services;

import hdang09.dtos.requests.CreateRoomDTO;
import hdang09.dtos.responses.RoomResponseDTO;
import hdang09.entities.Player;
import hdang09.entities.Room;
import hdang09.enums.ResponseStatus;
import hdang09.enums.RoomStatus;
import hdang09.mappers.RoomMapper;
import hdang09.models.Response;
import hdang09.repositories.PlayerRepository;
import hdang09.repositories.RoomRepository;
import hdang09.utils.AuthorizationUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;
    private final AuthorizationUtil authorizationUtil;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public RoomService(
            RoomRepository roomRepository,
            PlayerRepository playerRepository,
            AuthorizationUtil authorizationUtil,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.roomRepository = roomRepository;
        this.playerRepository = playerRepository;
        this.authorizationUtil = authorizationUtil;
        this.messagingTemplate = messagingTemplate;
    }

    public ResponseEntity<Response<List<RoomResponseDTO>>> getAllRoom() {
        // Get all room
        List<Room> rooms = roomRepository.getAllWaitingAndPlayingRoom();

        // Map to response DTO
        List<RoomResponseDTO> roomResponseDTOs = RoomMapper.INSTANCE.toRoomResponseDTOs(rooms);

        // Return response
        Response<List<RoomResponseDTO>> response = new Response<>(ResponseStatus.SUCCESS, "Get all room successfully", roomResponseDTOs);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<Response<RoomResponseDTO>> createRoom(HttpServletRequest request, CreateRoomDTO createRoomDTO) {
        // Get player ID from token
        UUID playerId = authorizationUtil.getPlayerIdFromHeader(request);
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            Response<RoomResponseDTO> response = new Response<>(ResponseStatus.ERROR, "Player not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Check if player is already in room
        if (player.getCurrentRoom() != null) {
            Response<RoomResponseDTO> response = new Response<>(ResponseStatus.ERROR, "Player is already in room. Leave room first");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Map to entity
        Room room = RoomMapper.INSTANCE.toEntity(createRoomDTO);

        // Save room
        room = roomRepository.save(room);

        // Make player host
        player.setCurrentRoom(room);
        player.setHostRoom(room);
        playerRepository.save(player);

        // Add player to room
        room.getPlayers().add(player);

        // Map to response DTO
        RoomResponseDTO roomResponseDTO = RoomMapper.INSTANCE.toDTO(room);

        // WebSocket
        messagingTemplate.convertAndSend("/topic/rooms", roomResponseDTO);

        // Return response
        Response<RoomResponseDTO> response = new Response<>(ResponseStatus.SUCCESS, "Create room successfully", roomResponseDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<Response<Void>> joinRoom(HttpServletRequest request, UUID roomId) {
        // Get player ID from token
        UUID playerId = authorizationUtil.getPlayerIdFromHeader(request);
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Player not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Check if room exists
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Room not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Check if room is started
        if (room.getStatus().equals(RoomStatus.PLAYING) || room.getStatus().equals(RoomStatus.FINISHED)) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Room is already started");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if player is already in room
        if (player.getCurrentRoom() != null) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Player is already in room");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if room is full
        List<Player> players = room.getPlayers();
        if (players.size() >= room.getNumberOfPlayers()) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Room is full");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Add player to room
        player.setCurrentRoom(room);
        playerRepository.save(player);

        // Get all players in room with WebSocket
        messagingTemplate.convertAndSend("/topic/players/" + roomId, player);

        // WebSocket
        room.getPlayers().add(player);
        messagingTemplate.convertAndSend("/topic/rooms", room);

        // Return response
        Response<Void> response = new Response<>(ResponseStatus.SUCCESS, "Join room successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<Response<Void>> leaveRoom(HttpServletRequest request) {
        // Get player ID from token
        UUID playerId = authorizationUtil.getPlayerIdFromHeader(request);
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Player not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Check if player is in room or not
        if (player.getCurrentRoom() == null) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Player is not in room");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Check if room is already started
        boolean isHost = player.getHostRoom() != null && player.getHostRoom().equals(player.getCurrentRoom());
        if (player.getCurrentRoom().getStatus().equals(RoomStatus.PLAYING) && isHost) {
//            if (player.getCurrentRoom().getPlayers().size() != 1) {
//                Response<Void> response = new Response<>(ResponseStatus.ERROR, "You can't leave because there are still players in the room");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//            }

            // Set room to finished
            player.getCurrentRoom().setStatus(RoomStatus.FINISHED);
            roomRepository.save(player.getCurrentRoom());

            // Remove player from room
            player.setCurrentRoom(null);
            playerRepository.save(player);

            // Return response
            Response<Void> response = new Response<>(ResponseStatus.SUCCESS, "Leave room successfully. The room is finished");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        // Get all players in room with WebSocket
        UUID roomId = player.getCurrentRoom().getRoomId();
        messagingTemplate.convertAndSend("/topic/players/" + roomId, player);

        // Get all room with WebSocket
        player.getCurrentRoom().getPlayers().remove(player);
        messagingTemplate.convertAndSend("/topic/rooms", player.getCurrentRoom());

        // Remove player from room
        player.setCurrentRoom(null);
        playerRepository.save(player);

        // Return response
        Response<Void> response = new Response<>(ResponseStatus.SUCCESS, "Leave room successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<Response<RoomResponseDTO>> getPlayers(HttpServletRequest request) {
        // Get player ID from token
        UUID playerId = authorizationUtil.getPlayerIdFromHeader(request);
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            Response<RoomResponseDTO> response = new Response<>(ResponseStatus.ERROR, "Player not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Check if player is in room or not
        if (player.getCurrentRoom() == null) {
            Response<RoomResponseDTO> response = new Response<>(ResponseStatus.ERROR, "Player is not in room");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Map to response DTO
        RoomResponseDTO dto = RoomMapper.INSTANCE.toDTO(player.getCurrentRoom());

        // Return response
        Response<RoomResponseDTO> response = new Response<>(ResponseStatus.SUCCESS, "Get players in room successfully", dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<Response<Void>> startRoom(HttpServletRequest request) {
        // Get player ID from token
        UUID playerId = authorizationUtil.getPlayerIdFromHeader(request);
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Player not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Check if player is host
        boolean isHost = player.getHostRoom() != null && player.getHostRoom().equals(player.getCurrentRoom());
        if (!isHost) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Player is not host");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if player is not in room
        Room room = player.getCurrentRoom();
        if (room == null) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Player is not in room");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if room is full
        List<Player> players = room.getPlayers();
        if (players.size() > room.getNumberOfPlayers()) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Room is full");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if room is already started
        if (room.getStatus().equals(RoomStatus.PLAYING) || room.getStatus().equals(RoomStatus.FINISHED)) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Room is already started");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Set room to started
        room.setStatus(RoomStatus.PLAYING);
        roomRepository.save(room);

        // Notify the room is started with WebSocket
        messagingTemplate.convertAndSend("/topic/room-started/" + room.getRoomId(), true);

        // Get all room with WebSocket
        messagingTemplate.convertAndSend("/topic/rooms", room);

        // Return response
        Response<Void> response = new Response<>(ResponseStatus.SUCCESS, "Start room successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
