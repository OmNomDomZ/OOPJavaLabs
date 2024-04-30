package ru.nsu.rabetskii.warehouse;

import ru.nsu.rabetskii.patternobserver.Observable;
import ru.nsu.rabetskii.patternobserver.Observer;
import ru.nsu.rabetskii.component.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MotorWarehouse implements Warehouse, Observable {
    BlockingQueue<Component> motorWarehouse;
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private Observer observer;
    public MotorWarehouse(int capacity){
        motorWarehouse = new ArrayBlockingQueue<>(capacity);
    }

    @Override
    public void addComponent(Component component) {
        try{
            motorWarehouse.put(component);
            notifyObservers();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Component getComponent() {
        try {
            Component motor = motorWarehouse.take();
            notifyObservers();
            return motor;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized int getSize() {
        return motorWarehouse.size();
    }

    @Override
    public boolean isFull() {
        return motorWarehouse.remainingCapacity() == 0;
    }

    @Override
    public int getNewId() {
        return idCounter.getAndIncrement();
    }
    @Override
    public int getLastId() {
        return idCounter.get();
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
