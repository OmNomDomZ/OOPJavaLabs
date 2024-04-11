package ru.nsu.rabetskii;

public interface Warehouse {
    boolean addComponent(Component component);
    Component getComponent() throws InterruptedException;
    int getCurrentSize();
    int getCapacity();
    boolean isFull();
}
