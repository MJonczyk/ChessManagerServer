package app.controller.service;

import app.model.dto.GameDTO;
import app.model.entity.*;
import app.model.repository.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final EventRepository eventRepository;
    private final SiteRepository siteRepository;
    private final PlayerRepository playerRepository;
    private final ResultRepository resultRepository;
    private final ChessOpeningRepository chessOpeningRepository;
    private final CountryRepository countryRepository;

    public GameService(GameRepository gameRepository, EventRepository eventRepository, SiteRepository siteRepository,
                       PlayerRepository playerRepository, ResultRepository resultRepository,
                       ChessOpeningRepository chessOpeningRepository, CountryRepository countryRepository) {
        this.gameRepository = gameRepository;
        this.eventRepository = eventRepository;
        this.siteRepository = siteRepository;
        this.playerRepository = playerRepository;
        this.resultRepository = resultRepository;
        this.chessOpeningRepository = chessOpeningRepository;
        this.countryRepository = countryRepository;
    }

    public GameDTO getOne(Long id) {
        Game game = gameRepository.findById(id).orElse(null);

        if (game == null)
            return null;

        return new GameDTO(game);
    }

    public List<GameDTO> getAll() {
        List<Game> games = gameRepository.findAll();
        List<GameDTO> gameDTOS = new LinkedList<>();

        for(Game g : games)
            gameDTOS.add(new GameDTO(g));

        return gameDTOS;
    }

    public void delete(Long id) {
        gameRepository.deleteById(id);
    }

    public Game findOne(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    public void add(List<GameDTO> games) {
        for (GameDTO gameDTO : games) {
            Game game = parseGameDto(gameDTO);

            if (game.getEvent() == null) {
                Event event = new Event();
                event.setName(gameDTO.getEvent());
                eventRepository.save(event);
                game.setEvent(event);
            }

            if (game.getSite() == null) {
                Site site = new Site();
                site.setName(gameDTO.getSite());
                siteRepository.save(site);
                game.setSite(site);
            }

            if (game.getWhite() == null) {
                Player white = new Player();
                white.setName(gameDTO.getWhite());
                playerRepository.save(white);
                game.setWhite(white);
            }

            if (game.getBlack() == null) {
                Player black = new Player();
                black.setName(gameDTO.getBlack());
                playerRepository.save(black);
                game.setBlack(black);
            }

            if (game.getChessOpening() == null) {
                ChessOpening chessOpening= new ChessOpening();
                chessOpening.setCode(gameDTO.getChessOpening());
                chessOpeningRepository.save(chessOpening);
                game.setChessOpening(chessOpening);
            }

            gameRepository.save(game);
        }
    }

    public Game parseGameDto(GameDTO gameDTO) {
        Event event = eventRepository.findEventByName(gameDTO.getEvent());
        Player whitePlayer = playerRepository.findPlayerByName(gameDTO.getWhite());
        Player blackPlayer = playerRepository.findPlayerByName(gameDTO.getBlack());
        Result result = resultRepository.findResultByResult(gameDTO.getResult());
        ChessOpening chessOpening = chessOpeningRepository.findChessOpeningByCode(gameDTO.getChessOpening());
        Site site = siteRepository.findSiteByName(gameDTO.getSite());

        return new Game(event, site, whitePlayer, blackPlayer, result, chessOpening, gameDTO.getDate(),
                gameDTO.getRound(), gameDTO.getMoves(), gameDTO.getWhiteElo(), gameDTO.getBlackElo());
    }
}
