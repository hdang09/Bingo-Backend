package hdang09.repositories;

import hdang09.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

    @Query("SELECT r FROM Room r WHERE r.status = 'WAITING' OR r.status = 'PLAYING'")
    List<Room> getAllWaitingAndPlayingRoom();

}
