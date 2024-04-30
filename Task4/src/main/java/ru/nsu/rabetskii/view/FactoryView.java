package ru.nsu.rabetskii.view;

import ru.nsu.rabetskii.dealer.Dealer;
import ru.nsu.rabetskii.factory.CarFactory;
import ru.nsu.rabetskii.factory.Facade;
import ru.nsu.rabetskii.patternobserver.Observer;
import ru.nsu.rabetskii.supplier.Supplier;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class FactoryView extends JFrame implements ChangeListener, Observer {
    private final Facade factory;
    private final JLabel mainLabel;
    private final JTextArea motorText;
    private final JTextArea bodyText;
    private final JTextArea accessoryText;
    private final JTextArea dealerText;
    private final JTextArea autoInformation;

    private final JSlider motorSupplierSpeedSlider;
    private final JSlider bodySupplierSpeedSlider;
    private final JSlider accessorySupplierSpeedSlider;
    private final JSlider dealerSupplierSpeedSlider;

    public FactoryView(CarFactory model){
        this.factory = model;
        factory.setObservers(this);

        mainLabel = new JLabel();

        motorSupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, factory.getMotorSupplier().getSpeed() / 100 );
        bodySupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, factory.getBodySupplier().getSpeed() / 100);
        accessorySupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, factory.getAccessoriesSupplierList().getFirst().getSpeed() / 100);
        dealerSupplierSpeedSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, factory.getDealerList().getFirst().getSpeed() / 100);

        motorText = new JTextArea();
        settingJTextArea(motorText);
        motorText.setText("Motor Supplier Speed: " + factory.getMotorSupplier().getSpeed() + "ms"
                + "\nCurrent num: " + factory.getMotorWarehouse().getSize()
                + "\nNum produced: " + factory.getMotorWarehouse().getLastId());

        bodyText = new JTextArea();
        settingJTextArea(bodyText);
        bodyText.setText("Body Supplier Speed: " + factory.getBodySupplier().getSpeed() + "ms"
                + "\nCurrent num: " + factory.getBodyWarehouse().getSize()
                + "\nNum produced: " + factory.getBodyWarehouse().getLastId());

        accessoryText = new JTextArea();
        settingJTextArea(accessoryText);
        accessoryText.setText("Accessory Supplier Speed: " + factory.getAccessoriesSupplierList().getFirst().getSpeed() + "ms"
                + "\nCurrent num: " + factory.getAccessoryWarehouse().getSize()
                + "\nNum produced: " + factory.getAccessoryWarehouse().getLastId());

        dealerText = new JTextArea();
        settingJTextArea(dealerText);
        dealerText.setText("Dealer Speed: " + factory.getDealerList().getFirst().getSpeed() + "ms");

        autoInformation = new JTextArea();
        settingJTextArea(autoInformation);
        autoInformation.setText("Current num auto in the autoWarehouse " + factory.getAutoWarehouse().getSize()
                + "\nNum of cars produced: " + factory.getAutoWarehouse().getLastId()
                + "\n\nNum of active tasks: " + factory.getWorkerList().getFirst().getNumTasks());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        getContentPane().setBackground(Color.lightGray);
        setResizable(false);
        setVisible(true);

        mainLabel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 10, 30, 10);

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        mainLabel.add(motorText, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        mainLabel.add(motorSupplierSpeedSlider, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainLabel.add(accessoryText, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        mainLabel.add(accessorySupplierSpeedSlider, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainLabel.add(bodyText, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        mainLabel.add(bodySupplierSpeedSlider, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        mainLabel.add(dealerText, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        mainLabel.add(dealerSupplierSpeedSlider, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainLabel.add(autoInformation, gbc);

        add(mainLabel);

        motorSupplierSpeedSlider.addChangeListener(this);
        bodySupplierSpeedSlider.addChangeListener(this);
        accessorySupplierSpeedSlider.addChangeListener(this);
        dealerSupplierSpeedSlider.addChangeListener(this);

        addWindowListener(new MyWindowAdapter(factory));
    }

    private void settingJTextArea(JTextArea text){
        Font font = new Font("Arial", Font.BOLD, 13);
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setBackground(Color.lightGray);
        text.setFont(font);
        text.setFocusable(false);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == motorSupplierSpeedSlider){
            factory.getMotorSupplier().setSpeed(motorSupplierSpeedSlider.getValue() * 100);
        } else if (e.getSource() == bodySupplierSpeedSlider){
            factory.getBodySupplier().setSpeed(bodySupplierSpeedSlider.getValue() * 100);
        } else if (e.getSource() == accessorySupplierSpeedSlider){
            for (Supplier accessorySupplier : factory.getAccessoriesSupplierList()){
                accessorySupplier.setSpeed(accessorySupplierSpeedSlider.getValue() * 100);
            }
        } else if (e.getSource() == dealerSupplierSpeedSlider){
            for (Dealer dealer : factory.getDealerList()){
                dealer.setSpeed(dealerSupplierSpeedSlider.getValue() * 100);
            }
        }
    }

    @Override
    public void update() {
        SwingUtilities.invokeLater(() -> {
            accessoryText.setText("Accessory Supplier Speed: " + factory.getAccessoriesSupplierList().getFirst().getSpeed() + "ms"
                    + "\nCurrent num: " + factory.getAccessoryWarehouse().getSize()
                    + "\nNum produced: " + factory.getAccessoryWarehouse().getLastId());
            motorText.setText("Motor Supplier Speed: " + factory.getMotorSupplier().getSpeed() + "ms"
                    + "\nCurrent num: " + factory.getMotorWarehouse().getSize()
                    + "\nNum produced: " + factory.getMotorWarehouse().getLastId());
            bodyText.setText("Body Supplier Speed: " + factory.getBodySupplier().getSpeed() + "ms"
                    + "\nCurrent num: " + factory.getBodyWarehouse().getSize()
                    + "\nNum produced: " + factory.getBodyWarehouse().getLastId());
            dealerText.setText("Dealer Speed: " + factory.getDealerList().getFirst().getSpeed() + "ms");

            autoInformation.setText("Current num auto in the autoWarehouse " + factory.getAutoWarehouse().getSize()
                    + "\nNum of cars produced: " + factory.getAutoWarehouse().getLastId()
                    + "\n\nNum of active tasks: " + factory.getWorkerList().getFirst().getNumTasks());

        });
    }
}
