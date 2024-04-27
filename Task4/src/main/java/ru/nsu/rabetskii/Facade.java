package ru.nsu.rabetskii;

import ru.nsu.rabetskii.supplier.Supplier;
import ru.nsu.rabetskii.warehouse.Warehouse;
import ru.nsu.rabetskii.worker.Worker;

import java.util.List;

public interface Facade {
    List<Supplier> getAccessoriesSupplierList();
    Supplier getMotorSupplier();
    Supplier getBodySupplier();
    List<Worker> getWorkers();
//    Dealer getDealer();
    Warehouse getAccessoryWarehouse();
    Warehouse getBodyWarehouse();
    Warehouse getMotorWarehouse();
    Warehouse getAutoWarehouse();
}
