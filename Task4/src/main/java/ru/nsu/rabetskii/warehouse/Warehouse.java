package ru.nsu.rabetskii.warehouse;

import ru.nsu.rabetskii.component.Component;

public interface Warehouse {
    boolean addComponent(Component component);
    Component getComponent() throws InterruptedException;
    int getSize();
    boolean isFull();
    int getNewId();
    int getLastId();
}
