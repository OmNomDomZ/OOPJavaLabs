package ru.nsu.rabetskii.component;

import java.util.concurrent.atomic.AtomicInteger;

public class BaseComponent implements Component {
    protected int id;
    public int getId(){
        return id;
    }
}
