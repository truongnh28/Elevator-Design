package com.vng.model;

import com.vng.elevator.Direction;

public class ElevatorRecorder {
    private String elevatorID;
    private int currentFloor;
    private Direction direction;
    private int nextFloorStop;

    public ElevatorRecorder() {
    }

    public ElevatorRecorder(String elevatorID, int currentFloor, int nextFloorStop, Direction direction) {
        this.currentFloor = currentFloor;
        this.elevatorID = elevatorID;
        this.direction = direction;
        this.nextFloorStop = nextFloorStop;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public String getElevatorID() {
        return elevatorID;
    }

    public void setElevatorID(String elevatorID) {
        this.elevatorID = elevatorID;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getNextFloorStop() {
        return nextFloorStop;
    }

    public void setNextFloorStop(int nextFloorStop) {
        this.nextFloorStop = nextFloorStop;
    }

    @Override
    public String toString() {
        return "ElevatorRecorder{" +
                "elevatorID='" + elevatorID + '\'' +
                ", currentFloor=" + currentFloor +
                ", direction=" + direction +
                ", nextFloorStop=" + nextFloorStop +
                '}';
    }
}
