package ru.nsu.rabetskii.dealer;

import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class Dealer implements Runnable {
    private Warehouse autoWarehouse;
    private int speed;

    public Dealer(Warehouse autoWarehouse, int speed) {
        this.autoWarehouse = autoWarehouse;
        this.speed = speed;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
                Component car = autoWarehouse.getComponent();
                System.out.println("Dealer sold car: " + car.getId());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}