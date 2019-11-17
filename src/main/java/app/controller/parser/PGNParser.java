package app.controller.parser;

import app.model.dto.GameDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PGNParser implements GameParser{

    @Override
    public List<String> gameToFile(GameDTO gameDTO) {
        LinkedList<String> lines = new LinkedList<>();

        lines.add(createValue("Event", gameDTO.getEvent()));
        lines.add("\n");
        lines.add(createValue("Site", gameDTO.getSite()));
        lines.add("\n");
        lines.add(createValue("Date", gameDTO.getDate()));
        lines.add("\n");
        lines.add(createValue("Round", gameDTO.getRound()));
        lines.add("\n");
        lines.add(createValue("White", gameDTO.getWhite()));
        lines.add("\n");
        lines.add(createValue("Black", gameDTO.getBlack()));
        lines.add("\n");
        lines.add(createValue("Result", gameDTO.getResult()));
        lines.add("\n");
        if (gameDTO.getWhiteElo() == -1)
            lines.add(createValue("WhiteElo", ""));
        else
            lines.add(createValue("WhiteElo", Integer.toString(gameDTO.getWhiteElo())));
        lines.add("\n");

        if (gameDTO.getBlackElo() == -1)
            lines.add(createValue("BlackElo", ""));
        else
            lines.add(createValue("BlackElo", Integer.toString(gameDTO.getBlackElo())));
        lines.add("\n");

        if (gameDTO.getChessOpening() != null)
            lines.add(createValue("ECO", gameDTO.getChessOpening()));
        lines.add("\n");
        lines.add("\n");

        lines.addAll(createMoves(gameDTO.getMoves()));
        

        return lines;
    }

    @Override
    public List<GameDTO> fileToGame(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        GameDTO gameDTO = new GameDTO();
        LinkedList<GameDTO> games = new LinkedList<>();
        int blankLines = 0;

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("Event")) {
                gameDTO.setEvent(getValue(line));
            } else if (line.contains("Site")) {
                gameDTO.setSite(getValue(line));
            } else if (line.contains("Date")) {
                gameDTO.setDate(getValue(line));
            } else if (line.contains("Round")) {
                gameDTO.setRound(getValue(line));
            } else if (line.contains("WhiteElo")) {
                gameDTO.setWhiteElo(valueToElo(getValue(line)));
            } else if (line.contains("BlackElo")) {
                gameDTO.setBlackElo(valueToElo(getValue(line)));
            } else if (line.contains("White")) {
                gameDTO.setWhite(getValue(line));
            } else if (line.contains("Black")) {
                gameDTO.setBlack(getValue(line));
            } else if (line.contains("Result")) {
                gameDTO.setResult(getValue(line));
            } else if (line.contains("ECO")) {
                gameDTO.setChessOpening(getValue(line));
            } else if (!line.isEmpty() && !line.contains("[")) {
                if (gameDTO.getMoves().length() == 0) {
                    gameDTO.setMoves(line);
                } else {
                    String moves = gameDTO.getMoves() + " " + line;
                    gameDTO.setMoves(moves);
                }
            } else if (line.isEmpty()){
                blankLines++;

                if (blankLines == 2) {
                    games.add(gameDTO);
                    gameDTO = new GameDTO();
                    blankLines = 0;
                }
            }
        }
        return games;
    }

    private String createValue(String tagName, String value) {
        return "[" + tagName + " \"" + value + "\"]";
    }

    private List<String> createMoves(String moves) {
        LinkedList<String> movesInLines = new LinkedList<>();
        int startIndex = 0;
        int endIndex = 79;
        String line, leftMoves;
        int count = 0;

        while(count != 2) {
            leftMoves = moves.substring(endIndex);
            int firstPeriodIndex = leftMoves.indexOf('.');

            if (firstPeriodIndex == 1 || firstPeriodIndex == 2 || (firstPeriodIndex == 3 && leftMoves.charAt(0) == ' ')) {
                line = moves.substring(startIndex, endIndex);
                movesInLines.add(line.trim());
                startIndex += line.length();
                endIndex += line.length();
            } else {
                int lastPeriodIndex = moves.substring(startIndex, endIndex).lastIndexOf('.');
                if (endIndex == moves.length())
                    line = moves.substring(startIndex, endIndex);
                else
                    line = moves.substring(startIndex, startIndex + lastPeriodIndex - 2);
                movesInLines.add(line.trim());
                startIndex += line.length();
                endIndex += line.length();
            }
            movesInLines.add("\n");
            if (endIndex > moves.length()) {
                endIndex = moves.length();
                count++;
            }
        }

        return movesInLines;
    }

    private String getValue(String line) {
        String value = line.substring(line.indexOf("\""), line.lastIndexOf("\""));
        return value.replaceAll("[\"]", "");
    }


    private int valueToElo(String line) {
        return line.length() == 0 ? -1 : Integer.parseInt(line);
    }


    public static void main(String[] args) {
        File file = new File("/home/michal/chess_manager_et/example.pgn");
        PGNParser pgnParser = new PGNParser();

        try {
            InputStream inputStream = new FileInputStream(file);
            List<GameDTO> games = pgnParser.fileToGame(inputStream);
            for (GameDTO g : games)
                System.out.println(g);

            List<String> lines = pgnParser.gameToFile(games.get(1));
            for (String l : lines)
                System.out.println(l);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
