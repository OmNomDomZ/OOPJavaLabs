package ru.nsu.rabetskii;

import ru.nsu.rabetskii.warehouse.Warehouse;
import ru.nsu.rabetskii.worker.Worker;

import java.util.List;

public class Controller implements Observer {

    private final List<Worker> workers;
    Warehouse autoWarehouse;

    public Controller(List<Worker> workers, Warehouse autoWarehouse){
        this.workers = workers;
        this.autoWarehouse = autoWarehouse;
    }

    @Override
    public void update() {
        if (!autoWarehouse.isFull()){
            for (Worker worker : workers){
                if (worker.isWaiting()){
                    synchronized (worker){
                        worker.notify();
                    }
                }
            }
        }
    }

}
