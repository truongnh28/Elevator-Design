package com.vng.actors;

public class PersonRequest {
    private int currentFloor;
    private int targetFloor;

    public PersonRequest(int currentFloor, int targetFloor) {
        this.currentFloor = currentFloor;
        this.targetFloor = targetFloor;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }
}
