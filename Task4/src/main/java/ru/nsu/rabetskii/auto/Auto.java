package ru.nsu.rabetskii.auto;

import ru.nsu.rabetskii.component.BaseComponent;
import ru.nsu.rabetskii.component.Component;

public class Auto extends BaseComponent {
    private final Component body;
    private final Component motor;
    private final Component accessory;

    public Auto(Component body, Component motor, Component accessory, int autoId) {
        this.id = autoId;
        this.body = body;
        this.motor = motor;
        this.accessory = accessory;
    }

    @Override
    public String getInformation() {
        return "Car " +
                "id #" + id +
                " {body=" + body.getId() +
                ", motor=" + motor.getId() +
                ", accessory=" + accessory.getId() +
                '}';
    }
}