package ru.nsu.rabetskii.supplier;

import ru.nsu.rabetskii.ModelListener;
import ru.nsu.rabetskii.ModelObservable;
import ru.nsu.rabetskii.component.AccessoryComponent;
import ru.nsu.rabetskii.component.Component;
import ru.nsu.rabetskii.warehouse.Warehouse;

public class AccessorySupplier extends BaseSupplier implements Runnable, ModelListener {
    private Warehouse warehouse;
    private int speed;
    private ModelObservable modelObservable;

    public AccessorySupplier(Warehouse warehouse, int speed, ModelObservable modelObservable) {
        this.warehouse = warehouse;
        this.speed = speed;
        this.modelObservable = modelObservable;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(speed);
                Component component = new AccessoryComponent();
                warehouse.addComponent(component);
                onModelChanged();
                System.out.println("Accessory #" + component.getId());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onModelChanged() {
        modelObservable.notifyObservers();
    }
}