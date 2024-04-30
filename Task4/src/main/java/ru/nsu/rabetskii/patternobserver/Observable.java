package ru.nsu.rabetskii.patternobserver;

public interface Observable {
    void notifyObservers();
    void setObservers(Observer observer);
}
