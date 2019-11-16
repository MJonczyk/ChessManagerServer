package app.model.repository;

import app.model.entity.ChessOpening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChessOpeningRepository extends JpaRepository<ChessOpening, Long> {
    ChessOpening findChessOpeningByCode(String code);
}
