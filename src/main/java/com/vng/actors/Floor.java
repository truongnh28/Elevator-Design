package com.vng.actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Floor {
    private static Floor floor = null;
    private final Map<Integer, CopyOnWriteArrayList<PersonRequest>> floorInfo = new HashMap<>();
//    private Map<Integer, Integer> numberOfPeopleOnEachFloor = new HashMap<>();

    private Floor() {

    }

    public static Floor getFloorInstance() {
        if(floor == null) {
            floor = new Floor();
        }
        return floor;
    }

    public void addPeopleOnFloor(int currentFloor, int countPeople, int targetFloor) {
        if(!floorInfo.containsKey(currentFloor)) {
            floorInfo.put(currentFloor, new CopyOnWriteArrayList<>());
        }
        for(int i = 0; i < countPeople; i++) {
            floorInfo.get(currentFloor).add(new PersonRequest(currentFloor, targetFloor));
        }
    }

    public synchronized List<PersonRequest> takePeopleFromFloor(int floor, int numberPeople) {
        if(!floorInfo.containsKey(floor)) {
            return new ArrayList<PersonRequest>();
        }
        List<PersonRequest> personRequests = new CopyOnWriteArrayList<>(
                floorInfo.get(floor).subList(0, Math.min(numberPeople, floorInfo.get(floor).size())));
        for(PersonRequest p : personRequests) {
            floorInfo.get(floor).remove(p);
        }
        return personRequests;
    }

    public int getNumberPeopleWaitingOnFloor(int floor) {
        return floorInfo.get(floor) != null ? floorInfo.get(floor).size() : 0;
    }

    public Map<Integer, CopyOnWriteArrayList<PersonRequest>> getFloorInfo() {
        return floorInfo;
    }
}
