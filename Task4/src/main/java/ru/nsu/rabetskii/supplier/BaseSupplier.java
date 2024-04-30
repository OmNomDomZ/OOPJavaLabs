package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.Listener;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class BaseSupplier implements Supplier {
    protected int speed;
    protected Warehouse warehouse;
    protected Listener listener;
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