package ru.nsu.rabetskii;

class Dealer implements Runnable {
    private Warehouse autoWarehouse;
    private int requestSpeed;

    public Dealer(Warehouse autoWarehouse, int requestSpeed) {
        this.autoWarehouse = autoWarehouse;
        this.requestSpeed = requestSpeed;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Component car = autoWarehouse.getComponent();
                System.out.println("Dealer sold car: " + car.getId());
                Thread.sleep(requestSpeed);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}