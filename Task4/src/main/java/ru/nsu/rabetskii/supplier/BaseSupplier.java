package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.Observer;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class BaseSupplier implements Supplier {
    protected int speed;
    protected Warehouse warehouse;
    protected Observer observer;
    protected boolean log;
    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public int getSpeed() {
        return speed;
    }
}