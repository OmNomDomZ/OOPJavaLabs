package ru.nsu.rabetskii.model;

import java.awt.*;

public interface GameObject {
    Point getPoint();
    Rectangle getBounds();
    void updateGameState();
    void getDamage();
    boolean collidesWith(GameObject object);
    int getWidth();
    int getHeight();
    void setOnGround(boolean status);
    boolean getOnGround();
}
