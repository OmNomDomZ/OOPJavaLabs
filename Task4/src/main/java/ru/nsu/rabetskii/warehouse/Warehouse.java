package ru.nsu.rabetskii.warehouse;

import ru.nsu.rabetskii.component.Component;

public interface Warehouse {
    void addComponent(Component component);
    Component getComponent();
    int getSize();
}
