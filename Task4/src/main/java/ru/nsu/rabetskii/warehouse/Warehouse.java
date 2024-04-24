package ru.nsu.rabetskii.warehouse;

import ru.nsu.rabetskii.component.Component;

public interface Warehouse {
    void addComponent(Component component);
    void Constructor(int size);
//    Component getComponent() throws InterruptedException;
}
