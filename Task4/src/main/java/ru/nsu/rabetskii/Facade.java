package ru.nsu.rabetskii;

import ru.nsu.rabetskii.dealer.Dealer;
import ru.nsu.rabetskii.supplier.Supplier;
import ru.nsu.rabetskii.warehouse.Warehouse;
import ru.nsu.rabetskii.worker.Worker;

import java.util.List;

public interface Facade {
    List<Supplier> getAccessoriesSupplierList();
    Supplier getMotorSupplier();
    Supplier getBodySupplier();
//    Worker getWorker();
//    Dealer getDealer();
    Warehouse getAccessoryWarehouse();
    Warehouse getBodyWarehouse();
    Warehouse getMotorWarehouse();
    Warehouse getAutoWarehouse();
    void setListener(ModelListener modelListener);
}
