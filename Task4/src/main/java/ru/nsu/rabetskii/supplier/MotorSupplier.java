package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.CarFactory;
import ru.nsu.rabetskii.component.BaseComponent;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.component.MotorComponent;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class MotorSupplier extends BaseSupplier implements Runnable{
    CarFactory factory; // надо придумать по другому

    public MotorSupplier(Warehouse warehouse, int speed, CarFactory factory) {
        this.warehouse = warehouse;
        this.speed = speed;

        this.factory = factory;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(speed);
                Component component = new MotorComponent();
                warehouse.addComponent(component);
                factory.update();
                System.out.println("Motor #" + component.getId());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
