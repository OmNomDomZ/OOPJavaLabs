package ru.nsu.rabetskii;

import ru.nsu.rabetskii.supplier.AccessorySupplier;
import ru.nsu.rabetskii.supplier.Supplier;
import ru.nsu.rabetskii.warehouse.Warehouse;
import ru.nsu.rabetskii.worker.Worker;

import java.util.List;
import java.util.concurrent.ExecutorService;

public interface Facade {
    List<AccessorySupplier> getAccessoriesSupplierList();
    Supplier getMotorSupplier();
    Supplier getBodySupplier();
    Warehouse getAccessoryWarehouse();
    Warehouse getBodyWarehouse();
    Warehouse getMotorWarehouse();
    Warehouse getAutoWarehouse();
}
