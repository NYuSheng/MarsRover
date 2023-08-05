package jpm.nys.marsrover.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jpm.nys.marsrover.exception.InvalidCommandRequestException;

public class Rover {

    private final String id;
    private Coordinate coordinate;

    @JsonCreator
    public Rover(
        @JsonProperty("id")
        String id,
        @JsonProperty("coordinate")
        Coordinate coordinate
    ) {
        this.id = id;
        this.coordinate = coordinate;
    }

    public String getId() {
        return id;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getNextStep(String command) throws Exception {
        return switch (command.toLowerCase()) {
            case "f" -> moveForward(false);
            case "b" -> moveBackward(false);
            case "r", "l" -> null;
            default -> throw new InvalidCommandRequestException();
        };
    }

    public void move(String command) throws Exception {
        switch (command.toLowerCase()) {
            case "f" -> moveForward(true);
            case "b" -> moveBackward(true);
            case "r" -> rotateClockwise();
            case "l" -> rotateAntiClockwise();
            default -> throw new InvalidCommandRequestException();
        }
    }

    private Coordinate moveForward(Boolean commitAction) {
        Direction currentDirection = coordinate.getDirection();
        Integer currentX = coordinate.getX();
        Integer currentY = coordinate.getY();

        Coordinate newCoordinates = switch (coordinate.getDirection()) {
            case N -> new Coordinate(currentDirection, currentX, currentY + 1);
            case E -> new Coordinate(currentDirection, currentX + 1, currentY);
            case S -> new Coordinate(currentDirection, currentX, currentY - 1);
            case W -> new Coordinate(currentDirection, currentX - 1, currentY);
        };

        if (commitAction)
            this.coordinate = newCoordinates;

        return newCoordinates;
    }

    private Coordinate moveBackward(Boolean commitAction) {
        Direction currentDirection = coordinate.getDirection();
        Integer currentX = coordinate.getX();
        Integer currentY = coordinate.getY();

        Coordinate newCoordinates = switch (coordinate.getDirection()) {
            case N -> new Coordinate(currentDirection, currentX, currentY - 1);
            case E -> new Coordinate(currentDirection, currentX - 1, currentY);
            case S -> new Coordinate(currentDirection, currentX, currentY + 1);
            case W -> new Coordinate(currentDirection, currentX + 1, currentY);
        };

        if (commitAction)
            this.coordinate = newCoordinates;

        return newCoordinates;
    }

    private void rotateClockwise() {
        switch (coordinate.getDirection()) {
            case N -> coordinate.setDirection(Direction.E);
            case E -> coordinate.setDirection(Direction.S);
            case S -> coordinate.setDirection(Direction.W);
            case W -> coordinate.setDirection(Direction.N);
        }
    }

    private void rotateAntiClockwise() {
        switch (coordinate.getDirection()) {
            case N -> coordinate.setDirection(Direction.W);
            case E -> coordinate.setDirection(Direction.N);
            case S -> coordinate.setDirection(Direction.E);
            case W -> coordinate.setDirection(Direction.S);
        }
    }

}
