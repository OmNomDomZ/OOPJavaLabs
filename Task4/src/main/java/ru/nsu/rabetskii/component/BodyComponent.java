package ru.nsu.rabetskii.component;

import java.util.concurrent.atomic.AtomicInteger;

public class BodyComponent extends BaseComponent{
    private static final AtomicInteger idCounter = new AtomicInteger();
    public BodyComponent(){
        this.id = idCounter.incrementAndGet();
    }
}
