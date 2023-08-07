package jpm.nys.marsrover.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jpm.nys.marsrover.exception.InvalidCommandRequestException;
import jpm.nys.marsrover.exception.RoverExistsException;
import jpm.nys.marsrover.exception.RoverNotFoundException;
import jpm.nys.marsrover.model.ApiException;
import jpm.nys.marsrover.exception.InvalidCreateRequestException;
import jpm.nys.marsrover.model.Coordinate;
import jpm.nys.marsrover.model.Direction;
import jpm.nys.marsrover.model.Rover;
import jpm.nys.marsrover.service.RoverService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(RoverController.class)
public class RoverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoverService roverService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("API is able to return response from rover service for getAll")
    public void ableToGetAllRovers() throws Exception {
        List<Rover> expected = new ArrayList();
        expected.add(new Rover("R1", new Coordinate(Direction.N, 3, 4)));
        Mockito.when(roverService.getRovers()).thenReturn(expected);

        String endpoint = "/api/v1/allRovers";
        MvcResult response = sendRequestWithGet(endpoint)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        List<Rover> actual = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<Rover>>() {
        });
        assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("API is able to return response from rover service for create")
    public void ableToCreateRover() throws Exception {
        Rover expected = new Rover("R1", new Coordinate(Direction.N, 3, 4));
        Mockito.when(roverService.create(Mockito.anyString())).thenReturn(expected);

        String endpoint = "/api/v1/createRover?createRequest=3,4,N";
        MvcResult response = sendRequestWithPost(endpoint)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Rover actual = objectMapper.readValue(response.getResponse().getContentAsString(), Rover.class);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    @DisplayName("API is able to return error response from rover service for create for InvalidCreateRequestException")
    public void ableToThrowInvalidCreateRequestExceptionForCreateRover() throws Exception {
        Mockito.when(roverService.create(Mockito.anyString())).thenThrow(new InvalidCreateRequestException());

        String endpoint = "/api/v1/createRover?createRequest=3jD";
        MvcResult response = sendRequestWithPost(endpoint)
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

        ApiException exception = objectMapper.readValue(response.getResponse().getContentAsString(), ApiException.class);
        assertThat(exception.getMessage()).isEqualTo("Error creating rover. Format {CoordinateX [Number], CoordinateY [Number], Direction [N|S|E|W]}.");
    }

    @Test
    @DisplayName("API is able to return error response from rover service for create for RoverExistsException")
    public void ableToThrowRoverExistsExceptionForCreateRover() throws Exception {
        Mockito.when(roverService.create(Mockito.anyString())).thenThrow(new RoverExistsException(3,4));

        String endpoint = "/api/v1/createRover?createRequest=3,4,E";
        MvcResult response = sendRequestWithPost(endpoint)
            .andExpect(MockMvcResultMatchers.status().isConflict())
            .andReturn();

        ApiException exception = objectMapper.readValue(response.getResponse().getContentAsString(), ApiException.class);
        assertThat(exception.getMessage()).isEqualTo("Rover already exists on Coordinates {3, 4}");
    }

    @Test
    @DisplayName("API is able to return response from rover service for current position")
    public void ableToGetCurrentPosition() throws Exception {
        Rover expected = new Rover("R1", new Coordinate(Direction.N, 3, 4));
        Mockito.when(roverService.get(Mockito.anyString())).thenReturn(expected);

        String endpoint = "/api/v1/currentPosition?roverId=R1";
        MvcResult response = sendRequestWithGet(endpoint)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Rover actual = objectMapper.readValue(response.getResponse().getContentAsString(), Rover.class);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    @DisplayName("API is able to return error response from rover service for current position for RoverNotFoundException")
    public void ableToThrowRoverNotFoundExceptionForGetCurrentPosition() throws Exception {
        Mockito.when(roverService.get(Mockito.anyString())).thenThrow(new RoverNotFoundException("R1"));

        String endpoint = "/api/v1/currentPosition?roverId=R1";
        MvcResult response = sendRequestWithGet(endpoint)
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

        ApiException exception = objectMapper.readValue(response.getResponse().getContentAsString(), ApiException.class);
        assertThat(exception.getMessage()).isEqualTo("Rover R1 does not exist.");
    }

    @Test
    @DisplayName("API is able to return response from rover service for move")
    public void ableToMoveRover() throws Exception {
        Rover expected = new Rover("R1", new Coordinate(Direction.N, 3, 4));
        Mockito.when(roverService.move(Mockito.anyString(), Mockito.anyString())).thenReturn(expected);

        String endpoint = "/api/v1/moveRover?roverId=R1&moveCommands=f,f,f";
        MvcResult response = sendRequestWithPut(endpoint)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Rover actual = objectMapper.readValue(response.getResponse().getContentAsString(), Rover.class);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    @DisplayName("API is able to return error response from rover service for move for InvalidCommandException")
    public void ableToThrowInvalidCommandExceptionForMoveRover() throws Exception {
        Mockito.when(roverService.move(Mockito.anyString(), Mockito.anyString())).thenThrow(new InvalidCommandRequestException());

        String endpoint = "/api/v1/moveRover?roverId=R1&moveCommands=f,g,h";
        MvcResult response = sendRequestWithPut(endpoint)
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

        ApiException exception = objectMapper.readValue(response.getResponse().getContentAsString(), ApiException.class);
        assertThat(exception.getMessage()).isEqualTo("Unrecognized command. Please use only f, b, r and l.");
    }

    private ResultActions sendRequestWithGet(String endpoint) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(endpoint).contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions sendRequestWithPost(String endpoint) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(endpoint).contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions sendRequestWithPut(String endpoint) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(endpoint).contentType(MediaType.APPLICATION_JSON));
    }

}
