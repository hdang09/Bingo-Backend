package hdang09.controllers;

import hdang09.models.Response;
import hdang09.services.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/board")
@Tag(name = "Board")
@SecurityRequirement(name = "bearerAuth")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService){
        this.boardService = boardService;
    }

    @Operation(summary = "Get board game")
    @GetMapping
    public ResponseEntity<Response<int[][]>> getBoard(HttpServletRequest request) {
        return boardService.getBoard(request);
    }

    @Operation(summary = "Get all drawn number of a room")
    @GetMapping("/drawn")
    public ResponseEntity<Response<Set<Integer>>> getAllDrawnNumber(HttpServletRequest request) {
        return boardService.getAllDrawnNumber(request);
    }

    @Operation(summary = "Get all my drawn number")
    @GetMapping("/drawn/my-number")
    public ResponseEntity<Response<Set<Integer>>> getAllDrawnNumberByPlayer(HttpServletRequest request) {
        return boardService.getAllDrawnNumberByPlayer(request);
    }

    @Operation(summary = "Drawn a number")
    @PostMapping("/drawn/{drawnNumber}")
    public ResponseEntity<Response<Void>> drawn(
            HttpServletRequest request,
            @PathVariable int drawnNumber
    ) {
        return boardService.drawn(request, drawnNumber);
    }

    @Operation(summary = "Call a number")
    @PostMapping("/call")
    public ResponseEntity<Response<Integer>> callANumber(
            HttpServletRequest request
    ) {
        return boardService.callANumber(request);
    }
}
