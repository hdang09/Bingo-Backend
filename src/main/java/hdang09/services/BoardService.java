package hdang09.services;

import hdang09.entities.*;
import hdang09.enums.ResponseStatus;
import hdang09.enums.RoomStatus;
import hdang09.models.Response;
import hdang09.repositories.*;
import hdang09.utils.ArrayUtil;
import hdang09.utils.AuthorizationUtil;
import hdang09.utils.BingoBoardUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class BoardService {

    private final DrawnRepository drawnRepository;
    private final DrawnByPlayerRepository drawnByPlayerRepository;
    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;
    private final BoardRepository boardRepository;
    private final AuthorizationUtil authorizationUtil;

    @Autowired
    public BoardService(
            DrawnRepository drawnRepository,
            DrawnByPlayerRepository drawnByPlayerRepository,
            RoomRepository roomRepository,
            PlayerRepository playerRepository,
            BoardRepository boardRepository,
            AuthorizationUtil authorizationUtil
    ) {
        this.drawnRepository = drawnRepository;
        this.drawnByPlayerRepository = drawnByPlayerRepository;
        this.roomRepository = roomRepository;
        this.playerRepository = playerRepository;
        this.boardRepository = boardRepository;
        this.authorizationUtil = authorizationUtil;
    }

    public ResponseEntity<Response<int[][]>> getBoard(HttpServletRequest request) {
        // Get player
        UUID playerId = authorizationUtil.getPlayerIdFromHeader(request);
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            Response<int[][]> response = new Response<>(ResponseStatus.ERROR, "Player not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Get room
        Room room = player.getCurrentRoom();
        if (room == null) {
            Response<int[][]> response = new Response<>(ResponseStatus.ERROR, "Player is not in room");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Check if room is started
        if (!room.getStatus().equals(RoomStatus.PLAYING)) {
            Response<int[][]> response = new Response<>(ResponseStatus.ERROR, "Room is not started");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if player has a board
        Board currentBoard = boardRepository.findByRoomAndPlayer(room, player);
        if (currentBoard != null) {
            // Convert player board to 2D array
            String playerBoardString = currentBoard.getBoard();
            int[][] playerBoard = ArrayUtil.convertStringTo2DArray(playerBoardString);

            // Return response
            Response<int[][]> response = new Response<>(ResponseStatus.SUCCESS, "Get board successfully", playerBoard);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        // Generate board
        int[][] board = BingoBoardUtil.generateBingoBoard(room.getWidth(), room.getHeight(), room.getMaxNumberEachRow());

        // Save board to database
        Board boardEntity = new Board(room, player, Arrays.deepToString(board));
        boardRepository.save(boardEntity);

        // Return response
        Response<int[][]> response = new Response<>(ResponseStatus.SUCCESS, "Get board successfully", board);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<Response<Set<Integer>>> getAllDrawnNumber(HttpServletRequest request) {
        // Get player
        UUID playerId = authorizationUtil.getPlayerIdFromHeader(request);
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            Response<Set<Integer>> response = new Response<>(ResponseStatus.ERROR, "Player not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Get room
        Room room = player.getCurrentRoom();
        if (room == null) {
            Response<Set<Integer>> response = new Response(ResponseStatus.ERROR, "Room not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Check if room is started
        if (!room.getStatus().equals(RoomStatus.PLAYING)) {
            Response<Set<Integer>> response = new Response<>(ResponseStatus.ERROR, "Room is not started");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Get all drawn number
        List<Drawn> drawnList = drawnRepository.findAllByRoom(room);
        Set<Integer> drawnNumber = drawnList.stream().map(Drawn::getDrawnNumber).collect(Collectors.toSet());

        // Return response
        Response<Set<Integer>> response = new Response<>(ResponseStatus.SUCCESS, "Get all drawn number", drawnNumber);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<Response<Set<Integer>>> getAllDrawnNumberByPlayer(HttpServletRequest request) {
        // Get player
        UUID playerId = authorizationUtil.getPlayerIdFromHeader(request);
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            Response<Set<Integer>> response = new Response<>(ResponseStatus.ERROR, "Player not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Get room
        Room room = player.getCurrentRoom();
        if (room == null) {
            Response<Set<Integer>> response = new Response(ResponseStatus.ERROR, "Player is not in room");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Check if room is started
        if (!room.getStatus().equals(RoomStatus.PLAYING)) {
            Response<Set<Integer>> response = new Response<>(ResponseStatus.ERROR, "Room is not started");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Get all player drawn number
        List<DrawnByPlayer> drawnList = drawnByPlayerRepository.findAllByRoomAndPlayer(room, player);
        Set<Integer> drawnNumber = drawnList.stream().map(DrawnByPlayer::getDrawnNumber).collect(Collectors.toSet());

        // Return response
        Response<Set<Integer>> response = new Response<>(ResponseStatus.SUCCESS, "Get all drawn number by player", drawnNumber);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<Response<Void>> drawn(HttpServletRequest request, int drawnNumber) {
        // Get player
        UUID playerId = authorizationUtil.getPlayerIdFromHeader(request);
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Player not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Get room
        Room room = player.getCurrentRoom();
        if (room == null) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Player is not in room");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Check if room is started
        if (!room.getStatus().equals(RoomStatus.PLAYING)) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Room is not started");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if bet number is valid
        int max = room.getWidth() * 10;
        if (drawnNumber < 1 || drawnNumber > max) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Drawn number must be between 1 and " + max);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // TODO: Check if player has enough money

        // Check if system drawn number contains my drawn number
        List<Drawn> drawnList = drawnRepository.findAllByRoom(room);
        Set<Integer> drawnNumberSet = drawnList.stream().map(Drawn::getDrawnNumber).collect(Collectors.toSet());
        if (!drawnNumberSet.contains(drawnNumber)) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "This number does not exist");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if player has drawn this number
        List<DrawnByPlayer> drawnByPlayerList = drawnByPlayerRepository.findAllByRoomAndPlayer(room, player);
        Set<Integer> drawnByPlayerNumberSet = drawnByPlayerList.stream().map(DrawnByPlayer::getDrawnNumber).collect(Collectors.toSet());
        if (drawnByPlayerNumberSet.contains(drawnNumber)) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "You have drawn this number");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Drawn a number
        DrawnByPlayer drawn = new DrawnByPlayer(room, drawnNumber, player);
        drawnByPlayerRepository.save(drawn);

        // Get player board
        Board boardEntity = boardRepository.findByRoomAndPlayer(room, player);
        if (boardEntity == null) {
            Response<Void> response = new Response<>(ResponseStatus.ERROR, "Player does not have a board");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Convert player board to 2D array
        String playerBoardString = boardEntity.getBoard();
        int[][] playerBoard = ArrayUtil.convertStringTo2DArray(playerBoardString);

        // Check if player is winner
        boolean isBingo = BingoBoardUtil.isBingo(playerBoard, drawnByPlayerNumberSet, drawnNumber);
        if (isBingo) {
            // Remove all player in the room
            playerRepository.removeAllPlayerInRoom(room);

            room.setStatus(RoomStatus.FINISHED);
            roomRepository.save(room);

            // Return response
            Response<Void> response = new Response<>(ResponseStatus.SUCCESS, "BINGO! You are the winner");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        // Return response
        Response<Void> response = new Response<>(ResponseStatus.SUCCESS, "Drawn successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    public ResponseEntity<Response<Integer>> call(HttpServletRequest request) {
        // Get player
        UUID playerId = authorizationUtil.getPlayerIdFromHeader(request);
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            Response<Integer> response = new Response<>(ResponseStatus.ERROR, "Player not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Get room
        Room room = player.getCurrentRoom();
        if (room == null) {
            Response<Integer> response = new Response<>(ResponseStatus.ERROR, "Player is not in room");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Check if room is started
        if (!room.getStatus().equals(RoomStatus.PLAYING)) {
            Response<Integer> response = new Response<>(ResponseStatus.ERROR, "Room is not started");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if player is a host
        if (!player.isHost()) {
            Response<Integer> response = new Response<>(ResponseStatus.ERROR, "You cannot call a number because you are not a host");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Get all drawn number
        List<Drawn> drawnList = drawnRepository.findAllByRoom(room);
        Set<Integer> drawnNumber = drawnList.stream().map(Drawn::getDrawnNumber).collect(Collectors.toSet());

        // Generate a random number
        int randomNumber = 0;
        do {
            randomNumber = BingoBoardUtil.generateRandomNumber(1, room.getWidth() * 10);
        } while (drawnNumber.contains(randomNumber));

        // Add drawn number to database
        Drawn drawn = new Drawn(room, randomNumber);
        drawnRepository.save(drawn);

        // Return response
        Response<Integer> response = new Response<>(ResponseStatus.SUCCESS, "Call successfully", randomNumber);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
