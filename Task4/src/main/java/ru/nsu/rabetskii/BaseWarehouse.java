package ru.nsu.rabetskii;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BaseWarehouse implements Warehouse{
    int capacity;
    BlockingQueue<Component> components;
    public BaseWarehouse(int capacity){
        this.capacity = capacity;
        components = new LinkedBlockingQueue<>(capacity);
    }

    public boolean addComponent(Component component){
        if (components.size() < capacity){
            components.offer(component);
            return true;
        }
        return false;
    }
    public Component getComponent() throws InterruptedException {
        while (!components.isEmpty()){
            wait();
        }
        notifyAll();
        return components.poll();
    }
    public int getCurrentSize(){
        return components.size();
    }
    public int getCapacity(){
        return capacity;
    }

    public boolean isFull() {
        return components.size() == capacity;
    }

}
