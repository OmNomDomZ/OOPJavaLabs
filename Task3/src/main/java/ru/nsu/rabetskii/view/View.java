package ru.nsu.rabetskii.view;

import ru.nsu.rabetskii.controller.Controller;
import ru.nsu.rabetskii.model.GameObject;
import ru.nsu.rabetskii.model.Model;
import ru.nsu.rabetskii.model.ModelListener;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame implements ModelListener {
    private Model model;
    private Controller controller;
    private JLabel playerLabel;
    private JLabel mainLabel;
    private int startX = 0;
    private int startY = 0;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public View(Model model){
        this.model = model;
        controller = new Controller(model);

        mainLabel = new JLabel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(screenSize.width, screenSize.height);
        getContentPane().setBackground(Color.darkGray);
        setLayout(null);
        setResizable(false);

        playerLabel = new JLabel();
        ImageIcon playerIcon = new ImageIcon(getClass().getResource("/defaultPlayer.png"));
        playerLabel.setIcon(playerIcon);
        playerLabel.setBounds(model.getPlayer().getX(), model.getPlayer().getY(),
                                model.getPlayer().getWidth(), model.getPlayer().getHeight());


        ImageIcon groundIcon = new ImageIcon(getClass().getResource("/ground.png"));
        for (GameObject ground : model.getGround()){
            for (int i = 0; i < ground.getWidth(); i += groundIcon.getIconWidth()){
                JLabel tempLabel = new JLabel();
                tempLabel.setIcon(groundIcon);
                tempLabel.setBounds(ground.getX() + i, ground.getY(),
                        groundIcon.getIconWidth(), ground.getHeight());
                mainLabel.add(tempLabel);
            }
        }

        mainLabel.add(playerLabel);
        this.addKeyListener(controller);
        this.setVisible(true);
        this.add(mainLabel);

        model.setListener(this);
    }



    @Override
    public void onModelChanged() {
        SwingUtilities.invokeLater(() -> {

            playerLabel.setBounds(model.getPlayer().getX(), model.getPlayer().getY(),
                    model.getPlayer().getWidth(), model.getPlayer().getHeight());

            // движение mainLabel
//            if (!model.getPlayer().getOnGround()) {
                if (model.getPlayer().getY() > (screenSize.height - startY) / 2) {
                        mainLabel.setBounds(startX, startY -= 2, screenSize.width, screenSize.height * 3);
                } else {
                    mainLabel.setBounds(startX, startY, screenSize.width, screenSize.height * 3);
                }
//            }



            // Удаление всех старых меток пуль
            Component[] components = mainLabel.getComponents();
            for (Component component : components) {
                if (component instanceof JLabel) {
                    String name = component.getName();
                    if (name != null && (name.equals("bullet") || name.equals("enemy"))) {
                        mainLabel.remove(component);
                    }
                }
            }

            // Создание новых меток пуль
            for (GameObject bullet : model.getBullet()) {
                JLabel bulletLabel = new JLabel();
                bulletLabel.setName("bullet"); // Устанавливаем имя метки, чтобы потом можно было их идентифицировать
                bulletLabel.setOpaque(true);
                bulletLabel.setBackground(Color.RED);
                bulletLabel.setBounds(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
                mainLabel.add(bulletLabel);
            }

            // Создание новых меток врагов
            for (GameObject enemy : model.getEnemies()){
                JLabel enemyLabel = new JLabel();
                enemyLabel.setName("enemy");
                ImageIcon enemyIcon = new ImageIcon(getClass().getResource("/enemy.png"));
                enemyLabel.setIcon(enemyIcon);
                enemyLabel.setBounds(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
                mainLabel.add(enemyLabel);
            }
                 repaint();
        });
    }
}
