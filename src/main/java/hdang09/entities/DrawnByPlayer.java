package hdang09.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "drawn_by_player")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DrawnByPlayer {

    @Id
    @Column(name = "dbp_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID dpbId;

    @OneToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "drawn_number")
    private int drawnNumber;

    @OneToOne
    @JoinColumn(name = "player_id")
    private Player player;

    public DrawnByPlayer(Room room, int drawnNumber, Player player) {
        this.room = room;
        this.drawnNumber = drawnNumber;
        this.player = player;
    }
}

