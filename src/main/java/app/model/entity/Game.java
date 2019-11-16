package app.model.entity;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JoinColumn(name = "result_id")
    private Result result;

    @ManyToOne
    @JoinColumn(name = "chess_opening_id")
    private ChessOpening chessOpening;

    @Column(name = "game_date")
    private String date;

    @Column(name = "round")
    private String round;

    @Column(name = "moves")
    private String moves;

    @Column(name = "white_elo")
    private int whiteElo;

    @Column(name = "black_elo")
    private int blackElo;

    public Game() {}

    public Game(Event event, Site site, Player white, Player black, Result result, ChessOpening chessOpening,
                String date, String round, String moves, int whiteElo, int blackElo) {
        this.event = event;
        this.site = site;
        this.white = white;
        this.black = black;
        this.result = result;
        this.chessOpening = chessOpening;
        this.date = date;
        this.round = round;
        this.moves = moves;
        this.whiteElo = whiteElo;
        this.blackElo = blackElo;
    }
}
