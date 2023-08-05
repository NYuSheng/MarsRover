package jpm.nys.marsrover.model;

import jpm.nys.marsrover.exception.InvalidCommandRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static jpm.nys.marsrover.model.Direction.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RoverTest {

    private static Stream<Arguments> inputsForMovingForward() {
        return Stream.of(
            Arguments.of(new Rover("R1", new Coordinate(N, 0, 0)), new Coordinate(N, 0, 1)),
            Arguments.of(new Rover("R1", new Coordinate(S, 0, 0)), new Coordinate(S, 0, -1)),
            Arguments.of(new Rover("R1", new Coordinate(E, 0, 0)), new Coordinate(E, 1, 0)),
            Arguments.of(new Rover("R1", new Coordinate(W, 0, 0)), new Coordinate(W, -1, 0))
        );
    }

    private static Stream<Arguments> inputsForMovingBackward() {
        return Stream.of(
            Arguments.of(new Rover("R1", new Coordinate(N, 0, 0)), new Coordinate(N, 0, -1)),
            Arguments.of(new Rover("R1", new Coordinate(S, 0, 0)), new Coordinate(S, 0, 1)),
            Arguments.of(new Rover("R1", new Coordinate(E, 0, 0)), new Coordinate(E, -1, 0)),
            Arguments.of(new Rover("R1", new Coordinate(W, 0, 0)), new Coordinate(W, 1, 0))
        );
    }

    private static Stream<Arguments> inputsForRotatingClockwise() {
        return Stream.of(
            Arguments.of(new Rover("R1", new Coordinate(N, 0, 0)), new Coordinate(E, 0, 0)),
            Arguments.of(new Rover("R1", new Coordinate(S, 0, 0)), new Coordinate(W, 0, 0)),
            Arguments.of(new Rover("R1", new Coordinate(E, 0, 0)), new Coordinate(S, 0, 0)),
            Arguments.of(new Rover("R1", new Coordinate(W, 0, 0)), new Coordinate(N, 0, 0))
        );
    }

    private static Stream<Arguments> inputsForRotatingAntiClockwise() {
        return Stream.of(
            Arguments.of(new Rover("R1", new Coordinate(N, 0, 0)), new Coordinate(W, 0, 0)),
            Arguments.of(new Rover("R1", new Coordinate(S, 0, 0)), new Coordinate(E, 0, 0)),
            Arguments.of(new Rover("R1", new Coordinate(E, 0, 0)), new Coordinate(N, 0, 0)),
            Arguments.of(new Rover("R1", new Coordinate(W, 0, 0)), new Coordinate(S, 0, 0))
        );
    }

    @Test
    @DisplayName("Rover recognises command ignoring case")
    public void roverRecogniseCommandIgnoreCase() throws Exception {
        Coordinate expectedCoordinates = new Coordinate(N, 0, 1);

        Rover subject = new Rover("R1", new Coordinate(N, 0, 0));
        subject.move("F");

        assertThat(subject.getCoordinate()).isEqualToComparingFieldByFieldRecursively(expectedCoordinates);
    }

    @Test
    @DisplayName("Rover move throws InvalidCommandRequestException at unrecognized command")
    public void roverMoveThrowInvalidCommandRequestExceptionForUnrecognizedCommand() {
        String expectedErrorMessage = "Unrecognized command. Please use only f, b, r and l.";

        Rover subject = new Rover("R1", new Coordinate(N, 0, 0));
        assertThatThrownBy(() -> subject.move("U"))
            .isInstanceOf(InvalidCommandRequestException.class)
            .hasMessage(expectedErrorMessage);
    }

    @Test
    @DisplayName("Rover get next step throws InvalidCommandRequestException at unrecognized command")
    public void roverGetNextStepThrowInvalidCommandRequestExceptionForUnrecognizedCommand() {
        String expectedErrorMessage = "Unrecognized command. Please use only f, b, r and l.";

        Rover subject = new Rover("R1", new Coordinate(N, 0, 0));
        assertThatThrownBy(() -> subject.getNextStep("U"))
            .isInstanceOf(InvalidCommandRequestException.class)
            .hasMessage(expectedErrorMessage);
    }

    @Test
    @DisplayName("Rover get next step returns null for rotating")
    public void roverGetNextStepReturnsNullForRotating() throws Exception {
        Rover subject = new Rover("R1", new Coordinate(N, 0, 0));
        Coordinate actual = subject.getNextStep("r");
        assertThat(actual).isNull();
    }

    @ParameterizedTest
    @MethodSource("inputsForMovingForward")
    public void roverAbleToSeeNextStepForward(Rover subject, Coordinate expectedCoordinates) throws Exception {
        Coordinate actual = subject.getNextStep("f");
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expectedCoordinates);
    }

    @ParameterizedTest
    @MethodSource("inputsForMovingBackward")
    public void roverAbleToSeeNextStepBackward(Rover subject, Coordinate expectedCoordinates) throws Exception {
        Coordinate actual = subject.getNextStep("b");
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expectedCoordinates);
    }

    @ParameterizedTest
    @MethodSource("inputsForMovingForward")
    public void roverAbleToMoveForward(Rover subject, Coordinate expectedCoordinates) throws Exception {
        subject.move("f");
        assertThat(subject.getCoordinate()).isEqualToComparingFieldByFieldRecursively(expectedCoordinates);
    }

    @ParameterizedTest
    @MethodSource("inputsForMovingBackward")
    public void roverAbleToMoveBackward(Rover subject, Coordinate expectedCoordinates) throws Exception {
        subject.move("b");
        assertThat(subject.getCoordinate()).isEqualToComparingFieldByFieldRecursively(expectedCoordinates);
    }

    @ParameterizedTest
    @MethodSource("inputsForRotatingClockwise")
    public void roverAbleToRotateClockwise(Rover subject, Coordinate expectedCoordinates) throws Exception {
        subject.move("r");
        assertThat(subject.getCoordinate()).isEqualToComparingFieldByFieldRecursively(expectedCoordinates);
    }

    @ParameterizedTest
    @MethodSource("inputsForRotatingAntiClockwise")
    public void roverAbleToRotateAntiClockwise(Rover subject, Coordinate expectedCoordinates) throws Exception {
        subject.move("l");
        assertThat(subject.getCoordinate()).isEqualToComparingFieldByFieldRecursively(expectedCoordinates);
    }

}
