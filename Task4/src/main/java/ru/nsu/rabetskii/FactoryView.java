package ru.nsu.rabetskii;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class FactoryView extends JFrame implements ChangeListener {
    private CarFactory model;
    private JLabel mainLabel;
    private JLabel motorLabel;
    private JLabel bodyLabel;
    private JLabel accessoryLabel;
    private JLabel dealerLabel;
    private JSlider motorSupplierSpeedSlider;
    private JSlider bodySupplierSpeedSlider;
    private JSlider accessorySupplierSpeedSlider;
    private JSlider dealerSupplierSpeedSlider;

    public FactoryView(CarFactory model){
        this.model = model;

        mainLabel = new JLabel();
        motorLabel = new JLabel("Motor Supplier Speed:");
        bodyLabel = new JLabel("Body Supplier Speed:");
        accessoryLabel = new JLabel("Accessory Supplier Speed:");
        dealerLabel = new JLabel("Dealer Supplier Speed:");

        motorSupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        configureSlider(motorSupplierSpeedSlider);

        bodySupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        configureSlider(bodySupplierSpeedSlider);

        accessorySupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        configureSlider(accessorySupplierSpeedSlider);

        dealerSupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        configureSlider(dealerSupplierSpeedSlider);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        getContentPane().setBackground(Color.lightGray);
        setLayout(new GridLayout(4, 2, 10, 40));
        setResizable(false);

        add(motorLabel);
        add(motorSupplierSpeedSlider);
        add(bodyLabel);
        add(bodySupplierSpeedSlider);
        add(accessoryLabel);
        add(accessorySupplierSpeedSlider);
        add(dealerLabel);
        add(dealerSupplierSpeedSlider);

        setVisible(true);
    }

    private void configureSlider(JSlider slider) {
        slider.setMajorTickSpacing(50);
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }
}
