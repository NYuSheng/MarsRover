package jpm.nys.marsrover.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinate {

    private Direction direction;
    private Integer x;
    private Integer y;

    @JsonCreator
    public Coordinate(
        @JsonProperty("direction")
        Direction direction,
        @JsonProperty("x")
        Integer x,
        @JsonProperty("y")
        Integer y
    ) {
        this.direction = direction;
        this.x = x;
        this.y = y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

}
