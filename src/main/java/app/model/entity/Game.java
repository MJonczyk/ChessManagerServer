package app.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue
    @Column(name = "game_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;

    @ManyToOne
    @JoinColumn(name = "white_id")
    private Player white;

    @ManyToOne
    @JoinColumn(name = "black_id")
    private Player black;

    @ManyToOne
    @Column(name = "result_id")
    private Result result;

    @ManyToOne
    @JoinColumn(name = "chess_opening_id")
    private ChessOpening chessOpening;

    @Column(name = "game_date")
    private Date date;

    @Column(name = "round")
    private String round;

    @Column(name = "moves")
    private String moves;

    @Column(name = "white_elo")
    private int whiteElo;

    @Column(name = "black_elo")
    private int blackElo;
}
