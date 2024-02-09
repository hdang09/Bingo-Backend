package hdang09.controllers;

import hdang09.dtos.requests.CreateRoomDTO;
import hdang09.dtos.responses.AllRoomResponseDTO;
import hdang09.dtos.responses.RoomResponseDTO;
import hdang09.models.Response;
import hdang09.services.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/room")
@Tag(name = "Room")
@SecurityRequirement(name = "bearerAuth")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService){
        this.roomService = roomService;
    }

    @Operation(summary = "Get all room")
    @GetMapping("/all")
    public ResponseEntity<Response<List<AllRoomResponseDTO>>> getAllRoom() {
        return roomService.getAllRoom();
    }

    @Operation(summary = "Create room")
    @PostMapping("/create")
    public ResponseEntity<Response<RoomResponseDTO>> createRoom(HttpServletRequest request, @Valid @RequestBody CreateRoomDTO createRoomDTO) {
        return roomService.createRoom(request, createRoomDTO);
    }

    @Operation(summary = "Join room")
    @PostMapping("/join/{roomId}")
    public ResponseEntity<Response<Void>> joinRoom(HttpServletRequest request, @PathVariable UUID roomId) {
        return roomService.joinRoom(request, roomId);
    }

    @Operation(summary = "Leave room")
    @DeleteMapping ("/leave")
    public ResponseEntity<Response<Void>> leaveRoom(HttpServletRequest request) {
        return roomService.leaveRoom(request);
    }

    @Operation(summary = "Get all players in the current room")
    @GetMapping("/players")
    public ResponseEntity<Response<RoomResponseDTO>> getPlayers(HttpServletRequest request) {
        return roomService.getPlayers(request);
    }

    @Operation(summary = "Start game")
    @GetMapping("/start")
    public ResponseEntity<Response<Void>> startRoom(HttpServletRequest request) {
        return roomService.startRoom(request);
    }
}
