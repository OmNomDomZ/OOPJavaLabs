package ru.nsu.rabetskii.warehouse;

import ru.nsu.rabetskii.patternobserver.Observable;
import ru.nsu.rabetskii.patternobserver.Observer;
import ru.nsu.rabetskii.component.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BodyWarehouse implements Warehouse, Observable {
    BlockingQueue<Component> bodyWarehouse;
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private Observer observer;

    public BodyWarehouse(int capacity){
        bodyWarehouse = new ArrayBlockingQueue<>(capacity);
    }

    @Override
    public boolean addComponent(Component component) {
        try{
            boolean res = bodyWarehouse.offer(component, 5, TimeUnit.SECONDS);
            if (res){
                notifyObservers();
            }
            return res;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Component getComponent() throws InterruptedException{
            Component body = bodyWarehouse.poll(5, TimeUnit.SECONDS);
            notifyObservers();
            return body;
    }

    @Override
    public synchronized int getSize() {
        return bodyWarehouse.size();
    }

    @Override
    public boolean isFull() {
        return bodyWarehouse.remainingCapacity() == 0;
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
