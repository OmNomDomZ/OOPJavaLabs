package ru.nsu.rabetskii.dealer;

import ru.nsu.rabetskii.Controller;
import ru.nsu.rabetskii.Observer;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

import javax.sql.RowSetListener;
import java.util.LinkedList;
import java.util.Set;

public class Dealer implements Runnable {
    private Warehouse autoWarehouse;
    private int speed;
    private Observer listener;

    public Dealer(Warehouse autoWarehouse, int speed) {
        this.autoWarehouse = autoWarehouse;
        this.speed = speed;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
                Component car = autoWarehouse.getComponent();
                System.out.println("Dealer sold car: " + car.getId());

                listener.observableChanged();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void setListener(Observer listener){
        this.listener = listener;
    }
}