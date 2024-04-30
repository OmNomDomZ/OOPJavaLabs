package ru.nsu.rabetskii.component;

public class BaseComponent implements Component {
    protected int id;
    public int getId(){
        return id;
    }

    @Override
    public String getInformation() {
        return null;
    }
}
