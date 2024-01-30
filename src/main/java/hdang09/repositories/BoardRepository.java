package hdang09.repositories;

import hdang09.entities.Board;
import hdang09.entities.Player;
import hdang09.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BoardRepository extends JpaRepository<Board, UUID> {

    Board findByRoomAndPlayer(Room room, Player player);
}
