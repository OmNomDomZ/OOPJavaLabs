package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.Observer;
import ru.nsu.rabetskii.component.AccessoryComponent;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class AccessorySupplier extends BaseSupplier implements Runnable {
    public AccessorySupplier(Warehouse warehouse, int speed, Observer listener) {
        this.warehouse = warehouse;
        this.speed = speed;
        this.listener = listener;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(speed);
                Component component = new AccessoryComponent();
                warehouse.addComponent(component);
                update();
//                System.out.println("Accessory #" + component.getId());
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