package com.zgcprog.Tank_Game;

/**
 * @author 祝广程
 * @version 1.0
 */
public class Boom {
    int x, y;    // 爆炸处坐标
    int life = 9;   // 炸弹的生命周期

    public Boom(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void lifeDown() {
        if (life > 0) {
            life--;
        }
    }
}
