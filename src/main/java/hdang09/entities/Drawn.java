package hdang09.entities;

import lombok.*;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "drawn")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Drawn {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "drawn_id")
    private UUID drawnId;

    @OneToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "drawn_number")
    private int drawnNumber;

    public Drawn(Room room, int randomNumber) {
        this.room = room;
        this.drawnNumber = randomNumber;
    }
}

