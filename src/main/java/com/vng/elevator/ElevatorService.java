package com.vng.elevator;

import com.vng.model.ElevatorRecorder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Component
public class ElevatorService {
    private static ElevatorService elevatorController = null;
    private Map<Integer, Elevator> elevatorList;
    private List<Thread> elevatorThread = new ArrayList<>();
    private int numberFloor;
    private int numberCabin;

//    private ElevatorService(int numberFloor, int numberCabin) {
//        this.numberFloor = numberFloor;
//        this.numberCabin = numberCabin;
//    }
//
//    public static ElevatorService getElevatorController(int numberFloor, int numberCabin) {
//        if (elevatorController == null) {
//            elevatorController = new ElevatorService(numberFloor, numberCabin);
//        }
//        return elevatorController;
//    }
    public ElevatorService() {

    }

    public void startElevators() {
        for (Map.Entry<Integer, Elevator> entryElevator : elevatorList.entrySet()) {
            Thread thread = new Thread(entryElevator.getValue());
            elevatorThread.add(thread);
            thread.start();
        }
    }

    public Elevator selectElevator(int requestFloor) {
        int minScore = 100;
        Elevator selectedElevator = null;
        for (Map.Entry<Integer, Elevator> elevator : elevatorList.entrySet()) {
            int elevationScore;
            if (elevator.getValue().getDirection().equals(Direction.DOWN)) {
                elevationScore = elevator.getValue().getCurrentFloor() - requestFloor;
            } else if (elevator.getValue().getDirection().equals(Direction.UP)) {
                elevationScore = requestFloor - elevator.getValue().getCurrentFloor();
            } else {
                elevationScore = Math.abs(requestFloor - elevator.getValue().getCurrentFloor());
            }

            if (elevationScore < 0) {
                elevationScore = -elevationScore * 2;
            }

            if (minScore > elevationScore) {
                minScore = elevationScore;
                selectedElevator = elevator.getValue();
            }
        }
        return selectedElevator;
    }

    public void registerRequest(int floor) {
        selectElevator(floor).addRequest(floor);
    }

    public void shutDownElevators() {
        for (Map.Entry<Integer, Elevator> elevator : elevatorList.entrySet()) {
            elevator.getValue().setRunning(false);
        }
    }

    public int getNumberFloor() {
        return numberFloor;
    }

    public void setNumberFloor(int numberFloor) {
        this.numberFloor = numberFloor;
    }

    public int getNumberCabin() {
        return numberCabin;
    }

    public void setNumberCabin(int numberCabin) {
        this.numberCabin = numberCabin;
    }

    public Map<Integer, Elevator> getElevatorList() {
        return elevatorList;
    }

    public void setElevatorList(Map<Integer, Elevator> elevatorList) {
        this.elevatorList = elevatorList;
    }
}
