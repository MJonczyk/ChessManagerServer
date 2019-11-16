package app.controller;

import app.controller.service.GameService;
import app.model.dto.GameDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameController {
    private final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games")
    List<GameDTO> getAll() {
        return gameService.getAll();
    }

    @GetMapping("/game/{id}")
    GameDTO getOne(@PathVariable Long id) {
        return gameService.getOne(id);
    }

    @DeleteMapping("/game/{id}")
    void delete(@PathVariable Long id) {
        gameService.delete(id);
    }
}
