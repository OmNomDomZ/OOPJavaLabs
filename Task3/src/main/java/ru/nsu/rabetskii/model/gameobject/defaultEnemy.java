package ru.nsu.rabetskii.model.gameobject;

public class defaultEnemy extends MyObject{
    public defaultEnemy(int x, int y){
        this.x = x;
        this.y = y;
        hp = 1;
        width = 25;
        height = 25;
        speed = -5;
    }

    public void updateGameState() {
        x += speed;
    }

    @Override
    public void getDamage() {
        hp--;
    }

}