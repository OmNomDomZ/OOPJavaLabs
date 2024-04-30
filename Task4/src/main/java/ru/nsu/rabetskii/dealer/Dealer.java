package ru.nsu.rabetskii.dealer;

import ru.nsu.rabetskii.Listener;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class Dealer implements Runnable {
    private Warehouse autoWarehouse;
    private int speed;
    private Listener listener;
    private int dealerId;
    private boolean log;

    public Dealer(Warehouse autoWarehouse, int speed, Listener listener, int dealerId, boolean log) {
        this.autoWarehouse = autoWarehouse;
        this.speed = speed;
        setListener(listener);
        this.dealerId = dealerId;
        this.log = log;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
                Component car = autoWarehouse.getComponent();
                if (log){
                    System.out.println("Dealer sold car: " + car.getId());
                }
                if (listener != null) {
                    listener.observableChanged();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }
}