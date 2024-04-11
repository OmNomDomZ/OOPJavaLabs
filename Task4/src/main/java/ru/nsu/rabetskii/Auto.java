package ru.nsu.rabetskii;

public class Auto extends BaseComponent {
    private Component body;
    private Component motor;
    private Component accessory;

    public Auto(Component body, Component motor, Component accessory) {
        super();
        this.body = body;
        this.motor = motor;
        this.accessory = accessory;
    }

    public Component getBody() {
        return body;
    }

    public Component getMotor() {
        return motor;
    }

    public Component getAccessory() {
        return accessory;
    }

    public String toString() {
        return "Auto{" +
                "id=" + id +
                ", body=" + body.getId() +
                ", motor=" + motor.getId() +
                ", accessory=" + accessory.getId() +
                '}';
    }
}