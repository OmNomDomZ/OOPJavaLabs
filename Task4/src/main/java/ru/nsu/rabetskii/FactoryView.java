package ru.nsu.rabetskii;

import ru.nsu.rabetskii.supplier.Supplier;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class FactoryView extends JFrame implements ChangeListener, Observer {
    private final CarFactory factory;
    private final JLabel sliderLabel;
    private final JLabel informationLabel;
    private final JLabel motorLabel;
    private final JLabel motorCount;
    private final JLabel motorCountUI;
    private String motorCountString;
    private final JLabel bodyLabel;
    private final JLabel bodyCount;
    private final JLabel bodyCountUI;
    private String bodyCountString;
    private final JLabel autoCount;
    private final JLabel autoCountUI;
    private String autoCountString;
    private final JLabel accessoryLabel;
    private final JLabel accessoryCount;
    private final JLabel accessoryCountUI;
    private String accessoryCountString;
    private final JLabel dealerLabel;
    private final JSlider motorSupplierSpeedSlider;
    private final JSlider bodySupplierSpeedSlider;
    private final JSlider accessorySupplierSpeedSlider;
    private final JSlider dealerSupplierSpeedSlider;

    public FactoryView(CarFactory model){
        this.factory = model;
        factory.setObservers(this);

        sliderLabel = new JLabel();
        informationLabel = new JLabel();
        motorLabel = new JLabel("Motor Supplier Speed: " + factory.getMotorSupplier().getSpeed());
        motorCount = new JLabel("Num Motors at the moment: " + factory.getMotorWarehouse().getSize());
        motorCountString = repeatCharacter(factory.getMotorWarehouse().getSize());
        motorCountUI = new JLabel(motorCountString);
        bodyLabel = new JLabel("Body Supplier Speed: " + factory.getBodySupplier().getSpeed());
        bodyCount = new JLabel("Num bodies at the moment: " + factory.getBodyWarehouse().getSize());
        bodyCountString = repeatCharacter(factory.getBodyWarehouse().getSize());
        bodyCountUI = new JLabel(bodyCountString);
        accessoryLabel = new JLabel("Accessory Supplier Speed: " + factory.getAccessoriesSupplierList().getFirst().getSpeed());
        accessoryCount = new JLabel("Num accessories at the moment: " + factory.getAccessoryWarehouse().getSize());
        accessoryCountString = repeatCharacter(factory.getAccessoryWarehouse().getSize());
        accessoryCountUI = new JLabel(accessoryCountString);
        dealerLabel = new JLabel("Dealer Supplier Speed: ");
        autoCount = new JLabel("Num auto at the moment: " + factory.getAutoWarehouse().getSize());
        autoCountString = repeatCharacter(factory.getAutoWarehouse().getSize());
        autoCountUI = new JLabel(autoCountString);

        motorSupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, factory.getMotorSupplier().getSpeed() / 1000 );
        configureSlider(motorSupplierSpeedSlider);

        bodySupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, factory.getBodySupplier().getSpeed() / 1000);
        configureSlider(bodySupplierSpeedSlider);

        accessorySupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, factory.getAccessoriesSupplierList().getFirst().getSpeed() / 1000);
        configureSlider(accessorySupplierSpeedSlider);

        dealerSupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 0);
        configureSlider(dealerSupplierSpeedSlider);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        getContentPane().setBackground(Color.darkGray);
        setLayout(new GridLayout(1, 2));
        setResizable(false);
        setVisible(true);

        sliderLabel.setLayout(new GridLayout(4, 2, 10, 40));
        sliderLabel.setSize(600, 600);
        sliderLabel.setVisible(true);
        sliderLabel.setBackground(Color.lightGray);
        sliderLabel.setOpaque(true);
        sliderLabel.add(motorLabel);
        sliderLabel.add(motorSupplierSpeedSlider);
        sliderLabel.add(bodyLabel);
        sliderLabel.add(bodySupplierSpeedSlider);
        sliderLabel.add(accessoryLabel);
        sliderLabel.add(accessorySupplierSpeedSlider);
        sliderLabel.add(dealerLabel);
        sliderLabel.add(dealerSupplierSpeedSlider);

        informationLabel.setLayout(new GridLayout(4, 2, 10, 40));
        informationLabel.setSize(400, 600);
        informationLabel.setVisible(true);
        informationLabel.setBackground(Color.lightGray);
        informationLabel.setOpaque(true);
        informationLabel.add(motorCount);
        informationLabel.add(motorCountUI);
        informationLabel.add(bodyCount);
        informationLabel.add(bodyCountUI);
        informationLabel.add(accessoryCount);
        informationLabel.add(accessoryCountUI);
        informationLabel.add(autoCount);
        informationLabel.add(autoCountUI);

        add(sliderLabel);
        add(informationLabel);

        motorSupplierSpeedSlider.addChangeListener(this);
        bodySupplierSpeedSlider.addChangeListener(this);
        accessorySupplierSpeedSlider.addChangeListener(this);
    }

    private void configureSlider(JSlider slider) {
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == motorSupplierSpeedSlider){
            factory.getMotorSupplier().setSpeed(motorSupplierSpeedSlider.getValue() * 1000);
        } else if (e.getSource() == bodySupplierSpeedSlider){
            factory.getBodySupplier().setSpeed(bodySupplierSpeedSlider.getValue() * 1000);
        } else if (e.getSource() == accessorySupplierSpeedSlider){
            for (Supplier accessorySupplier : factory.getAccessoriesSupplierList()){
                accessorySupplier.setSpeed(accessorySupplierSpeedSlider.getValue() * 1000);
            }
        }
    }

    @Override
    public void update() {
        SwingUtilities.invokeLater(() -> {
            motorCountString = repeatCharacter(factory.getMotorWarehouse().getSize());
            motorCountUI.setText(motorCountString);
            bodyCountString = repeatCharacter(factory.getBodyWarehouse().getSize());
            bodyCountUI.setText(bodyCountString);
            accessoryCountString = repeatCharacter(factory.getAccessoryWarehouse().getSize());
            accessoryCountUI.setText(accessoryCountString);
            autoCountString = repeatCharacter(factory.getAutoWarehouse().getSize());
            autoCountUI.setText(autoCountString);

            autoCount.setText("Num auto at the moment: " + factory.getAutoWarehouse().getSize());
            motorCount.setText("Num Motors at the moment: " + factory.getMotorWarehouse().getSize());
            bodyCount.setText("Num bodies at the moment: " + factory.getBodyWarehouse().getSize());
            accessoryCount.setText("Num Accessories at the moment: " + factory.getAccessoryWarehouse().getSize());
        });
    }

    public String repeatCharacter(int count) {
        return "#".repeat(Math.max(0, count));
    }
}
