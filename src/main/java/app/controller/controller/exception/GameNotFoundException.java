package app.controller.controller.exception;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(Long id) {
        super("Game with entered id (" + id.toString() + ") not found.");
    }
}
