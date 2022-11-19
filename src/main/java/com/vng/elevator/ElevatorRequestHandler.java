package com.vng.elevator;

import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

public class ElevatorRequestHandler implements Runnable {
    private final Elevator elevator;
    private final LinkedBlockingQueue<Integer> requestsWaitingHandler = new LinkedBlockingQueue<>();
    private boolean running = true;

    public ElevatorRequestHandler(Elevator elevator) {
        this.elevator = elevator;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    private synchronized void addRequest() {
        Integer request;
        request = this.requestsWaitingHandler.poll();
        if (request == null) {
            return;
        }
        this.elevator.addRequest(request);
    }

    @Override
    public void run() {
        while (isRunning()) {
            addRequest();
        }
    }
}
