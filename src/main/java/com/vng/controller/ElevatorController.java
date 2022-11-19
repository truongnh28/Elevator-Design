package com.vng.controller;

import com.vng.actors.Floor;
import com.vng.api.ResponseObject;
import com.vng.elevator.Elevator;
import com.vng.elevator.ElevatorService;
import com.vng.model.ElevatorModel;
import com.vng.model.ElevatorRecorder;
import com.vng.model.SaveElevatorRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ElevatorController {
    private final Logger logger = LoggerFactory.getLogger(ElevatorController.class);

    @Autowired
    private ElevatorService elevatorService;
    @Autowired
    private ApplicationContext context;

    @GetMapping("/createElevatorSystem")
    public ResponseEntity<ResponseObject> createElevatorSystem(
            @RequestParam(value = "numberFloor", defaultValue = "2") int numberFloor,
            @RequestParam(value = "numberCabin", defaultValue = "1") int numberCabin) {
        if(elevatorService != null) {
            logger.info("Failed request when server is exist");
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new ResponseObject("FOUND", "Failed request when server is exist", ""));
        }
        if(numberFloor < 1 || numberCabin < 1) {
            logger.info("Bad request when parameter is not valid.\n");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("BAD_REQUEST", "Bad request when parameter is not valid.", ""));
        }
        Map<Integer, Elevator> elevators = new HashMap<>();
        for (int i = 1; i <= numberCabin; i++) {
            elevators.put(i, new Elevator("E" + i, numberFloor));
        }
//        elevatorService = ElevatorService.getElevatorController(numberFloor, numberCabin);
        elevatorService = context.getBean(ElevatorService.class);
        elevatorService.setElevatorList(elevators);
        elevatorService.startElevators();
        logger.info("Create elevator system successfully");
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successful request.", ""));
    }

    @GetMapping("/requestElevator")
    public ResponseEntity<ResponseObject> requestElevator(
            @RequestParam(value = "floor", defaultValue = "1") int floor) {
        if(elevatorService == null) {
            logger.info("Failed request when server is null.");
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new ResponseObject("FOUND", "Failed request when server is null.", ""));
        }
        if(floor > elevatorService.getNumberFloor() || floor < 1) {
            logger.info("Bad request when parameter is not valid.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("BAD_REQUEST", "Bad request when parameter is not valid.", ""));
        }
        elevatorService.registerRequest(floor);
        logger.info("Successful request.");
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successful request.", ""));
    }

    @GetMapping("/elevatorSystemEvent")
    public ResponseEntity<ResponseObject> elevatorSystemEvent() {
        if(elevatorService == null) {
            logger.info("Failed request when server is null.");
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new ResponseObject("FOUND", "Failed request when server is null.", ""));
        }
        logger.info("Successful request.");
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successful request.",
                        SaveElevatorRecorder.getSaveElevatorRecorder().getElevatorRecorders()));
    }

    @GetMapping("/elevatorEvent")
    public ResponseEntity<ResponseObject> elevatorEvent(@RequestParam(value = "elevatorID") String elevatorID) {
        if(elevatorService == null) {
            logger.info("Bad request when parameter is not valid.");
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new ResponseObject("FOUND", "", "Failed request when server is null."));
        }
        List<ElevatorRecorder> elevatorEvents = SaveElevatorRecorder.getSaveElevatorRecorder()
                .getElevatorRecorders().stream()
                .filter(x -> x.getElevatorID().equals(elevatorID))
                .collect(Collectors.toList());
        if(elevatorEvents.size() == 0) {
            logger.info("Failed request when server is null.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("BAD_REQUEST", "Bad request when parameter is not valid.", "")
            );
        }
        logger.info("Successful request.");
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successful request.", elevatorEvents));
    }

    @PostMapping("addPeopleOnFloor")
    public ResponseEntity<ResponseObject> addPeopleOnFloor(
            @RequestParam(value = "currentFloor") int currentFloor,
            @RequestParam(value = "countPeople") int countPeople,
            @RequestParam(value = "targetFloor") int targetFloor) {

        if(elevatorService == null) {
            logger.info("Failed request when server is null.");
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new ResponseObject("FOUND", "Failed request when server is null.", ""));
        }
        if((currentFloor < 1 || currentFloor > elevatorService.getNumberFloor()) ||
                (targetFloor < 1 || targetFloor > elevatorService.getNumberFloor()) || countPeople < 0) {
            logger.info("Bad request when parameter is not valid.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("BAD_REQUEST", "Bad request when parameter is not valid.", "")
            );
        }
        Floor.getFloorInstance().addPeopleOnFloor(currentFloor, countPeople, targetFloor);
        logger.info("Successful request.");
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successful request.",
                        Floor.getFloorInstance().getNumberPeopleWaitingOnFloor(currentFloor)));
    }

    @GetMapping("elevatorStatus")
    public ResponseEntity<ResponseObject> elevatorStatus() {
        if(elevatorService == null) {
            logger.info("Failed request when server is null.");
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new ResponseObject("FOUND", "Failed request when server is null.", ""));
        }
        logger.info("Successful request.");
        List<ElevatorModel> elevators = new ArrayList<>();
        for (var it : elevatorService.getElevatorList().values()) {
            elevators.add(new
                    ElevatorModel(it.getElevatorID(), it.getNumberFloor(), it.getCurrentFloor(), it.getDirection()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successful request.", elevators));
    }
}
