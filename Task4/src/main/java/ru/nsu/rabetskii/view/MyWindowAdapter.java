package ru.nsu.rabetskii.view;

import ru.nsu.rabetskii.dealer.Dealer;
import ru.nsu.rabetskii.factory.Facade;
import ru.nsu.rabetskii.supplier.Supplier;
import ru.nsu.rabetskii.worker.Worker;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyWindowAdapter extends WindowAdapter {
    Facade factory;
    public MyWindowAdapter(Facade factory){ this.factory = factory;}

    @Override
    public void windowClosing(WindowEvent e){
        for (Worker worker : factory.getWorkerList()){
            worker.changeIsRunning();
        }
        for (Dealer dealer : factory.getDealerList()){
            dealer.changeIsRunning();
        }
        for (Supplier accessorySupplier : factory.getAccessoriesSupplierList()){
            accessorySupplier.changeIsRunning();
        }
        factory.getBodySupplier().changeIsRunning();
        factory.getMotorSupplier().changeIsRunning();

        factory.getSuppliersPool().shutdown();
        factory.getDealersPool().shutdown();
        factory.getWorkersPool().shutdown();
    }
}
