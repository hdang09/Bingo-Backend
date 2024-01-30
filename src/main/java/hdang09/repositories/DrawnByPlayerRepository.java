package hdang09.repositories;

import hdang09.entities.DrawnByPlayer;
import hdang09.entities.Player;
import hdang09.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DrawnByPlayerRepository extends JpaRepository<DrawnByPlayer, UUID> {

    List<DrawnByPlayer> findAllByRoomAndPlayer(Room room, Player player);
}
