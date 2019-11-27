package app.model.dto;

import app.model.entity.Game;
import lombok.Data;


@Data
public class GameDTO {
    public GameDTO() {
        this.moves = "";
    }

    public GameDTO(Game game) {
        this.id = game.getId();
        this.event = game.getEvent().getName();
        this.site = game.getSite().getName();
        this.white = game.getWhite().getName();
        this.black = game.getBlack().getName();
        this.result = game.getResult().getResult();
        this.chessOpening = game.getChessOpening().getCode();
        this.date = game.getDate();
        this.round = game.getRound();
        this.moves = game.getMoves();
        this.whiteElo = game.getWhiteElo();
        this.blackElo = game.getBlackElo();
        this.gameType = game.getGameType().getName();
    }

    private Long id;
    private String event;
    private String site;
    private String white;
    private String black;
    private String result;
    private String chessOpening;
    private String date;
    private String round;
    private String moves;
    private int whiteElo;
    private int blackElo;
    private String gameType;
}
