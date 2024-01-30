package hdang09.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "board")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "board_id")
    private UUID boardId;

    @OneToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "board")
    private String board;

    public Board(Room room, Player player, String string) {
        this.room = room;
        this.player = player;
        this.board = string;
    }
}

