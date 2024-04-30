package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.Listener;
import ru.nsu.rabetskii.component.AccessoryComponent;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class AccessorySupplier extends BaseSupplier implements Runnable {
    public AccessorySupplier(Warehouse warehouse, int speed, Listener listener, boolean log) {
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
                Component component = new AccessoryComponent();
                warehouse.addComponent(component);
                update();
                if (log){ System.out.println("Accessory #" + component.getId());}
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