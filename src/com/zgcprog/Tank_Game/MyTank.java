package com.zgcprog.Tank_Game;

import java.util.Date;

/**
 * @author 祝广程
 * @version 1.0
 */
public class MyTank extends Tank {
    // 定义一个boolean值检测攻击是否准备好，避免高频率连发
    boolean isReady = true;
    // 定义两个变量记录最近两次攻击的时间
    static Date last_time = new Date();
    static Date this_time = new Date();
    // 定义一个变量记录坦克的剩余弹药数(初始为5)
    int bulletNum = 5;

    public MyTank(int x, int y, int direct) {
        super(x, y, direct);
        // 设置我方坦克的初始血量为5
        super.setHP(5);
        // 设置我方坦克的初始移动速度为4
        super.setSpeed(4);
    }

    public void shotEnemyTank() {
        last_time = this_time;
        this_time = new Date();
        double gap = this_time.getTime() - last_time.getTime();
        if ((last_time == null || gap >= 150) && isReady == false) {
            isReady = true;
        }
        if (isReady && bulletNum > 0) {
            Shot shot = null;
            switch (getDirect()) {
                case 0:
                    shot = new Shot(getX() + 20, getY(), 0);
                    break;
                case 1:
                    shot = new Shot(getX() + 60, getY() + 20, 1);
                    break;
                case 2:
                    shot = new Shot(getX() + 20, getY() + 60, 2);
                    break;
                case 3:
                    shot = new Shot(getX(), getY() + 20, 3);
                    break;
            }
            shots.add(shot);
            new Thread(shot).start();
            isReady = false;
            bulletNum--;
        }
    }
}
