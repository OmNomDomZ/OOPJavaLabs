package ru.nsu.rabetskii;

import ru.nsu.rabetskii.factory.CarFactory;
import ru.nsu.rabetskii.view.FactoryView;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FactoryView(new CarFactory()));
    }
}
