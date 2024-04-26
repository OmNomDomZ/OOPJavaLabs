package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.component.BaseComponent;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class BaseSupplier implements Supplier {
    protected int speed;
    protected Warehouse warehouse;

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }
}