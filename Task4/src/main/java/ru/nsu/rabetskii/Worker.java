//package ru.nsu.rabetskii;
//
//import ru.nsu.rabetskii.warehouse.Warehouse;
//
//class Worker implements Runnable {
//    private Warehouse bodyWarehouse;
//    private Warehouse motorWarehouse;
//    private Warehouse accessoryWarehouse;
//    private Warehouse autoWarehouse;
//
//    public Worker(Warehouse bodyWarehouse, Warehouse motorWarehouse, Warehouse accessoryWarehouse, Warehouse autoWarehouse) {
//        this.bodyWarehouse = bodyWarehouse;
//        this.motorWarehouse = motorWarehouse;
//        this.accessoryWarehouse = accessoryWarehouse;
//        this.autoWarehouse = autoWarehouse;
//    }
//
//    @Override
//    public void run() {
//        while (!Thread.currentThread().isInterrupted()) {
//            try {
//                Component body = bodyWarehouse.getComponent();
//                Component motor = motorWarehouse.getComponent();
//                Component accessory = accessoryWarehouse.getComponent();
//
//                Auto car = new Auto(body, motor, accessory);
//                autoWarehouse.addComponent(car);
//
//                System.out.println("Worker assembled car: " + car.getId());
//
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//    }
//}
