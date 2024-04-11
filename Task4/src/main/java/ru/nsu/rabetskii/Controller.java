package ru.nsu.rabetskii;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller implements Runnable{
    private Warehouse autoWarehouse;
    private CarFactory carFactory;
    private ExecutorService executorService;

    public Controller(Warehouse autoWarehouse, CarFactory carFactory){
        this.autoWarehouse = autoWarehouse;
        this.carFactory = carFactory;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            if (!autoWarehouse.isFull()){
                requestNewCar();
            }
        }
    }

    private void requestNewCar(){
        executorService.submit(() -> {
            carFactory.createCar();
        });
    }

    public void shutdown() {
        executorService.shutdownNow();
    }
}
