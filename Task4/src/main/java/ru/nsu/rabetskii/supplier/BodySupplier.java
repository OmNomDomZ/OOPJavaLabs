//package ru.nsu.rabetskii.supplier;
//
//import ru.nsu.rabetskii.component.BaseComponent;
//import ru.nsu.rabetskii.warehouse.Warehouse;
//
//public class BodySupplier extends BaseSupplier implements Runnable{
//    public BodySupplier(Warehouse warehouse) {
//        super(warehouse);
//    }
//
//    @Override
////    public void run() {
//        while (true) {
//            try {
//                Thread.sleep(speed);
//                BaseComponent component = new BaseComponent();
//                synchronized (warehouse) {
//                    warehouse.addComponent(component);
//                    System.out.println("Accessory #" + component.getId());
//                }
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//}
