package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.CarFactory;
import ru.nsu.rabetskii.component.BaseComponent;
import ru.nsu.rabetskii.component.BodyComponent;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class BodySupplier extends BaseSupplier implements Runnable{
    CarFactory factory; // надо придумать по другому

    public BodySupplier(Warehouse warehouse, int speed, CarFactory factory) {
        this.warehouse = warehouse;
        this.speed = speed;

        this.factory = factory;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
                Component component = new BodyComponent();
                warehouse.addComponent(component);
                factory.update();
                System.out.println("Body #" + component.getId());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
