package ru.nsu.rabetskii.auto;

import ru.nsu.rabetskii.component.BaseComponent;
import ru.nsu.rabetskii.component.Component;

import java.util.concurrent.atomic.AtomicInteger;

public class Auto extends BaseComponent {
    private final Component body;
    private final Component motor;
    private final Component accessory;
    private static final AtomicInteger idCounter = new AtomicInteger();

    public Auto(Component body, Component motor, Component accessory) {
        this.id = idCounter.incrementAndGet();
        this.body = body;
        this.motor = motor;
        this.accessory = accessory;
    }

    public String getCarInformation() {
        return "Auto{" +
                "id=" + id +
                ", body=" + body.getId() +
                ", motor=" + motor.getId() +
                ", accessory=" + accessory.getId() +
                '}';
    }
}