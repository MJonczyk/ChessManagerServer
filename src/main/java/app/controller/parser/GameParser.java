package app.controller.parser;

import app.model.dto.GameDTO;

import java.io.InputStream;
import java.util.List;

public interface GameParser {
    List<String> gameToFile(GameDTO gameDTO);
    List<GameDTO> fileToGame(InputStream inputStream);
}
