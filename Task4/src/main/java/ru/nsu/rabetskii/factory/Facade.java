package ru.nsu.rabetskii.factory;

import ru.nsu.rabetskii.dealer.Dealer;
import ru.nsu.rabetskii.patternobserver.Observer;
import ru.nsu.rabetskii.supplier.AccessorySupplier;
import ru.nsu.rabetskii.supplier.Supplier;
import ru.nsu.rabetskii.warehouse.Warehouse;
import ru.nsu.rabetskii.worker.Worker;

import java.util.List;
import java.util.concurrent.ExecutorService;

public interface Facade {
    List<AccessorySupplier> getAccessoriesSupplierList();
    List<Dealer> getDealerList();
    List<Worker> getWorkerList();
    Supplier getMotorSupplier();
    Supplier getBodySupplier();
    Warehouse getAccessoryWarehouse();
    Warehouse getBodyWarehouse();
    Warehouse getMotorWarehouse();
    Warehouse getAutoWarehouse();
    ExecutorService getSuppliersPool();
    ExecutorService getWorkersPool();
    ExecutorService getDealersPool();

//    CustomThreadPool getSuppliersPool();
//    CustomThreadPool getWorkersPool();
//    CustomThreadPool getDealersPool();
    void setObservers(Observer observer);

}
