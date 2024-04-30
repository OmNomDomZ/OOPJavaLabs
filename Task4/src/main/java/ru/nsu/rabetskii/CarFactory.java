package ru.nsu.rabetskii;

import ru.nsu.rabetskii.auto.Auto;
import ru.nsu.rabetskii.dealer.Dealer;
import ru.nsu.rabetskii.supplier.*;
import ru.nsu.rabetskii.warehouse.BaseWarehouse;
import ru.nsu.rabetskii.warehouse.Warehouse;
import ru.nsu.rabetskii.worker.Worker;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarFactory implements Facade, Listener {
    private Warehouse bodyWarehouse;
    private Warehouse motorWarehouse;
    private Warehouse accessoryWarehouse;
    private Warehouse autoWarehouse;

    private final ExecutorService suppliersPool;
    private ExecutorService workerPool;
    private ExecutorService dealersPool;

    private final List<Supplier> accessoriesSupplierList;
    private final Supplier motorSupplier;
    private final Supplier bodySupplier;
    private final List<Worker> workersList;
    private final List<Dealer> dealersList;

    private int accessorySupplierCount;
    private int workerCount;
    private int dealerCount;

    private boolean log;
    private Listener listener;
    private Controller controller;

    public CarFactory() {
        readConfig();

        suppliersPool = Executors.newFixedThreadPool(accessorySupplierCount + 2);
        workerPool = Executors.newFixedThreadPool(workerCount);
        dealersPool = Executors.newFixedThreadPool(dealerCount);

        accessoriesSupplierList = new LinkedList<>();
        for (int i = 0; i < accessorySupplierCount; i++) {
            Supplier accessorySupplier = new AccessorySupplier(accessoryWarehouse, 10000, this, log);
            accessoriesSupplierList.add(accessorySupplier);
            suppliersPool.execute((Runnable) accessorySupplier);
        }

        motorSupplier = new MotorSupplier(motorWarehouse, 10000, this, log);
        suppliersPool.execute((Runnable) motorSupplier);

        bodySupplier = new BodySupplier(bodyWarehouse, 10000, this, log);
        suppliersPool.execute((Runnable) bodySupplier);

        workersList = new LinkedList<>();
        for (int i = 0; i < workerCount; ++i){
            Worker worker = new Worker(bodyWarehouse, motorWarehouse, accessoryWarehouse, autoWarehouse, this, 9000, i, log);
            workersList.add(worker);
            workerPool.submit(worker);
        }

        controller = new Controller(workersList, autoWarehouse);

        dealersList = new LinkedList<>();
        for (int i = 0; i < dealerCount; ++i) {
            Dealer dealer = new Dealer(autoWarehouse, 11000, controller, i, log);
            dealersList.add(dealer);
            dealersPool.submit(dealer);
        }
    }

    private void readConfig() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/config.properties"));

            bodyWarehouse = new BaseWarehouse(Integer.parseInt(properties.getProperty("BodyWarehouseSize")));
            accessoryWarehouse = new BaseWarehouse(Integer.parseInt(properties.getProperty("AccessoryWarehouseSize")));
            motorWarehouse = new BaseWarehouse(Integer.parseInt(properties.getProperty("MotorWarehouseSize")));
            autoWarehouse = new BaseWarehouse(Integer.parseInt(properties.getProperty("AutoWarehouseSize")));

            accessorySupplierCount = Integer.parseInt(properties.getProperty("AccessorySupplierCount"));
            workerCount = Integer.parseInt(properties.getProperty("WorkerCount"));
            dealerCount = Integer.parseInt(properties.getProperty("DealerCount"));

            log = Boolean.parseBoolean(properties.getProperty("log"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        synchronized (this){
            notifyListener();
        }
    }

    private void notifyListener() {
        synchronized (this){
            if (listener != null) {
                listener.observableChanged();
            }
        }
    }
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public List<Supplier> getAccessoriesSupplierList() {
        return accessoriesSupplierList;
    }
    @Override
    public List<Worker> getWorkers(){
        return workersList;
    }
    @Override
    public Supplier getMotorSupplier() {
        return motorSupplier;
    }

    @Override
    public Supplier getBodySupplier() {
        return bodySupplier;
    }

    @Override
    public Warehouse getAccessoryWarehouse() {
        return accessoryWarehouse;
    }

    @Override
    public Warehouse getBodyWarehouse() {
        return bodyWarehouse;
    }

    @Override
    public Warehouse getMotorWarehouse() {
        return motorWarehouse;
    }

    @Override
    public Warehouse getAutoWarehouse() {
        return autoWarehouse;
    }

    @Override
    public ExecutorService getWorkerPool() {
        return workerPool;
    }

    @Override
    public int getWorkerCount(){
        return workerCount;
    }

    @Override
    public void observableChanged() {
        update();
    }


}
