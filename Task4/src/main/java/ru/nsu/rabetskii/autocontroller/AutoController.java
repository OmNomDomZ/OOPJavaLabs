package ru.nsu.rabetskii.autocontroller;

import ru.nsu.rabetskii.patternobserver.Observer;
import ru.nsu.rabetskii.warehouse.Warehouse;
import ru.nsu.rabetskii.worker.Worker;

import java.util.List;

public class AutoController implements Observer {

    private final List<Worker> workers;
    private final Warehouse autoWarehouse;

    public AutoController(List<Worker> workers, Warehouse autoWarehouse){
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
