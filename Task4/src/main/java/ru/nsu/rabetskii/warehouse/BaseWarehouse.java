package ru.nsu.rabetskii.warehouse;

import ru.nsu.rabetskii.component.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BaseWarehouse implements Warehouse {
    private int size;
    private BlockingQueue<Component> warehouse;

    @Override
    public void Constructor(int size) {
        this.size = size;
        warehouse = new ArrayBlockingQueue<>(size);
    }

    @Override
    public  void addComponent(Component component) {
        try{
            warehouse.put(component);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
