package ru.nsu.rabetskii;

import ru.nsu.rabetskii.supplier.Supplier;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class FactoryView extends JFrame implements ChangeListener, ModelListener {
    private Facade factory;
    private JLabel sliderLabel;
    private JLabel informationLabel;
    private JLabel motorLabel;
    private JLabel motorCount;
    private JLabel motorCountUI;
    private String motorCountString;
    private JLabel bodyLabel;
    private JLabel bodyCount;
    private JLabel bodyCountUI;
    private String bodyCountString;
    private JLabel accessoryLabel;
    private JLabel accessoryCount;
    private JLabel accessoryCountUI;
    private String accessoryCountString;
    private JLabel dealerLabel;
    private JSlider motorSupplierSpeedSlider;
    private JSlider bodySupplierSpeedSlider;
    private JSlider accessorySupplierSpeedSlider;
    private JSlider dealerSupplierSpeedSlider;

    public FactoryView(CarFactory model){
        this.factory = model;
        factory.setListener(this);

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

        motorSupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, factory.getMotorSupplier().getSpeed() / 1000);
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

    public String repeatCharacter(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append('#');
        }
        return sb.toString();
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
    public void onModelChanged() {
        motorCountString = repeatCharacter(factory.getMotorWarehouse().getSize());
        motorCountUI.setText(motorCountString);
        bodyCountString = repeatCharacter(factory.getBodyWarehouse().getSize());
        bodyCountUI.setText(accessoryCountString);
        accessoryCountString = repeatCharacter(factory.getAccessoryWarehouse().getSize());
        accessoryCountUI.setText(accessoryCountString);

        motorCount.setText("Num Motors at the moment: " + factory.getMotorWarehouse().getSize());
        bodyCount.setText("Num Bodies at the moment: " + factory.getBodyWarehouse().getSize());
        accessoryCount.setText("Num Accessories at the moment: " + factory.getAccessoryWarehouse().getSize());

    }
}