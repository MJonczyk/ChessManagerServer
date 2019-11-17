package app.controller.controller;

import app.controller.assembler.GameResourceAssembler;
import app.controller.controller.exception.GameNotFoundException;
import app.controller.service.GameService;
import app.model.dto.GameDTO;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class GameController {
    private final GameService gameService;
    private final GameResourceAssembler gameResourceAssembler;

    GameController(GameService gameService, GameResourceAssembler gameResourceAssembler) {
        this.gameService = gameService;
        this.gameResourceAssembler = gameResourceAssembler;
    }

    @GetMapping("/games")
    public Resources<Resource<GameDTO>> getAll() {
        List<Resource<GameDTO>> games = gameService.getAll().stream()
                .map(gameResourceAssembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(games, linkTo(methodOn(GameController.class).getAll()).withSelfRel());
    }

    @GetMapping("/game/{id}")
    public Resource<GameDTO> getOne(@PathVariable Long id) {
        GameDTO game = gameService.getOne(id);

        if(game == null)
            throw new GameNotFoundException(id);

        return gameResourceAssembler.toResource(game);
    }

    @DeleteMapping("/game/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        gameService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
