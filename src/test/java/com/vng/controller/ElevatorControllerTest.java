package com.vng.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vng.actors.Floor;
import com.vng.elevator.Direction;
import com.vng.elevator.Elevator;
import com.vng.elevator.ElevatorService;
import com.vng.model.ElevatorRecorder;
import com.vng.model.SaveElevatorRecorder;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;
import javax.swing.*;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ElevatorController.class)
class ElevatorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ElevatorService service;
    @Mock
    Elevator elevator1;
    @Mock
    Elevator elevator2;
    @Mock
    SaveElevatorRecorder saveElevatorRecorder;
    @Test
    public void elevatorStatusTest() throws Exception {
        Mockito.when(elevator1.getElevatorID()).thenReturn("E1");
        Mockito.when(elevator1.getCurrentFloor()).thenReturn(1);
        Mockito.when(elevator1.getNumberFloor()).thenReturn(10);
        Mockito.when(elevator1.getDirection()).thenReturn(Direction.STATIONARY);
        Mockito.when(elevator1.getNextFloorStop()).thenReturn(10);
        Mockito.when(elevator2.getElevatorID()).thenReturn("E2");
        Mockito.when(elevator2.getCurrentFloor()).thenReturn(1);
        Mockito.when(elevator2.getNumberFloor()).thenReturn(10);
        Mockito.when(elevator2.getDirection()).thenReturn(Direction.STATIONARY);
        Mockito.when(elevator2.getNextFloorStop()).thenReturn(10);
        given(service.getNumberFloor()).willReturn(10);
        given(service.getNumberCabin()).willReturn(2);
        given(service.selectElevator(5)).willReturn(new Elevator("E1", 10));
        given(service.getElevatorList()).willReturn(
                new HashMap<>() {{
                    put(1, elevator1);
                    put(2, elevator2);
                }});
//        mockMvc.perform(get("/api/elevatorStatus").contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isFound())
//                .andExpect(jsonPath("status", is("FOUND")))
//                .andExpect(jsonPath("message", is("Failed request when server is null.")))
//                .andExpect(jsonPath("data", is("")));
        mockMvc.perform(get("/api/elevatorStatus").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data", hasSize(2)))
                .andExpect(jsonPath("data[0].elevatorID", is("E1")))
                .andExpect(jsonPath("data[0].numberFloor", is(10)))
                .andExpect(jsonPath("data[0].currentFloor", is(1)))
                .andExpect(jsonPath("data[0].direction", is("STATIONARY")))
                .andExpect(jsonPath("data[1].elevatorID", is("E2")))
                .andExpect(jsonPath("data[1].numberFloor", is(10)))
                .andExpect(jsonPath("data[1].currentFloor", is(1)))
                .andExpect(jsonPath("data[1].direction", is("STATIONARY")));
    }
    @Test
    public void elevatorSystemEvent() throws Exception {
        Mockito.when(elevator1.getElevatorID()).thenReturn("E1");
        Mockito.when(elevator1.getCurrentFloor()).thenReturn(1);
        Mockito.when(elevator1.getNumberFloor()).thenReturn(10);
        Mockito.when(elevator1.getDirection()).thenReturn(Direction.STATIONARY);
        Mockito.when(elevator1.getNextFloorStop()).thenReturn(10);
        Mockito.when(elevator2.getElevatorID()).thenReturn("E2");
        Mockito.when(elevator2.getCurrentFloor()).thenReturn(1);
        Mockito.when(elevator2.getNumberFloor()).thenReturn(10);
        Mockito.when(elevator2.getDirection()).thenReturn(Direction.STATIONARY);
        Mockito.when(elevator2.getNextFloorStop()).thenReturn(10);
        given(service.getNumberFloor()).willReturn(10);
        given(service.getNumberCabin()).willReturn(2);
        given(service.selectElevator(5)).willReturn(elevator1);
        given(service.getElevatorList()).willReturn(
                new HashMap<>() {{
                    put(1, elevator1);
                    put(2, elevator2);
                }});

        mockMvc.perform(get("/api/elevatorSystemEvent").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", is("Successful request.")))
                .andExpect(jsonPath("data", hasSize(36)));
    }
    @Test
    public void addPeopleOnFloorTestBadRequest() throws Exception {
        Mockito.when(elevator1.getElevatorID()).thenReturn("E1");
        Mockito.when(elevator1.getCurrentFloor()).thenReturn(1);
        Mockito.when(elevator1.getNumberFloor()).thenReturn(10);
        Mockito.when(elevator1.getDirection()).thenReturn(Direction.STATIONARY);
        Mockito.when(elevator1.getNextFloorStop()).thenReturn(10);
        Mockito.when(elevator2.getElevatorID()).thenReturn("E2");
        Mockito.when(elevator2.getCurrentFloor()).thenReturn(1);
        Mockito.when(elevator2.getNumberFloor()).thenReturn(10);
        Mockito.when(elevator2.getDirection()).thenReturn(Direction.STATIONARY);
        Mockito.when(elevator2.getNextFloorStop()).thenReturn(10);
        given(service.getNumberFloor()).willReturn(10);
        given(service.getNumberCabin()).willReturn(2);
        given(service.selectElevator(5)).willReturn(elevator1);
        given(service.getElevatorList()).willReturn(
                new HashMap<>() {{
                    put(1, elevator1);
                    put(2, elevator2);
                }});
        mockMvc.perform(post("/api/addPeopleOnFloor?currentFloor=0&countPeople=3&targetFloor=6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data", is("")));
    }
    @Test
    public void addPeopleOnFloorTestOk() throws Exception {
        Mockito.when(elevator1.getElevatorID()).thenReturn("E1");
        Mockito.when(elevator1.getCurrentFloor()).thenReturn(1);
        Mockito.when(elevator1.getNumberFloor()).thenReturn(10);
        Mockito.when(elevator1.getDirection()).thenReturn(Direction.STATIONARY);
        Mockito.when(elevator1.getNextFloorStop()).thenReturn(10);
        Mockito.when(elevator2.getElevatorID()).thenReturn("E2");
        Mockito.when(elevator2.getCurrentFloor()).thenReturn(1);
        Mockito.when(elevator2.getNumberFloor()).thenReturn(10);
        Mockito.when(elevator2.getDirection()).thenReturn(Direction.STATIONARY);
        Mockito.when(elevator2.getNextFloorStop()).thenReturn(10);
        given(service.getNumberFloor()).willReturn(10);
        given(service.getNumberCabin()).willReturn(2);
        given(service.selectElevator(5)).willReturn(elevator1);
        given(service.getElevatorList()).willReturn(
                new HashMap<>() {{
                    put(1, elevator1);
                    put(2, elevator2);
                }});
        mockMvc.perform(post("/api/addPeopleOnFloor?currentFloor=2&countPeople=3&targetFloor=6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data", is(3)));
    }
    @Test
    public void elevatorEventTest() throws Exception {
        Mockito.when(elevator1.getElevatorID()).thenReturn("E1");
        Mockito.when(elevator1.getCurrentFloor()).thenReturn(1);
        Mockito.when(elevator1.getNumberFloor()).thenReturn(10);
        Mockito.when(elevator1.getDirection()).thenReturn(Direction.STATIONARY);
        Mockito.when(elevator1.getNextFloorStop()).thenReturn(10);
        Mockito.when(elevator2.getElevatorID()).thenReturn("E2");
        Mockito.when(elevator2.getCurrentFloor()).thenReturn(1);
        Mockito.when(elevator2.getNumberFloor()).thenReturn(10);
        Mockito.when(elevator2.getDirection()).thenReturn(Direction.STATIONARY);
        Mockito.when(elevator2.getNextFloorStop()).thenReturn(10);
        given(service.getNumberFloor()).willReturn(10);
        given(service.getNumberCabin()).willReturn(2);
        given(service.selectElevator(5)).willReturn(elevator1);
        given(service.getElevatorList()).willReturn(
                new HashMap<>() {{
                    put(1, elevator1);
                    put(2, elevator2);
                }});
        mockMvc.perform(get("/api/elevatorEvent?elevatorID=E1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", is("Successful request.")))
                .andExpect(jsonPath("data", hasSize(36)));
    }
    @Test
    public void requestElevatorTestOk() throws Exception {
        Mockito.when(elevator1.getElevatorID()).thenReturn("E1");
        Mockito.when(elevator1.getCurrentFloor()).thenReturn(1);
        Mockito.when(elevator1.getNumberFloor()).thenReturn(10);
        Mockito.when(elevator1.getDirection()).thenReturn(Direction.STATIONARY);
        Mockito.when(elevator1.getNextFloorStop()).thenReturn(10);
        Mockito.when(elevator2.getElevatorID()).thenReturn("E2");
        Mockito.when(elevator2.getCurrentFloor()).thenReturn(1);
        Mockito.when(elevator2.getNumberFloor()).thenReturn(10);
        Mockito.when(elevator2.getDirection()).thenReturn(Direction.STATIONARY);
        Mockito.when(elevator2.getNextFloorStop()).thenReturn(10);
        given(service.getNumberFloor()).willReturn(10);
        given(service.getNumberCabin()).willReturn(2);
        given(service.selectElevator(5)).willReturn(elevator1);
        given(service.getElevatorList()).willReturn(
                new HashMap<>() {{
                    put(1, elevator1);
                    put(2, elevator2);
                }});
        mockMvc.perform(get("/api/requestElevator?floor=2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", is("Successful request.")))
                .andExpect(jsonPath("data", is("")));
    }
    @Test
    public void requestElevatorTestBadRequest() throws Exception {
        Mockito.when(elevator1.getElevatorID()).thenReturn("E1");
        Mockito.when(elevator1.getCurrentFloor()).thenReturn(1);
        Mockito.when(elevator1.getNumberFloor()).thenReturn(10);
        Mockito.when(elevator1.getDirection()).thenReturn(Direction.STATIONARY);
        Mockito.when(elevator1.getNextFloorStop()).thenReturn(10);
        Mockito.when(elevator2.getElevatorID()).thenReturn("E2");
        Mockito.when(elevator2.getCurrentFloor()).thenReturn(1);
        Mockito.when(elevator2.getNumberFloor()).thenReturn(10);
        Mockito.when(elevator2.getDirection()).thenReturn(Direction.STATIONARY);
        Mockito.when(elevator2.getNextFloorStop()).thenReturn(10);
        given(service.getNumberFloor()).willReturn(10);
        given(service.getNumberCabin()).willReturn(2);
        given(service.selectElevator(5)).willReturn(elevator1);
        given(service.getElevatorList()).willReturn(
                new HashMap<>() {{
                    put(1, elevator1);
                    put(2, elevator2);
                }});
        mockMvc.perform(get("/api/requestElevator?floor=0").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", is("Bad request when parameter is not valid.")))
                .andExpect(jsonPath("data", is("")));
    }
}