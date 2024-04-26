package ru.nsu.rabetskii;

import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.dealer.Dealer;
import ru.nsu.rabetskii.supplier.*;
import ru.nsu.rabetskii.warehouse.BaseWarehouse;
import ru.nsu.rabetskii.warehouse.Warehouse;
import ru.nsu.rabetskii.worker.Worker;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarFactory {
    private Warehouse bodyWarehouse;
    private Warehouse motorWarehouse;
    private Warehouse accessoryWarehouse;
    private Warehouse autoWarehouse;

    private final ExecutorService suppliersPool;
    private final ExecutorService workerPool;
    private ExecutorService dealersPool;
//    private Controller controller;

    private int accessorySupplierCount;
    private int workerCount;
    private int dealerCount;

    public CarFactory() {

        readConfig();

        suppliersPool = Executors.newFixedThreadPool(accessorySupplierCount + 2);
        workerPool = Executors.newFixedThreadPool(workerCount);
        dealersPool =Executors.newFixedThreadPool(dealerCount);

        for (int i = 0; i < accessorySupplierCount; i++) {
            suppliersPool.submit(new AccessorySupplier(accessoryWarehouse, 3000));
        }
        suppliersPool.submit(new MotorSupplier(motorWarehouse, 3000));
        suppliersPool.submit(new BodySupplier(bodyWarehouse, 3000));

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
}
