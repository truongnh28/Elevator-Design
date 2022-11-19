package com.vng.model;

import com.vng.elevator.Direction;

public class ElevatorModel {
    private String elevatorID;
    private int numberFloor;
    private int currentFloor;
    private Direction direction;

    public ElevatorModel(String elevatorID, int numberFloor, int currentFloor, Direction direction) {
        this.elevatorID = elevatorID;
        this.numberFloor = numberFloor;
        this.currentFloor = currentFloor;
        this.direction = direction;
    }

    public String getElevatorID() {
        return elevatorID;
    }

    public void setElevatorID(String elevatorID) {
        this.elevatorID = elevatorID;
    }

    public int getNumberFloor() {
        return numberFloor;
    }

    public void setNumberFloor(int numberFloor) {
        this.numberFloor = numberFloor;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
