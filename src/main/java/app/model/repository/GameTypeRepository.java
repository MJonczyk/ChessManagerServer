package app.model.repository;

import app.model.entity.GameType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameTypeRepository extends JpaRepository<GameType, Long> {
    GameType findGameTypeByName(String name);
}
