package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.component.AccessoryComponent;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class AccessorySupplier extends BaseSupplier implements Runnable {
    public AccessorySupplier(Warehouse warehouse, boolean log) {
        this.warehouse = warehouse;
        this.speed = startSpeed;
        this.log = log;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(speed);
                int accessoryId = warehouse.getNewId();
                Component component = new AccessoryComponent(accessoryId);
                warehouse.addComponent(component);
                if (log) {
                    System.out.println("Accessory #" + component.getId());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}