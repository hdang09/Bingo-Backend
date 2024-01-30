package hdang09.repositories;

import hdang09.entities.Player;
import hdang09.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {
    @Transactional
    @Modifying
    @Query("UPDATE Player p SET p.currentRoom = null, p.host = false WHERE p.currentRoom = ?1")
    void removeAllPlayerInRoom(Room room);

    Player findByEmail(String email);
}
