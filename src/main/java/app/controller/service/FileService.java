package app.controller.service;

import app.controller.parser.PGNParser;
import app.model.dto.GameDTO;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class FileService {
    private final PGNParser pgnParser;

    public FileService() {
        this.pgnParser = new PGNParser();
    }

    public List<GameDTO> parsePGN(InputStream pgnStream) {
        return pgnParser.fileToGame(pgnStream);
    }

    public List<String> getPGN(GameDTO gameDTO) {
        return pgnParser.gameToFile(gameDTO);
    }
}
