package ru.nsu.rabetskii;

import ru.nsu.rabetskii.dealer.Dealer;
import ru.nsu.rabetskii.warehouse.Warehouse;
import ru.nsu.rabetskii.worker.Worker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Controller implements Listener {

    private List<Worker> workers;
    Warehouse autoWarehouse;

    public Controller(List<Worker> workers, Warehouse autoWarehouse){
        this.workers = new ArrayList<>();
        for (Worker worker : workers){
            addListener(worker);
        }
        this.autoWarehouse = autoWarehouse;

    }

    @Override
    public void observableChanged() {
        if (!autoWarehouse.isFull()){
            for (Worker worker : workers){
                if (worker.isWaiting()){
                    worker.notify();
                }
            }
        }
    }

    public void addListener(Worker worker){
        if (!workers.contains(worker)){
            workers.add(worker);
        }
    }
}
