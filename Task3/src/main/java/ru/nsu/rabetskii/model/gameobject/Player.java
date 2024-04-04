package ru.nsu.rabetskii.model.gameobject;

import ru.nsu.rabetskii.model.Model;

import java.awt.*;
import java.util.List;

public class Player extends MyObject{
    private final double GRAVITY = 0.5;
    private final double MAX_FALL_SPEED = 9.0;
    private final int JUMP_HEIGHT = 80;
    private double fallSpeed;
    private boolean keySpacePressed;
    private List<GameObject> bullets;
    private int maxNumBullets;
    private int currentNumBullets;
    private String weapon;
    private Model model;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private enum Weapon {
        MACHINE_GUN,
        LASER,
    }
    public Player(List<GameObject> bullets, String weapon, Model model){
        maxNumBullets = 0;
        x = 770;
        y = 10;
        width = 30;
        height = 30;
        hp = 4;
        speed = 10;
        fallSpeed = 0;
        objectOnGround = false;
        this.bullets = bullets;
        this.weapon = weapon;
        switch (Weapon.valueOf(weapon)){
            case MACHINE_GUN:
                maxNumBullets = 5;
                break;
            case LASER:
                maxNumBullets = 3;
                break;
        }
        currentNumBullets = maxNumBullets;
        this.model = model;
    }

    public void shoot(){
        if (!objectOnGround && currentNumBullets != 0) {
            GameObject bullet = null;
            switch (Weapon.valueOf(weapon)){
                case MACHINE_GUN:
                    bullet = new MachineGun(x + width / 2, y, model);
                    break;
                case LASER:
                    bullet = new Laser(x + width / 2, y, screenSize.height * 3 - x + width, model);
                    break;
            }
            bullets.add(bullet);
            currentNumBullets--;
            fallSpeed = 0;
        }
    }

    public int getCurrentNumBullets() {
        return currentNumBullets;
    }

    public int getMaxNumBullets() {
        return maxNumBullets;
    }

    public void updateGameState() {
        if (movingLeft){
            x -= speed;
        } else if (movingRight){
            x += speed;
        }

        if (objectOnGround){
            fallSpeed = 0;
            currentNumBullets = maxNumBullets;
        } else{
            y += (int) fallSpeed;
            fallSpeed = fallSpeed < MAX_FALL_SPEED ? fallSpeed + GRAVITY : fallSpeed;
        }

        // удаляем пулю из списка
        bullets.removeIf(bullet -> bullet.getHp() == 0);
    }

    @Override
    public void getDamage() {
        --hp;
        y -= 150;
    }

    public void setKeySpacePressed(boolean keySpacePressed) {
        if (keySpacePressed && !this.keySpacePressed){
            this.keySpacePressed = true;
            if (objectOnGround){
                y -= JUMP_HEIGHT;
            } else
            if (currentNumBullets != 0){
                shoot();
            }
        } else {
            this.keySpacePressed = keySpacePressed;
        }
    }
}