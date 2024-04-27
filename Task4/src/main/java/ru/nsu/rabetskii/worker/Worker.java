package ru.nsu.rabetskii.worker;

import ru.nsu.rabetskii.Controller;
import ru.nsu.rabetskii.Observer;
import ru.nsu.rabetskii.auto.Auto;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class Worker implements Runnable, Observer {
    private final Warehouse bodyWarehouse;
    private final Warehouse motorWarehouse;
    private final Warehouse accessoryWarehouse;
    private final Warehouse autoWarehouse;
    private final Observer listener;
    private final Controller controller;
    private final int speed;

    public Worker(Warehouse bodyWarehouse, Warehouse motorWarehouse, Warehouse accessoryWarehouse, Warehouse autoWarehouse, Observer listener, int speed, Controller controller) {
        this.bodyWarehouse = bodyWarehouse;
        this.motorWarehouse = motorWarehouse;
        this.accessoryWarehouse = accessoryWarehouse;
        this.autoWarehouse = autoWarehouse;
        this.listener = listener;
        this.speed = speed;
        this.controller = controller;
        controller.setListener(this);
    }

    @Override
    public void observableChanged() {
        if (autoWarehouse.getSize() == 0){
            createCar();
        }
    }

    public void createCar(){
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
                Component body = null;
                Component motor = null;
                Component accessory = null;
                Auto car = null;

                if (bodyWarehouse.getSize() > 0 && motorWarehouse.getSize() > 0 && accessoryWarehouse.getSize() > 0) {
                    body = bodyWarehouse.getComponent();
                    motor = motorWarehouse.getComponent();
                    accessory = accessoryWarehouse.getComponent();
                    car = new Auto(body, motor, accessory);
                    autoWarehouse.addComponent(car);
                    update();
                    System.out.println("Worker assembled car: " + car.getCarInformation());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void update() {
        notifyListener();
    }

    private void notifyListener() {
        if (listener != null) {
            listener.observableChanged();
        }
    }

    @Override
    public void run() {

    }
}
