package ru.nsu.rabetskii;

public interface Observable {
    void notifyObservers();
    void setObservers(Observer observer);
}
