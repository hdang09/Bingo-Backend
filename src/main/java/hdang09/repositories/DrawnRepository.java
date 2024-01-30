package hdang09.repositories;

import hdang09.entities.Drawn;
import hdang09.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DrawnRepository extends JpaRepository<Drawn, UUID> {
    List<Drawn> findAllByRoom(Room room);
}
