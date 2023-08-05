package jpm.nys.marsrover.service;

import jpm.nys.marsrover.exception.InvalidCreateRequestException;
import jpm.nys.marsrover.exception.RoverExistsException;
import jpm.nys.marsrover.exception.RoverNotFoundException;
import jpm.nys.marsrover.model.Coordinate;
import jpm.nys.marsrover.model.Direction;
import jpm.nys.marsrover.model.Rover;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoverService {

    private List<Rover> rovers;

    public RoverService() {
        this.rovers = new ArrayList<>();
    }

    public RoverService(ArrayList<Rover> rovers) {
        this.rovers = rovers;
    }

    public Rover create(String request) throws Exception {
        Coordinate coordinates;
        try {
            String[] roverParams = request.split(",");

            Integer coordinateX = Integer.parseInt(roverParams[0]);
            Integer coordinateY = Integer.parseInt(roverParams[1]);
            Direction direction = Direction.valueOf(roverParams[2]);

            coordinates = new Coordinate(direction, coordinateX, coordinateY);
        } catch (Exception ex) {
            throw new InvalidCreateRequestException();
        }

        if (roverExistsOn(coordinates.getX(), coordinates.getY()))
            throw new RoverExistsException(coordinates.getX(), coordinates.getY());

        String roverId = "R" + (rovers.size() + 1);

        Rover rover = new Rover(roverId, coordinates);
        rovers.add(rover);
        return rover;
    }

    private boolean roverExistsOn(Integer x, Integer y) {
        for (Rover rover : rovers) {
            Coordinate roverCoordinates = rover.getCoordinate();
            if (roverCoordinates.getX() == x && roverCoordinates.getY() == y)
                return true;
        }
        return false;
    }

    public Rover get(String roverId) throws Exception {
        return rovers.stream()
            .filter(rover -> rover.getId().equalsIgnoreCase(roverId))
            .findFirst()
            .orElseThrow(() -> new RoverNotFoundException(roverId));
    }

    public Rover move(String roverId, String request) throws Exception {
        Rover rover = get(roverId);

        String[] commands = request.split(",");
        for (String command : commands) {
            Coordinate newCoordinates = rover.getNextStep(command);
            if (newCoordinates != null && roverExistsOn(newCoordinates.getX(), newCoordinates.getY())) break;
            rover.move(command);
        }

        return rover;
    }

}
