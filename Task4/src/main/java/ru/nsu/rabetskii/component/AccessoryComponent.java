package ru.nsu.rabetskii.component;

import java.util.concurrent.atomic.AtomicInteger;

public class AccessoryComponent extends BaseComponent{
    private static final AtomicInteger idCounter = new AtomicInteger();
    public AccessoryComponent(){
        this.id = idCounter.incrementAndGet();
    }
}
