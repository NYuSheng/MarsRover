package jpm.nys.marsrover.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jpm.nys.marsrover.model.Rover;
import jpm.nys.marsrover.service.RoverService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Rover Operations")
public class RoverController {

    private final RoverService roverService;

    public RoverController(RoverService roverService) {
        this.roverService = roverService;
    }

    @GetMapping("/allRovers")
    @Operation(description = "Gets all rovers")
    public List<Rover> getAll() {
        return roverService.getRovers();
    }

    @GetMapping("/currentPosition")
    @Operation(description = "Gets the current position of the given rover")
    public Rover getCurrentPosition(
        @RequestParam
        @Parameter(name = "roverId", description = "Id of the requested rover")
        String roverId
    ) throws Exception {
        return roverService.get(roverId);
    }

    @PostMapping("/createRover")
    @Operation(description = "Creates a rover with the given coordinates and direction")
    public Rover create(
        @RequestParam
        @Parameter(
            name = "createRequest",
            description = "Coordinates and direction of the rover to be created. Format: {CoordinateX [Number], CoordinateY [Number], Direction [N|S|E|W]}"
        )
        String createRequest
    ) throws Exception {
        return roverService.create(createRequest);
    }

    @PutMapping("/moveRover")
    @Operation(description = "Moves a rover with the given commands")
    public Rover move(
        @RequestParam
        @Parameter(name = "roverId", description = "Id of the requested rover")
        String roverId,
        @RequestParam
        @Parameter(name = "moveCommands", description = "Commands for the rover. Format: {Comma separated with only f, b, r and l values accepted}")
        String moveCommands
    ) throws Exception {
        return roverService.move(roverId, moveCommands);
    }

}
