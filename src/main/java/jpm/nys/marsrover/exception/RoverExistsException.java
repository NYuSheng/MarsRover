package jpm.nys.marsrover.exception;

public class RoverExistsException extends Exception {

    public RoverExistsException(Integer x, Integer y) {
        super("Rover already exists on Coordinates {" + x + ", " + y + "}");
    }

}
