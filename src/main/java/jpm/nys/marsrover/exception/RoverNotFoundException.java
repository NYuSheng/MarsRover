package jpm.nys.marsrover.exception;

public class RoverNotFoundException extends Exception {

    public RoverNotFoundException(String roverId) {
        super("Rover " + roverId + " does not exist.");
    }

}
