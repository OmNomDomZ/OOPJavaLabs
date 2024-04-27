package ru.nsu.rabetskii.warehouse;

import ru.nsu.rabetskii.component.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BaseWarehouse implements Warehouse {
    private final BlockingQueue<Component> warehouse;

    public BaseWarehouse(int capacity) {
        warehouse = new ArrayBlockingQueue<>(capacity);
    }

    @Override
    public void addComponent(Component component) {
        try{
            warehouse.put(component);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Component getComponent() {
        try {
            return warehouse.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized int getSize() {
        return warehouse.size();
    }
}
