package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.component.BaseComponent;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class BaseSupplier implements Supplier, Runnable {
    private int speed;
    private final Warehouse warehouse;

    public BaseSupplier(Warehouse warehouse, int speed){
        this.warehouse = warehouse;
        this.speed = speed;
    }

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(speed);
                BaseComponent component = new BaseComponent();
                synchronized (warehouse) {
                    warehouse.addComponent(component);
                    System.out.println("Accessory #" + component.getId());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}