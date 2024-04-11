package ru.nsu.rabetskii;

class Supplier implements Runnable {
    protected Warehouse accessoryWarehouse;
    protected final int productionSpeed;

    public Supplier(Warehouse warehouse, int productionSpeed) {
        this.accessoryWarehouse = warehouse;
        this.productionSpeed = productionSpeed;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(productionSpeed);
                Component component = new BaseComponent();
                synchronized (accessoryWarehouse) {
                    if (accessoryWarehouse.addComponent(component)) {
                        System.out.println("Supplier produced: " + component.getId());
                        accessoryWarehouse.notifyAll();
                    } else {
                        accessoryWarehouse.wait();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}