package jpm.nys.marsrover.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jpm.nys.marsrover.model.Coordinate;
import jpm.nys.marsrover.model.Direction;
import jpm.nys.marsrover.model.Rover;
import jpm.nys.marsrover.service.RoverService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(RoverController.class)
public class RoverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoverService roverService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
    @DisplayName("API is able to return response from rover service for current position")
    public void ableToGetCurrentPosition() throws Exception {
        Rover expected = new Rover("R1", new Coordinate(Direction.N, 3, 4));
        Mockito.when(roverService.get(Mockito.anyString())).thenReturn(expected);

        String endpoint = "/api/v1/currentPosition?roverId=3,4,N";
        MvcResult response = sendRequestWithGet(endpoint)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Rover actual = objectMapper.readValue(response.getResponse().getContentAsString(), Rover.class);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
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
