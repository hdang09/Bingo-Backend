package hdang09.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "player")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "player_id")
    private UUID playerId;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "full_name")
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "current_room_id", referencedColumnName = "room_id")
    @JsonBackReference
    private Room currentRoom;

    @ManyToOne
    @JoinColumn(name = "host_room_id", referencedColumnName = "room_id")
    @JsonBackReference
    private Room hostRoom;

    @Column(name = "email")
    private String email;

    @Column(name = "balance")
    private long balance = 100000;

    public Player(String avatarUrl, String fullName, String email) {
        this.avatarUrl = avatarUrl;
        this.fullName = fullName;
        this.email = email;
    }
}

