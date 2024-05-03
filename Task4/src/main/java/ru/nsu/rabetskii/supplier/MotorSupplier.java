package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.component.MotorComponent;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class MotorSupplier extends BaseSupplier implements Runnable {

    public MotorSupplier(Warehouse warehouse, boolean log) {
        this.warehouse = warehouse;
        this.speed = startSpeed;
        this.log = log;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(speed);
                int motorId = warehouse.getNewId();
                Component component = new MotorComponent(motorId);
                if (!warehouse.addComponent(component)){
                    continue;
                }
                if (log){
                    System.out.println("Motor #" + component.getId());
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
