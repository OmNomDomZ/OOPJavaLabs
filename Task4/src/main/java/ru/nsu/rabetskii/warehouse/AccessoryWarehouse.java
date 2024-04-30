package ru.nsu.rabetskii.warehouse;

import ru.nsu.rabetskii.patternobserver.Observable;
import ru.nsu.rabetskii.patternobserver.Observer;
import ru.nsu.rabetskii.component.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class AccessoryWarehouse implements Warehouse, Observable {
    BlockingQueue<Component> accessoryWarehouse;
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private Observer observer;

    public AccessoryWarehouse(int capacity){
        accessoryWarehouse = new ArrayBlockingQueue<>(capacity);
    }

    @Override
    public void addComponent(Component component) {
        try{
            accessoryWarehouse.put(component);
            notifyObservers();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Component getComponent() {
        try {
            Component accessory = accessoryWarehouse.take();
            notifyObservers();
            return accessory;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized int getSize() {
        return accessoryWarehouse.size();
    }

    @Override
    public boolean isFull() {
        return accessoryWarehouse.remainingCapacity() == 0;
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
