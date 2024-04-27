package ru.nsu.rabetskii;

import ru.nsu.rabetskii.dealer.Dealer;
import ru.nsu.rabetskii.worker.Worker;

import java.util.LinkedList;
import java.util.List;

public class Controller implements Observer {
    private final CarFactory carFactory;
    private final Dealer dealer;
    private List<Observer> listeners;

    public Controller(Dealer dealer, CarFactory carFactory){
        listeners = new LinkedList<>();
        this.carFactory = carFactory;
        this.dealer = dealer;
        dealer.setListener(this);
    }

    @Override
    public void observableChanged() {
        for (Observer listener : listeners){
            listener.observableChanged();
        }
    }

    public void setListener(Observer listener){
        listeners.add(listener);
    }
}
