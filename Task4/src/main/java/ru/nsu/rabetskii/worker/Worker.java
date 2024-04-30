package ru.nsu.rabetskii.worker;

import ru.nsu.rabetskii.auto.Auto;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.patternobserver.Observable;
import ru.nsu.rabetskii.patternobserver.Observer;
import ru.nsu.rabetskii.warehouse.Warehouse;

import java.util.concurrent.atomic.AtomicInteger;

public class Worker implements Runnable, Observable {
    private final Warehouse bodyWarehouse;
    private final Warehouse motorWarehouse;
    private final Warehouse accessoryWarehouse;
    private final Warehouse autoWarehouse;

    Observer observer;

    private final int workerId;
    private final boolean log;
    private boolean isWaiting = false;
    private boolean isRunning = true;
    private int autoId;
    private static final AtomicInteger numTasks = new AtomicInteger();

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
        while (isRunning) {
            isWaiting = false;
            numTasks.incrementAndGet();
            notifyObservers();
            Component body = bodyWarehouse.getComponent();
            Component motor = motorWarehouse.getComponent();
            Component accessory = accessoryWarehouse.getComponent();
            autoId = autoWarehouse.getNewId();
            Auto car = new Auto(body, motor, accessory, autoId);
            if (log){
                System.out.println("Worker #" + workerId + " assembled car: " + car.getId());
            }
            synchronized (this) {
                autoWarehouse.addComponent(car);
                isWaiting = true;
                try {
                    numTasks.decrementAndGet();
                    notifyObservers();
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void changeIsRunning(){
        this.isRunning = false;
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    public int getNumTasks(){
        return numTasks.get();
    }

    @Override
    public void notifyObservers() {
        if (observer != null) {
            observer.update();
        }
    }

    @Override
    public void setObservers(Observer observer) {
        this.observer = observer;
    }
}
