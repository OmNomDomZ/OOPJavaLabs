package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.component.AccessoryComponent;
import ru.nsu.rabetskii.component.BaseComponent;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class AccessorySupplier extends BaseSupplier implements Runnable{
    public AccessorySupplier(Warehouse warehouse, int speed) {
        this.warehouse = warehouse;
        this.speed = speed;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(speed);
                Component component = new AccessoryComponent();
                warehouse.addComponent(component);
                System.out.println("Accessory #" + component.getId());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
