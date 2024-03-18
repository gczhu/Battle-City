package com.zgcprog.Tank_Game;

import java.awt.*;
import java.util.Vector;

/**
 * @author 祝广程
 * @version 1.0
 */
public class Tank {
    // 坦克的坐标
    private int x;
    private int y;

    // 坦克的移动速度
    private int speed;

    // 坦克的方向(0:向上 1:向右 2:向下 3:向左)
    private int direct;

    // 坦克的血量
    private int HP;

    // 坦克的移动范围
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    final int width = screenSize.width - screenSize.width / 3;
    final int height = screenSize.height - screenSize.height / 3;

    // 定义一个Vector集合存放坦克的子弹
    Vector<Shot> shots = new Vector<>();

    // 坦克移动的方法
    public void moveUp() {
        if (y - speed > 10) {
            y -= speed;
        } else {
            y = 10;
        }
    }

    public void moveRight() {
        if (x + speed + 60 < width) {
            x += speed;
        } else {
            x = width - 58;
        }
    }

    public void moveDown() {
        if (y + speed + 60 < height) {
            y += speed;
        } else {
            y = height - 52;
        }
    }

    public void moveLeft() {
        if (x - speed > 10) {
            x -= speed;
        } else {
            x = 10;
        }
    }

    public Tank(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

}
