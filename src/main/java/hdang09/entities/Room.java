package hdang09.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import hdang09.enums.RoomStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.*;

@Entity
@Table(name = "room")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "room_id")
    private UUID roomId;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "bet_money")
    private int betMoney;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RoomStatus status = RoomStatus.WAITING;

    @Column(name = "width")
    private int width;

    @Column(name = "height")
    private int height;

    @Column(name = "max_num_each_row")
    private int maxNumberEachRow;

    @Column(name = "num_of_players")
    private int numberOfPlayers;

    @OneToMany(mappedBy = "currentRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Player> players = new ArrayList<>();

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomName='" + roomName + '\'' +
                ", betMoney=" + betMoney +
                ", status=" + status +
                ", width=" + width +
                ", height=" + height +
                ", maxNumberEachRow=" + maxNumberEachRow +
                ", numberOfPlayers=" + numberOfPlayers +
                '}';
    }
}

