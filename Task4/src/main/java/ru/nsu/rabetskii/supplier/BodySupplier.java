package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.component.BodyComponent;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class BodySupplier extends BaseSupplier implements Runnable {

    public BodySupplier(Warehouse warehouse, boolean log) {
        this.warehouse = warehouse;
        this.speed = startSpeed;
        this.log = log;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(speed);
                int bodyId = warehouse.getNewId();
                Component component = new BodyComponent(bodyId);
                warehouse.addComponent(component);
                if (log){
                    System.out.println("Body #" + component.getId());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
