package ru.nsu.rabetskii.dealer;

import ru.nsu.rabetskii.Observable;
import ru.nsu.rabetskii.Observer;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class Dealer implements Runnable, Observable {
    private final Warehouse autoWarehouse;
    private final int speed;
    private Observer observer;
    private final int dealerId;
    private final boolean log;

    public Dealer(Warehouse autoWarehouse, int speed, Observer controller, int dealerId, boolean log) {
        this.autoWarehouse = autoWarehouse;
        this.speed = speed;
        this.dealerId = dealerId;
        this.log = log;
        setObservers(controller);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
                Component car = autoWarehouse.getComponent();
                if (log){
                    System.out.println("Dealer #" + dealerId + " sold car: " + car.getId());
                }
                notifyObservers();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
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