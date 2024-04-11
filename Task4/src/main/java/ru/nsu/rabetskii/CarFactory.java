package ru.nsu.rabetskii;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarFactory {
    private Warehouse bodyWarehouse;
    private Warehouse motorWarehouse;
    private Warehouse accessoryWarehouse;
    private Warehouse autoWarehouse;
    private ExecutorService workerPool;

    public CarFactory(){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        bodyWarehouse = new BaseWarehouse(Integer.parseInt(properties.getProperty("StorageBodySize")));
        motorWarehouse = new BaseWarehouse(Integer.parseInt(properties.getProperty("StorageMotorSize")));
        accessoryWarehouse = new BaseWarehouse(Integer.parseInt(properties.getProperty("StorageAccessorySize")));
        autoWarehouse = new BaseWarehouse(Integer.parseInt(properties.getProperty("StorageAutoSize")));

        int accessorySuppliers = Integer.parseInt(properties.getProperty("AccessorySuppliers"));
        int workers = Integer.parseInt(properties.getProperty("Workers"));
        int dealers = Integer.parseInt(properties.getProperty("Dealers"));
        boolean logSale = Boolean.parseBoolean(properties.getProperty("LogSale"));

        workerPool = Executors.newFixedThreadPool(workers);
        for (int i = 0; i < workers; i++) {
            workerPool.submit(new Worker(bodyWarehouse, motorWarehouse, accessoryWarehouse, autoWarehouse));
        }
    }

    public void createCar() {
        try {
            Component body = bodyWarehouse.getComponent();
            Component motor = motorWarehouse.getComponent();
            Component accessory = accessoryWarehouse.getComponent();
            Auto car = new Auto(body, motor, accessory);
            autoWarehouse.addComponent(car);
            System.out.println("Car created: " + car.toString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        workerPool.shutdownNow();
    }
}
