package ru.nsu.rabetskii.component;

import java.util.concurrent.atomic.AtomicInteger;

public class MotorComponent extends BaseComponent{
    private static final AtomicInteger idCounter = new AtomicInteger();
    public MotorComponent(){
        this.id = idCounter.incrementAndGet();
    }
}
