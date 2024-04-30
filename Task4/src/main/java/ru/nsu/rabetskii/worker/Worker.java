package ru.nsu.rabetskii.worker;

import ru.nsu.rabetskii.Observable;
import ru.nsu.rabetskii.Observer;
import ru.nsu.rabetskii.auto.Auto;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class Worker implements Runnable, Observable {
    private final Warehouse bodyWarehouse;
    private final Warehouse motorWarehouse;
    private final Warehouse accessoryWarehouse;
    private final Warehouse autoWarehouse;

    private final int workerId;
    private Observer observer;
    private final boolean log;
    private boolean isWaiting = false;

    public Worker(Warehouse bodyWarehouse, Warehouse motorWarehouse, Warehouse accessoryWarehouse, Warehouse autoWarehouse, int id, boolean log) {
        this.bodyWarehouse = bodyWarehouse;
        this.motorWarehouse = motorWarehouse;
        this.accessoryWarehouse = accessoryWarehouse;
        this.autoWarehouse = autoWarehouse;
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
            if (log){
                System.out.println("Worker #" + workerId + "assembled car: " + car.getCarInformation());
            }
            notifyObservers();
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

    public boolean isWaiting() {
        return isWaiting;
    }

    @Override
    public void notifyObservers() {
        observer.update();
    }

    @Override
    public void setObservers(Observer observer) {
        this.observer = observer;
    }
}
