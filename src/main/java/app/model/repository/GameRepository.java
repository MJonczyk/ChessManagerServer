package app.model.repository;

import app.model.entity.Game;
import app.model.entity.GameType;
import app.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByUserEquals(User user);
    List<Game> findAllByGameTypeEquals(GameType gameType);
}
