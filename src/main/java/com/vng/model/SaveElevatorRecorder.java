package com.vng.model;

import java.util.ArrayList;
import java.util.List;

public class SaveElevatorRecorder {
    private static SaveElevatorRecorder saveEvent = null;
    private List<ElevatorRecorder> elevatorRecorders = new ArrayList<>();

    public static SaveElevatorRecorder getSaveElevatorRecorder() {
        if(saveEvent == null) {
            saveEvent = new SaveElevatorRecorder();
        }
        return saveEvent;
    }
    public List<ElevatorRecorder> getElevatorRecorders() {
        return elevatorRecorders;
    }

    public void setElevatorRecorders(List<ElevatorRecorder> elevatorRecorders) {
        this.elevatorRecorders = elevatorRecorders;
    }

    public void addElevatorRecorder(ElevatorRecorder elevatorRecorder) {
        elevatorRecorders.add(elevatorRecorder);
    }

}
