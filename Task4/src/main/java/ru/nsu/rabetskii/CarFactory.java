package ru.nsu.rabetskii;

import ru.nsu.rabetskii.dealer.Dealer;
import ru.nsu.rabetskii.supplier.*;
import ru.nsu.rabetskii.warehouse.BaseWarehouse;
import ru.nsu.rabetskii.warehouse.Warehouse;
import ru.nsu.rabetskii.worker.Worker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarFactory implements Facade, ModelObservable{
    private Warehouse bodyWarehouse;
    private Warehouse motorWarehouse;
    private Warehouse accessoryWarehouse;
    private Warehouse autoWarehouse;

    private final ExecutorService suppliersPool;
    private final ExecutorService workerPool;
    private ExecutorService dealersPool;

    private final List<Supplier> accessoriesSupplierList;
    private final Supplier motorSupplier;
    private final Supplier bodySupplier;

    private int accessorySupplierCount;
    private int workerCount;
    private int dealerCount;
    private ModelListener listener;
    private List<ModelListener> observers = new ArrayList<>();

    @Override
    public void addObserver(ModelListener observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ModelListener observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (ModelListener observer : observers) {
            observer.onModelChanged();
        }
    }

    public CarFactory() {
        readConfig();

        suppliersPool = Executors.newFixedThreadPool(accessorySupplierCount + 2);
        workerPool = Executors.newFixedThreadPool(workerCount);
        dealersPool = Executors.newFixedThreadPool(dealerCount);

        accessoriesSupplierList = new LinkedList<>();

        for (int i = 0; i < accessorySupplierCount; i++) {
            Supplier accessorySupplier = new AccessorySupplier(accessoryWarehouse, 5000, this);
            accessoriesSupplierList.add(accessorySupplier);
            suppliersPool.submit((Runnable) accessorySupplier);
        }

        motorSupplier = new MotorSupplier(motorWarehouse, 3000, this);
        suppliersPool.submit((Runnable) motorSupplier);

        bodySupplier = new BodySupplier(bodyWarehouse, 4000, this);
        suppliersPool.submit((Runnable) bodySupplier);

        for (int i = 0; i < workerCount; ++i){
            workerPool.submit(new Worker(bodyWarehouse, motorWarehouse, accessoryWarehouse, autoWarehouse, 3000));
        }
        for (int i = 0; i < dealerCount; ++i){
            dealersPool.submit(new Dealer(autoWarehouse, 3000));
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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        notifyListener();
    }

    private void notifyListener() {
        if (listener != null) {
            listener.onModelChanged();
        }
    }
    @Override
    public void setListener(ModelListener listener) {
        this.listener = listener;
    }

    @Override
    public List<Supplier> getAccessoriesSupplierList() {
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


//    @Override
//    public Worker getWorker() {
//        return wo;
//    }

//    @Override
//    public Dealer getDealer() {
//        return null;
//    }

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
}
