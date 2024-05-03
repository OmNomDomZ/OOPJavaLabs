package ru.nsu.rabetskii.warehouse;

import ru.nsu.rabetskii.patternobserver.Observable;
import ru.nsu.rabetskii.patternobserver.Observer;
import ru.nsu.rabetskii.component.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MotorWarehouse implements Warehouse, Observable {
    BlockingQueue<Component> motorWarehouse;
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private Observer observer;
    public MotorWarehouse(int capacity){
        motorWarehouse = new ArrayBlockingQueue<>(capacity);
    }

    @Override
    public boolean addComponent(Component component) {
        try{
            boolean res = motorWarehouse.offer(component, 5, TimeUnit.SECONDS);
            if (res) {
                notifyObservers();
            }
            return res;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Component getComponent() throws InterruptedException{
        Component motor = motorWarehouse.poll(5, TimeUnit.SECONDS);
        notifyObservers();
        return motor;
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
