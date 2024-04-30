package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.Listener;
import ru.nsu.rabetskii.component.BodyComponent;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class BodySupplier extends BaseSupplier implements Runnable{

    public BodySupplier(Warehouse warehouse, int speed, Listener listener, boolean log) {
        this.warehouse = warehouse;
        this.speed = speed;
        this.listener = listener;
        this.log = log;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
                Component component = new BodyComponent();
                warehouse.addComponent(component);
                update();
                if (log){
                    System.out.println("Body #" + component.getId());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() {
        notifyListener();
    }

    private void notifyListener() {
        if (listener != null) {
            listener.observableChanged();
        }
    }
}
