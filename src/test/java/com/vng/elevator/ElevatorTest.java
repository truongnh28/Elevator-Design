package com.vng.elevator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {
    @Test
    public void testElevatorOrderHandlingGoingDown() {
        Elevator e = new Elevator("E1", 10);
        e.setRunning(true);
        e.setCurrentFloor(10);
        e.addRequest(1);
        runElevatorTest(e, 11);
        assertEquals(1, e.getCurrentFloor());
        assertEquals(-1, e.getNextFloorStop());
        assertTrue(e.getNextFloorStopSet().isEmpty());
        assertEquals(Direction.STATIONARY, e.getDirection());

    }

    private void runElevatorTest(Elevator e, int loopCount) {

        for (int i = 0; i < loopCount; i++) {
            e.processRequest();
            System.out.println(e);
        }
    }

    @Test
    public void testElevatorOrderHandlingGoingUp() throws InterruptedException {

        Elevator e = new Elevator("E1", 10);
        e.setRunning(true);
        for (int i = 10; i > 1; i--) {
            e.addRequest(i);
        }
        runElevatorTest(e, 10);
        assertEquals(10, e.getCurrentFloor());
        assertEquals(-1, e.getNextFloorStop());
        assertTrue(e.getNextFloorStopSet().isEmpty());
        assertEquals(Direction.STATIONARY, e.getDirection());
    }
}