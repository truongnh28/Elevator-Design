package com.vng.elevator;

public enum Direction {
    UP(1), DOWN(-1), STATIONARY(0);
    int step;
    Direction(int step) {
        this.step = step;
    }
}
