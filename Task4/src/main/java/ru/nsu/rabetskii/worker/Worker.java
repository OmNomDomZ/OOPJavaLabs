package ru.nsu.rabetskii.worker;

import ru.nsu.rabetskii.Controller;
import ru.nsu.rabetskii.Listener;
import ru.nsu.rabetskii.auto.Auto;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class Worker implements Runnable, Listener {
    private final Warehouse bodyWarehouse;
    private final Warehouse motorWarehouse;
    private final Warehouse accessoryWarehouse;
    private final Warehouse autoWarehouse;

    private int workerId;
    private final Listener listener;
    private final int speed;
    private boolean log;
    private boolean isWaiting = false;

    public Worker(Warehouse bodyWarehouse, Warehouse motorWarehouse, Warehouse accessoryWarehouse, Warehouse autoWarehouse, Listener listener, int speed, int id, boolean log) {
        this.bodyWarehouse = bodyWarehouse;
        this.motorWarehouse = motorWarehouse;
        this.accessoryWarehouse = accessoryWarehouse;
        this.autoWarehouse = autoWarehouse;
        this.listener = listener;
        this.speed = speed;
        this.workerId = id;
        this.log = log;
    }

    @Override
    public void run(){
        while (!Thread.currentThread().isInterrupted()) {
            Component body = bodyWarehouse.getComponent();
            Component motor = motorWarehouse.getComponent();
            Component accessory = accessoryWarehouse.getComponent();
            Auto car = new Auto(body, motor, accessory);
            autoWarehouse.addComponent(car);
            update();
            if (log){
                System.out.println("Worker assembled car: " + car.getCarInformation());
            }
            synchronized (this) {
                isWaiting = true;
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
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
    public void observableChanged() {

    }

    public boolean isWaiting() {
        return isWaiting;
    }
}
