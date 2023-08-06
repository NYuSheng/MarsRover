package jpm.nys.marsrover.service;

import jpm.nys.marsrover.exception.InvalidCreateRequestException;
import jpm.nys.marsrover.exception.RoverExistsException;
import jpm.nys.marsrover.exception.RoverNotFoundException;
import jpm.nys.marsrover.model.Coordinate;
import jpm.nys.marsrover.model.Direction;
import jpm.nys.marsrover.model.Rover;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RoverServiceTest {

    private RoverService subject;

    @BeforeEach
    public void setup() {
        subject = new RoverService();
    }

    @Test
    @DisplayName("Get Rovers returns all rovers maintained in list")
    public void getRoversReturnsAllRovers() throws Exception {
        String request1 = "3,4,N";
        subject.create(request1);

        String request2 = "0,4,N";
        subject.create(request2);

        List<Rover> actual = subject.getRovers();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Creates a rover with the parameters given in request")
    public void createReturnsRoverByRequest() throws Exception {
        Rover expected = new Rover("R1", new Coordinate(Direction.N, 3, 4));

        String request = "3,4,N";
        Rover actual = subject.create(request);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    @DisplayName("Creates a rover with the parameters given in request even if more parameters are included")
    public void createReturnsRoverByLongRequest() throws Exception {
        Rover expected = new Rover("R1", new Coordinate(Direction.N, 3, 4));

        String request = "3,4,N,4,S,5";
        Rover actual = subject.create(request);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    @DisplayName("Create throws InvalidCreateRequestException with request of length shorter than 3")
    public void createThrowsInvalidCreateRequestExceptionWithIncorrectLengthRequest() {
        String expectedErrorMessage = "Error creating rover. Format {CoordinateX [Number], CoordinateY [Number], Direction [N|S|E|W]}.";

        String request = "3,4";

        assertThatThrownBy(() -> {
            subject.create(request);
        }).isInstanceOf(InvalidCreateRequestException.class)
            .hasMessage(expectedErrorMessage);
    }

    @Test
    @DisplayName("Create throws InvalidCreateRequestException with unparsable x or y coordinates")
    public void createThrowsInvalidCreateRequestExceptionWithUnparsableCoordinates() {
        String expectedErrorMessage = "Error creating rover. Format {CoordinateX [Number], CoordinateY [Number], Direction [N|S|E|W]}.";

        String request = "S,D,N";

        assertThatThrownBy(() -> {
            subject.create(request);
        }).isInstanceOf(InvalidCreateRequestException.class)
            .hasMessage(expectedErrorMessage);
    }

    @Test
    @DisplayName("Create throws InvalidCreateRequestException with unparsable direction")
    public void createThrowsInvalidCreateRequestExceptionWithUnparsableDirection() {
        String expectedErrorMessage = "Error creating rover. Format {CoordinateX [Number], CoordinateY [Number], Direction [N|S|E|W]}.";

        String request = "3,4,T";

        assertThatThrownBy(() -> {
            subject.create(request);
        }).isInstanceOf(InvalidCreateRequestException.class)
            .hasMessage(expectedErrorMessage);
    }

    @Test
    @DisplayName("Create throws RoverExistsException if a rover exists on the given coordinates")
    public void createThrowsRoverExistsExceptionIfRoverExists() throws Exception {
        String expectedErrorMessage = "Rover already exists on Coordinates {3, 4}";

        String request = "3,4,N";
        subject.create(request);

        assertThatThrownBy(() -> {
            subject.create(request);
        }).isInstanceOf(RoverExistsException.class)
            .hasMessage(expectedErrorMessage);
    }

    @Test
    @DisplayName("Get returns rover by given id if available")
    public void getReturnsRoverById() throws Exception {
        String request = "3,4,N";
        Rover createdRover = subject.create(request);

        assertThat(createdRover.getId()).isEqualTo("R1");

        Rover actualRover = subject.get("R1");
        assertThat(actualRover).isEqualToComparingFieldByFieldRecursively(createdRover);
    }

    @Test
    @DisplayName("Get returns rover by given id if available ignoring case sensitivity")
    public void getReturnsRoverByIdIgnoreCase() throws Exception {
        String request = "3,4,N";
        Rover createdRover = subject.create(request);

        assertThat(createdRover.getId()).isEqualTo("R1");

        Rover actualRover = subject.get("r1");
        assertThat(actualRover).isEqualToComparingFieldByFieldRecursively(createdRover);
    }

    @Test
    @DisplayName("Get throws RoverNotFoundException if given id is not found")
    public void getThrowsRoverNotFoundExceptionIfIdNotFound() {
        String expectedErrorMessage = "Rover R1 does not exist.";

        assertThatThrownBy(() -> {
            subject.get("R1");
        }).isInstanceOf(RoverNotFoundException.class)
            .hasMessage(expectedErrorMessage);
    }

    @Test
    @DisplayName("Move throws RoverNotFoundException if given id is not found")
    public void moveThrowsRoverNotFoundExceptionIfIdNotFound() {
        String expectedErrorMessage = "Rover R1 does not exist.";

        assertThatThrownBy(() -> {
            subject.move("R1", "f,f,f");
        }).isInstanceOf(RoverNotFoundException.class)
            .hasMessage(expectedErrorMessage);
    }

    @Test
    @DisplayName("Move invokes move command on selected rover")
    public void moveInvokesRoverMoveForEachCommand() throws Exception {
        String request = "0,0,N";
        Rover createdRover = subject.create(request);

        Coordinate expectedCoordinates = new Coordinate(Direction.E, 2, 3);
        subject.move("R1", "f,f,r,f,f,r,b,l");

        assertThat(createdRover.getCoordinate()).isEqualToComparingFieldByFieldRecursively(expectedCoordinates);
    }

    @Test
    @DisplayName("Move invokes move command on selected rover and prevents collision")
    public void moveInvokesRoverMoveForEachCommandAndPreventsCollision() throws Exception {
        String staticRequest = "3,4,N";
        subject.create(staticRequest);

        String movingRequest = "0,0,N";
        Rover movingRover = subject.create(movingRequest);

        Coordinate expectedCoordinates = new Coordinate(Direction.E, 2, 4);
        subject.move(movingRover.getId(), "f,f,f,f,r,f,f,f,f");

        assertThat(movingRover.getCoordinate()).isEqualToComparingFieldByFieldRecursively(expectedCoordinates);
    }

}
