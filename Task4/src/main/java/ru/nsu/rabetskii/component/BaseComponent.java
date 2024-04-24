package ru.nsu.rabetskii.component;

import java.util.concurrent.atomic.AtomicInteger;

public class BaseComponent implements Component {
    private static final AtomicInteger idCounter = new AtomicInteger();
    protected final int id;

    public BaseComponent() {
        this.id = idCounter.incrementAndGet();
    }

    public int getId(){
        return id;
    }
}
