package ru.nsu.rabetskii.dealer;

import ru.nsu.rabetskii.patternobserver.Observable;
import ru.nsu.rabetskii.patternobserver.Observer;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Dealer implements Runnable, Observable {
    private final Warehouse autoWarehouse;
    private int speed;
    private Observer observer;
    private final int dealerId;
    private final int startSpeed = 1000;
    private boolean isRunning = true;

    public Dealer(Warehouse autoWarehouse, Observer controller, int dealerId) {
        this.autoWarehouse = autoWarehouse;
        this.speed = startSpeed;
        this.dealerId = dealerId;
        setObservers(controller);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(speed);
                Component car = autoWarehouse.getComponent();
                LocalTime currentTime = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String formattedTime = currentTime.format(formatter);
                System.out.println("<" + formattedTime + ">" + "Dealer #" + dealerId + " sold car: " + car.getInformation());
                notifyObservers();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void changeIsRunning(){
        this.isRunning = false;
    }
    @Override
    public void notifyObservers() {
        observer.update();
    }

    @Override
    public void setObservers(Observer observer) {
        this.observer = observer;
    }
}