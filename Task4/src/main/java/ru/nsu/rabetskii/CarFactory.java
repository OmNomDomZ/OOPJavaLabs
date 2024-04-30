package ru.nsu.rabetskii;

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

public class CarFactory implements Facade, Observable, Observer {
    private Warehouse bodyWarehouse;
    private Warehouse motorWarehouse;
    private Warehouse accessoryWarehouse;
    private Warehouse autoWarehouse;

    private final ExecutorService suppliersPool;
    private final ExecutorService workerPool;
    private final ExecutorService dealersPool;

    private final List<AccessorySupplier> accessoriesSupplierList;
    private final MotorSupplier motorSupplier;
    private final BodySupplier bodySupplier;
    private final List<Worker> workersList;
    private final List<Dealer> dealersList;

    private int accessorySupplierCount;
    private int workerCount;
    private int dealerCount;

    private boolean log;
    private Observer observer;
    private final Controller controller;

    public CarFactory() {
        readConfig();

        suppliersPool = Executors.newFixedThreadPool(accessorySupplierCount + 2);
        workerPool = Executors.newFixedThreadPool(workerCount);
        dealersPool = Executors.newFixedThreadPool(dealerCount);

        accessoriesSupplierList = new LinkedList<>();
        for (int i = 0; i < accessorySupplierCount; i++) {
            AccessorySupplier accessorySupplier = new AccessorySupplier(accessoryWarehouse, 1000, log);
            accessoriesSupplierList.add(accessorySupplier);
            accessorySupplier.setObservers(this);
            suppliersPool.execute(accessorySupplier);
        }

        motorSupplier = new MotorSupplier(motorWarehouse, 1000, log);
        motorSupplier.setObservers(this);
        suppliersPool.execute(motorSupplier);

        bodySupplier = new BodySupplier(bodyWarehouse, 1000, log);
        bodySupplier.setObservers(this);
        suppliersPool.execute(bodySupplier);

        workersList = new LinkedList<>();
        for (int i = 0; i < workerCount; ++i){
            Worker worker = new Worker(bodyWarehouse, motorWarehouse, accessoryWarehouse, autoWarehouse, i, log);
            worker.setObservers(this);
            workersList.add(worker);
            workerPool.submit(worker);
        }

        controller = new Controller(workersList, autoWarehouse);

        dealersList = new LinkedList<>();
        for (int i = 0; i < dealerCount; ++i) {
            Dealer dealer = new Dealer(autoWarehouse, 1000, controller, i, log);
//            dealer.setObservers(this);
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

    @Override
    public List<AccessorySupplier> getAccessoriesSupplierList() {
        return accessoriesSupplierList;
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
    public void notifyObservers() {
        observer.update();
    }

    @Override
    public void setObservers(Observer observer) {
        this.observer = observer;
    }

    @Override
    public void update() {
        notifyObservers();
    }
}
