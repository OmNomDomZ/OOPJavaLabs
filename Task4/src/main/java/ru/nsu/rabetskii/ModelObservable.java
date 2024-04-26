package ru.nsu.rabetskii;

public interface ModelObservable {
    void addObserver(ModelListener observer);
    void removeObserver(ModelListener observer);
    void notifyObservers();
}