package ru.nsu.rabetskii;

public class BaseComponent implements Component{
    protected static int idCounter = 0;
    protected final int id;

    BaseComponent() {
        this.id = ++idCounter;
    }

    public int getId(){
        return id;
    }
}
