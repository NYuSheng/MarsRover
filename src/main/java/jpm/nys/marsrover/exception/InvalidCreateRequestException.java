package jpm.nys.marsrover.exception;

public class InvalidCreateRequestException extends Exception {

    public InvalidCreateRequestException() {
        super("Error creating rover. Format {CoordinateX [Number], CoordinateY [Number], Direction [N|S|E|W]}.");
    }

}
