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
        while (isRunning && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
                int bodyId = warehouse.getNewId();
                Component component = new BodyComponent(bodyId);
                if (!warehouse.addComponent(component)){
                    continue;
                }
                if (log){
                    System.out.println("Body #" + component.getId());
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
