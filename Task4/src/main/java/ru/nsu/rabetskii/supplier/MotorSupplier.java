package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.Observer;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.component.MotorComponent;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class MotorSupplier extends BaseSupplier implements Runnable{

    public MotorSupplier(Warehouse warehouse, int speed, Observer listener) {
        this.warehouse = warehouse;
        this.speed = speed;
        this.listener = listener;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(speed);
                Component component = new MotorComponent();
                warehouse.addComponent(component);
                update();
//                System.out.println("Motor #" + component.getId());
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
