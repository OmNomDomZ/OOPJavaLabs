package ru.nsu.rabetskii.warehouse;

import ru.nsu.rabetskii.patternobserver.Observable;
import ru.nsu.rabetskii.patternobserver.Observer;
import ru.nsu.rabetskii.component.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AccessoryWarehouse implements Warehouse, Observable {
    BlockingQueue<Component> accessoryWarehouse;
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private Observer observer;

    public AccessoryWarehouse(int capacity){
        accessoryWarehouse = new ArrayBlockingQueue<>(capacity);
    }

    @Override
    public boolean addComponent(Component component) {
        try{
            boolean res = accessoryWarehouse.offer(component, 5, TimeUnit.SECONDS);
            if (res) {
                notifyObservers();
            }
            return res;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Component getComponent() throws InterruptedException {
        Component accessory = accessoryWarehouse.poll(5, TimeUnit.SECONDS);
        notifyObservers();
        return accessory;
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
