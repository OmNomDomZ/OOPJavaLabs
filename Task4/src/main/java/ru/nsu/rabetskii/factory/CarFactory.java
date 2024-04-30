package ru.nsu.rabetskii.factory;

import ru.nsu.rabetskii.autocontroller.AutoController;
import ru.nsu.rabetskii.dealer.Dealer;
import ru.nsu.rabetskii.patternobserver.Observable;
import ru.nsu.rabetskii.patternobserver.Observer;
import ru.nsu.rabetskii.supplier.*;
import ru.nsu.rabetskii.warehouse.*;
import ru.nsu.rabetskii.worker.Worker;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarFactory implements Facade, Observable, Observer {
    private BodyWarehouse bodyWarehouse;
    private MotorWarehouse motorWarehouse;
    private AccessoryWarehouse accessoryWarehouse;
    private AutoWarehouse autoWarehouse;

    private final ExecutorService suppliersPool;
    private final ExecutorService workersPool;
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
    private final AutoController controller;

    public CarFactory() {
        readConfig();

        suppliersPool = Executors.newFixedThreadPool(accessorySupplierCount + 2);
        workersPool = Executors.newFixedThreadPool(workerCount);
        dealersPool = Executors.newFixedThreadPool(dealerCount);

        bodyWarehouse.setObservers(this);
        accessoryWarehouse.setObservers(this);
        motorWarehouse.setObservers(this);
        autoWarehouse.setObservers(this);

        accessoriesSupplierList = new LinkedList<>();
        for (int i = 1; i <= accessorySupplierCount; i++) {
            AccessorySupplier accessorySupplier = new AccessorySupplier(accessoryWarehouse, log);
            accessoriesSupplierList.add(accessorySupplier);
            suppliersPool.execute(accessorySupplier);
        }

        motorSupplier = new MotorSupplier(motorWarehouse, log);
        suppliersPool.execute(motorSupplier);

        bodySupplier = new BodySupplier(bodyWarehouse, log);
        suppliersPool.execute(bodySupplier);

        workersList = new LinkedList<>();
        for (int i = 1; i <= workerCount; ++i){
            Worker worker = new Worker(bodyWarehouse, motorWarehouse, accessoryWarehouse, autoWarehouse, i, log);
            worker.setObservers(this);
            workersList.add(worker);
            workersPool.execute(worker);
        }

        controller = new AutoController(workersList, autoWarehouse);

        dealersList = new LinkedList<>();
        for (int i = 1; i <= dealerCount; ++i) {
            Dealer dealer = new Dealer(autoWarehouse, controller, i);
            dealersList.add(dealer);
            dealer.setObservers(controller);
            dealersPool.execute(dealer);
        }
    }

    private void readConfig() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/config.properties"));

            bodyWarehouse = new BodyWarehouse(Integer.parseInt(properties.getProperty("BodyWarehouseSize")));
            accessoryWarehouse = new AccessoryWarehouse(Integer.parseInt(properties.getProperty("AccessoryWarehouseSize")));
            motorWarehouse = new MotorWarehouse(Integer.parseInt(properties.getProperty("MotorWarehouseSize")));
            autoWarehouse = new AutoWarehouse(Integer.parseInt(properties.getProperty("AutoWarehouseSize")));


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
    public List<Dealer> getDealerList() {
        return dealersList;
    }

    @Override
    public List<Worker> getWorkerList() {
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
    public ExecutorService getSuppliersPool() {
        return suppliersPool;
    }

    @Override
    public ExecutorService getWorkersPool() {
        return workersPool;
    }

    @Override
    public ExecutorService getDealersPool() {
        return dealersPool;
    }

    @Override
    public void notifyObservers() {
        if (observer != null){
            observer.update();
        }
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
