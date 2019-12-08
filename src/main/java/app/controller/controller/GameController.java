package app.controller.controller;

import app.controller.assembler.GameResourceAssembler;
import app.controller.controller.exception.GameNotFoundException;
import app.controller.service.GameService;
import app.controller.service.UserService;
import app.model.dto.GameDTO;
import app.model.entity.User;
import app.security.service.JwtTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class GameController {
    private final GameService gameService;
    private final UserService userService;
    private final JwtTokenGenerator tokenGenerator;
    private final GameResourceAssembler gameResourceAssembler;
    private Logger logger = LoggerFactory.getLogger(GameController.class);

    GameController(GameService gameService, UserService userService, JwtTokenGenerator tokenGenerator,
                   GameResourceAssembler gameResourceAssembler) {
        this.gameService = gameService;
        this.userService = userService;
        this.tokenGenerator = tokenGenerator;
        this.gameResourceAssembler = gameResourceAssembler;
    }

    @GetMapping("/games")
    public Resources<Resource<GameDTO>> getAll(@RequestHeader(required = false, value = "Authorization") String token) {
        logger.info("GET: /games");
        logger.info(token);
        User user;

        if (token == null)
            user = null;
        else {
            token = token.substring(7);
            user = userService.getOneByLogin(tokenGenerator.getUsernameFromToken(token));
        }

        List<Resource<GameDTO>> games = gameService.getAll(user).stream()
                .map(gameResourceAssembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(games, linkTo(methodOn(GameController.class).getAll(token)).withSelfRel());
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/games/{id}")
    public Resource<GameDTO> getOne(@PathVariable Long id) {
        logger.info("GET: /games/id");
        GameDTO game = gameService.getOne(id);


        if(game == null)
            throw new GameNotFoundException(id);

        return gameResourceAssembler.toResource(game);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @DeleteMapping("/games/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        logger.info("DELETE: /games");
        gameService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
