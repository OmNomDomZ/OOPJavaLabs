package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.Observable;
import ru.nsu.rabetskii.Observer;
import ru.nsu.rabetskii.component.BodyComponent;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class BodySupplier extends BaseSupplier implements Runnable, Observable {

    public BodySupplier(Warehouse warehouse, int speed, boolean log) {
        this.warehouse = warehouse;
        this.speed = speed;
        this.log = log;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
                Component component = new BodyComponent();
                warehouse.addComponent(component);
                notifyObservers();
                if (log){
                    System.out.println("Body #" + component.getId());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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
