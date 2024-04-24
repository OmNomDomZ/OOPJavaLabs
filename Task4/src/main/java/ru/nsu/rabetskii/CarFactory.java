package ru.nsu.rabetskii;

import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.supplier.BaseSupplier;
import ru.nsu.rabetskii.supplier.Supplier;
import ru.nsu.rabetskii.warehouse.BaseWarehouse;
import ru.nsu.rabetskii.warehouse.Warehouse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarFactory {
    private final Warehouse bodyWarehouse;
    private final Warehouse motorWarehouse;
    private final Warehouse accessoryWarehouse;
//    private final Warehouse autoWarehouse;

//    private final ExecutorService workerPool;
    private final ExecutorService suppliersPool;
//    private final ExecutorService dealersPool;
//    private Controller controller;

    private int accessorySupplierCount;
    private int bodySupplierCount;
    private int motorSupplierCount;

    public CarFactory() throws IOException {


        bodyWarehouse = new BaseWarehouse();
        motorWarehouse = new BaseWarehouse();
        accessoryWarehouse = new BaseWarehouse();

        readConfig();
//        autoWarehouse = new BaseWarehouse();

//        workerPool = Executors.newFixedThreadPool(workers);
        suppliersPool = Executors.newFixedThreadPool(accessorySupplierCount + 10);
//        dealersPool = Executors.newFixedThreadPool(dealers);


        for (int i = 0; i < accessorySupplierCount; i++) {
            suppliersPool.submit(new BaseSupplier(accessoryWarehouse, 1000));
        }
        for (int i = 0; i < motorSupplierCount; i++) {
            suppliersPool.submit(new BaseSupplier(motorWarehouse, 1000));
        }
        for (int i = 0; i < bodySupplierCount; i++) {
            suppliersPool.submit(new BaseSupplier(bodyWarehouse, 1000));
        }

//        suppliersPool = Executors.newFixedThreadPool(accessorySuppliers);
//        dealersPool = Executors.newFixedThreadPool(dealers);



//        controller = new Controller(this, autoWarehouse, bodyWarehouse, motorWarehouse, accessoryWarehouse, workerPool);

//        new Thread(controller).start();
//        startSuppliers(accessorySuppliers);
//        startDealers(dealers);
    }

    private void readConfig() throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/config.properties"));

            bodyWarehouse.Constructor(Integer.parseInt(properties.getProperty("BodyWarehouseSize")));
            accessoryWarehouse.Constructor(Integer.parseInt(properties.getProperty("AccessoryWarehouseSize")));
            motorWarehouse.Constructor(Integer.parseInt(properties.getProperty("MotorWarehouseSize")));

            accessorySupplierCount = Integer.parseInt(properties.getProperty("AccessorySupplierSize"));
            motorSupplierCount = Integer.parseInt(properties.getProperty("MotorSupplierSize"));
            bodySupplierCount = Integer.parseInt(properties.getProperty("BodySupplierSize"));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


//    private void startSuppliers(int numSuppliers) {
//        for (int i = 0; i < numSuppliers; i++) {
//            suppliersPool.submit(new Supplier(accessoryWarehouse, 10));
//        }
//    }
//
//    private void startDealers(int numDealers) {
//        for (int i = 0; i < numDealers; i++) {
//            dealersPool.submit(new Dealer(autoWarehouse, 20));
//        }
//    }
//
//    public void shutdown() {
//        workerPool.shutdownNow();
//        suppliersPool.shutdownNow();
//        dealersPool.shutdownNow();
////        controller.shutdown();
//    }
//
//    public Auto createCar() throws InterruptedException {
//        Component body = bodyWarehouse.getComponent();
//        Component motor = motorWarehouse.getComponent();
//        Component accessory = accessoryWarehouse.getComponent();
//
//        Auto car = new Auto(body, motor, accessory);
//        if (!autoWarehouse.isFull()) {
//            autoWarehouse.addComponent(car);
//            System.out.println("CarFactory assembled: " + car);
//        }
//
//        return car;
//    }
}
