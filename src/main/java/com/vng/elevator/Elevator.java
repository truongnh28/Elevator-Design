package com.vng.elevator;

import com.vng.actors.Floor;
import com.vng.actors.PersonRequest;
import com.vng.model.ElevatorRecorder;
import com.vng.model.SaveElevatorRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Elevator implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(Elevator.class);
    private int numberFloor;
    private boolean running = true;
    private int currentFloor;
    private String elevatorID;
    private Direction direction;
    private int nextFloorStop = -1;
    List<PersonRequest> personRequests = new ArrayList<>();
    private TreeSet<Integer> nextFloorStopSet;
    private final ElevatorRequestHandler elevatorRequestHandler;
    private final Thread elevatorHandlerRequestThread;
    private static final int MAX_CAPACITY = 20;

    public Elevator(String elevatorID, int numberFloor) {
        this.numberFloor = numberFloor;
        this.currentFloor = 1;
        this.direction = Direction.STATIONARY;
        this.elevatorID = elevatorID;
        this.nextFloorStopSet = new TreeSet<>();
        elevatorRequestHandler = new ElevatorRequestHandler(this);
        elevatorHandlerRequestThread = new Thread(elevatorRequestHandler);
        elevatorHandlerRequestThread.start();
    }

    public void addRequest(int floor) {
        nextFloorStopSet.add(floor);
    }

    public Integer findNearestFloor(SortedSet<Integer> floorsHaveRequest, int currentFloor) {
        SortedSet<Integer> floorsHaveRequestUp = floorsHaveRequest.headSet(currentFloor);
        SortedSet<Integer> floorsHaveRequestDown = floorsHaveRequest.tailSet(currentFloor);
        if (floorsHaveRequestDown.isEmpty() && floorsHaveRequestUp.isEmpty()) {
            return currentFloor;
        }
        if (floorsHaveRequestUp.isEmpty()) {
            return floorsHaveRequestDown.last();
        }
        if (floorsHaveRequestDown.isEmpty()) {
            return floorsHaveRequestUp.first();
        }
        return Math.min(currentFloor - floorsHaveRequestDown.last(),
                floorsHaveRequestUp.first() - currentFloor);
    }

    public Direction findDirection(int currentFloor, int targetFloor) {
        if (currentFloor == targetFloor) {
            return Direction.STATIONARY;
        }
        if (currentFloor > targetFloor) {
            return Direction.DOWN;
        }
        return Direction.UP;
    }

    public synchronized Integer checkNextStop(int currentFloor, Direction currentDirection) {
        SortedSet<Integer> floorStopList;
        Integer nextFloorStop = -1;
        switch (currentDirection) {
            case UP: {
                floorStopList = nextFloorStopSet.tailSet(currentFloor);
                if (floorStopList.isEmpty()) {
                    nextFloorStop = nextFloorStopSet.first();
                } else {
                    nextFloorStop = floorStopList.first();
                }
                break;
            }
            case DOWN: {
                floorStopList = nextFloorStopSet.headSet(currentFloor);
                if (floorStopList.isEmpty()) {
                    nextFloorStop = nextFloorStopSet.last();
                } else {
                    nextFloorStop = floorStopList.last();
                }
                break;
            }
            default: {
                nextFloorStop = findNearestFloor(nextFloorStopSet, currentFloor);
            }
        }
        return nextFloorStop;
    }

    public void movingNextFloor() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void removeVisitedFloorStop() {
        this.nextFloorStopSet.remove(this.currentFloor);
        if (this.nextFloorStopSet.isEmpty()) {
            this.direction = Direction.STATIONARY;
            this.nextFloorStop = -1;
        }
    }

    public void waitRequest() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void removePeoplesCompleteRequest() {
        if(getPersonRequests().isEmpty()) {
            return;
        }
        getPersonRequests().removeIf(p -> p.getTargetFloor() == currentFloor);
    }

    public synchronized void loadPeople() {
        List<PersonRequest> personRequestsFromFloor = Floor.getFloorInstance()
                .takePeopleFromFloor(this.currentFloor, MAX_CAPACITY - personRequests.size());
        this.getPersonRequests().addAll(personRequestsFromFloor);
        for(PersonRequest p:personRequestsFromFloor) {
            if(!this.getNextFloorStopSet().contains(p.getTargetFloor())) {
                addRequest(p.getTargetFloor());
            }
        }
    }

    public void processRequest() {
        if (this.nextFloorStopSet.isEmpty()) {
            waitRequest();
            return;
        }
        do {
            this.nextFloorStop = checkNextStop(this.currentFloor, this.direction);
            this.direction = findDirection(this.currentFloor, this.nextFloorStop);
            movingNextFloor();
            ElevatorRecorder elevatorRecorder =
                    new ElevatorRecorder(this.elevatorID, this.currentFloor, this.nextFloorStop, this.direction);
            LOG.info(elevatorRecorder.toString());
            SaveElevatorRecorder.getSaveElevatorRecorder().addElevatorRecorder(elevatorRecorder);
            this.currentFloor += direction.step;
        } while (this.getNextFloorStop() != this.currentFloor);
        if(this.nextFloorStop == this.currentFloor) {
            ElevatorRecorder elevatorRecorder =
                    new ElevatorRecorder(this.elevatorID, this.currentFloor, this.nextFloorStop, Direction.STATIONARY);
            LOG.info(elevatorRecorder.toString());
            SaveElevatorRecorder.getSaveElevatorRecorder().addElevatorRecorder(elevatorRecorder);
        }
        removePeoplesCompleteRequest();
        loadPeople();
        removeVisitedFloorStop();
    }

    @Override
    public void run() {
        while (isRunning()) {
            processRequest();
        }
        elevatorRequestHandler.setRunning(false);
    }

    public int getNumberFloor() {
        return numberFloor;
    }

    public void setNumberFloor(int numberFloor) {
        this.numberFloor = numberFloor;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
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

    public TreeSet<Integer> getNextFloorStopSet() {
        return nextFloorStopSet;
    }

    public void setNextFloorStopSet(TreeSet<Integer> nextFloorStopSet) {
        this.nextFloorStopSet = nextFloorStopSet;
    }

    public List<PersonRequest> getPersonRequests() {
        return personRequests;
    }

    public void setPersonRequests(List<PersonRequest> personRequests) {
        this.personRequests = personRequests;
    }

    @Override
    public String toString() {
        return "Elevator{" +
                "numberFloor=" + numberFloor +
                ", currentFloor=" + currentFloor +
                ", elevatorID='" + elevatorID + '\'' +
                ", direction=" + direction +
                '}';
    }
}
