package jpm.nys.marsrover.exception;

public class InvalidCommandRequestException extends Exception {

    public InvalidCommandRequestException() {
        super("Unrecognized command. Please use only f, b, r and l.");
    }

}
