package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.patternobserver.Observer;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class BaseSupplier implements Supplier {
    protected int speed;
    protected final int startSpeed = 1000;
    protected Warehouse warehouse;
    protected Observer observer;
    protected boolean isRunning = true;
    protected boolean log;
    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public void changeIsRunning() {
        isRunning = false;
    }
}