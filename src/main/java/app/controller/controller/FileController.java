package app.controller.controller;

import app.controller.service.FileService;
import app.controller.service.GameService;
import app.controller.service.UserService;
import app.model.dto.GameDTO;
import app.model.entity.Game;
import app.model.entity.Role;
import app.model.entity.User;
import app.security.service.JwtTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@RestController
public class FileController {
    private final FileService fileService;
    private final GameService gameService;
    private final UserService userService;
    private final JwtTokenGenerator tokenGenerator;
    private Logger logger = LoggerFactory.getLogger(FileController.class);

    public FileController(FileService fileService, GameService gameService, UserService userService,
                          JwtTokenGenerator tokenGenerator) {
        this.fileService = fileService;
        this.gameService = gameService;
        this.userService = userService;
        this.tokenGenerator = tokenGenerator;
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = "/upload")
    public ResponseEntity uploadPGN(@RequestParam("file") MultipartFile pgnFile,
                                    @RequestHeader("Authorization") String token) {
        token = token.substring(7);
        logger.info("POST: /upload");
        User user = userService.getOneByLogin(tokenGenerator.getUsernameFromToken(token));
        Role role = userService.getUsersRoles(user.getLogin()).get(0);
        logger.info(role.getName());
        try {
            List<GameDTO> games = fileService.parsePGN(pgnFile.getInputStream());
            gameService.add(games, role, user);
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(value = "/download/{id}")
    public ResponseEntity<byte[]> downloadPGN(@PathVariable Long id) {
        logger.info("GET: /download");
        Game game = gameService.findOne(id);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<String> lines = fileService.getPGN(new GameDTO(game));
        String content = String.join("", lines);
        byte[] byteContent = content.getBytes();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLength(byteContent.length);
        responseHeaders.setContentType(new MediaType("text", "plain"));
        responseHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=game_" + id.toString() + ".pgn");
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);

        return new ResponseEntity<byte[]>(byteContent, responseHeaders, HttpStatus.OK);
    }

    @Secured({"ROLE_USER"})
    @GetMapping(value = "/download/games/{username}")
    public ResponseEntity<byte[]> downloadDatabase(@PathVariable String username) {
        logger.info("GET: /download/games/{username}");
        List<GameDTO> games = gameService.getUsersGames(username);

        if (games.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        StringBuilder content = new StringBuilder();

        for (GameDTO game: games) {
            List<String> lines = fileService.getPGN(game);
            content.append(String.join("", lines));
            content.append("\n");
            logger.info(String.valueOf(content.length()));
        }


        byte[] byteContent = content.toString().getBytes();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLength(byteContent.length);
        responseHeaders.setContentType(new MediaType("text", "plain"));
        responseHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + username + "_db" + ".pgn");
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);

        return new ResponseEntity<byte[]>(byteContent, responseHeaders, HttpStatus.OK);
    }
}
