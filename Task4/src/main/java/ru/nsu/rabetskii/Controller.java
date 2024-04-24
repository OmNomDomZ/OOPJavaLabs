//package ru.nsu.rabetskii;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class Controller implements Runnable {
//    private Warehouse autoWarehouse;
//    private Warehouse bodyWarehouse;
//    private Warehouse motorWarehouse;
//    private Warehouse accessoryWarehouse;
//    private ExecutorService workerPool;
//    private CarFactory carFactory;
//    private boolean logSale;
//
//    public Controller(CarFactory carFactory, Warehouse autoWarehouse, Warehouse bodyWarehouse, Warehouse motorWarehouse, Warehouse accessoryWarehouse, ExecutorService workerPool, boolean logSale) {
//        this.carFactory = carFactory;
//        this.autoWarehouse = autoWarehouse;
//        this.bodyWarehouse = bodyWarehouse;
//        this.motorWarehouse = motorWarehouse;
//        this.accessoryWarehouse = accessoryWarehouse;
//        this.workerPool = workerPool;
//        this.logSale = logSale;
//    }
//
//    @Override
//    public void run() {
//        while (!Thread.currentThread().isInterrupted()) {
//            try {
//                if (!autoWarehouse.isFull()) {
//                    requestNewCar();
//                }
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//    }
//
//    private void requestNewCar() {
//        workerPool.submit(() -> {
//            try {
//                carFactory.createCar();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//
//    public void shutdown() {
//        workerPool.shutdownNow();
//    }
//}
