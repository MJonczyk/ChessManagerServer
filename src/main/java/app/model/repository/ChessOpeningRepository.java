package app.model.repository;

import app.model.entity.ChessOpening;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChessOpeningRepository extends JpaRepository<ChessOpening, Long> {
}
