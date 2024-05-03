package ru.nsu.rabetskii.view;

import ru.nsu.rabetskii.dealer.Dealer;
import ru.nsu.rabetskii.factory.Facade;
import ru.nsu.rabetskii.supplier.Supplier;
import ru.nsu.rabetskii.worker.Worker;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;

public class MyWindowAdapter extends WindowAdapter {
    Facade factory;
    public MyWindowAdapter(Facade factory){ this.factory = factory;}

    @Override
    public void windowClosing(WindowEvent e){
        for (Dealer dealer : factory.getDealerList()){
            dealer.changeIsRunning();
        }
        for (Worker worker : factory.getWorkerList()){
            worker.changeIsRunning();
        }
        for (Supplier accessorySupplier : factory.getAccessoriesSupplierList()){
            accessorySupplier.changeIsRunning();
        }
        factory.getBodySupplier().changeIsRunning();
        factory.getMotorSupplier().changeIsRunning();

        factory.getDealersPool().shutdown();
        factory.getWorkersPool().shutdown();
        factory.getSuppliersPool().shutdown();

        try {
            if (!factory.getSuppliersPool().awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("Suppliers didn't terminate");
                factory.getSuppliersPool().shutdownNow();
            }
            if (!factory.getWorkersPool().awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("Workers didn't terminate");
                factory.getWorkersPool().shutdownNow();
            }
            if (!factory.getDealersPool().awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("Dealers didn't terminate");
                factory.getDealersPool().shutdownNow();
            }
        } catch (InterruptedException ex) {
            System.out.println("failed to terminate");
            factory.getSuppliersPool().shutdownNow();
            factory.getWorkersPool().shutdownNow();
            factory.getDealersPool().shutdownNow();
        }
    }

}
