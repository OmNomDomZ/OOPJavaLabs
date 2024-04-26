package ru.nsu.rabetskii.worker;

import ru.nsu.rabetskii.auto.Auto;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class Worker implements Runnable {
    private Warehouse bodyWarehouse;
    private Warehouse motorWarehouse;
    private Warehouse accessoryWarehouse;
    private Warehouse autoWarehouse;
    private int speed;

    public Worker(Warehouse bodyWarehouse, Warehouse motorWarehouse, Warehouse accessoryWarehouse, Warehouse autoWarehouse, int speed) {
        this.bodyWarehouse = bodyWarehouse;
        this.motorWarehouse = motorWarehouse;
        this.accessoryWarehouse = accessoryWarehouse;
        this.autoWarehouse = autoWarehouse;
        this.speed = speed;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
                Component body = bodyWarehouse.getComponent();
                Component motor = motorWarehouse.getComponent();
                Component accessory = accessoryWarehouse.getComponent();

                Auto car = new Auto(body, motor, accessory);
                autoWarehouse.addComponent(car);

                System.out.println("Worker assembled car: " + car.getCarInformation());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
