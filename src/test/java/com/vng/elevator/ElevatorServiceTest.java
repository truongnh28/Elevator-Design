package com.vng.elevator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorServiceTest {

    @Test
    public void testElevatorSelection() {
        Elevator e1 = createElevator("E1", 7, Direction.DOWN);
        Elevator e2 = createElevator("E2", 5, Direction.UP);
        Elevator e3 = createElevator("E3", 3, Direction.DOWN);
        Elevator e4 = createElevator("E4", 1, Direction.UP);

        Map<Integer, Elevator> elevators = new HashMap<>();
        elevators.put(1, e1);
        elevators.put(2, e2);
        elevators.put(3, e3);
        elevators.put(4, e4);
        ElevatorService controller = new ElevatorService();
        controller.setNumberFloor(10);
        controller.setNumberCabin(4);
        controller.setElevatorList(elevators);
        assertEquals("E2", controller.selectElevator(5).getElevatorID());
        assertEquals("E3", controller.selectElevator(3).getElevatorID());
        assertEquals("E1", controller.selectElevator(7).getElevatorID());
        assertEquals("E2", controller.selectElevator(10).getElevatorID());
    }

    private Elevator createElevator(String elevatorId, int currentLevel, Direction direction) {
        Elevator e = new Elevator(elevatorId, 10);
        e.setCurrentFloor(currentLevel);
        e.setDirection(direction);
        return e;
    }
}