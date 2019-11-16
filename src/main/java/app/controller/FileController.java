package app.controller;

import app.controller.service.FileService;
import app.controller.service.GameService;
import app.model.dto.GameDTO;
import app.model.entity.Game;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class FileController {
    private final FileService fileService;
    private final GameService gameService;

    public FileController(FileService fileService, GameService gameService) {
        this.fileService = fileService;
        this.gameService = gameService;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity uploadPGN(@RequestParam("file") MultipartFile pgnFile) {
        try {
            List<GameDTO> games = fileService.parsePGN(pgnFile.getInputStream());
            gameService.add(games);
        }
        catch (IOException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/download/{id}")
    public ResponseEntity<byte[]> downloadPGN(@PathVariable Long id) {
        Game game = gameService.findOne(id);

        if (game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<String> lines = fileService.getPGN(new GameDTO(game));
        String content = String.join("", lines);
        byte[] byteContent = content.getBytes();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLength(byteContent.length);
        responseHeaders.setContentType(new MediaType("text", "plain"));
        responseHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + id.toString());

        return new ResponseEntity<byte[]>(byteContent, responseHeaders, HttpStatus.OK);
    }
}
