package com.zgcprog.Tank_Game;

import java.util.Date;
import java.util.Vector;

/**
 * @author 祝广程
 * @version 1.0
 */
public class EnemyTank extends Tank implements Runnable {
    // 攻击间隔
    int attackGap = 150;
    // 移动间隔(默认50ms)
    int moveGap = 50;
    // 除此坦克外的其他坦克
    Vector<Tank> otherTanks = new Vector<Tank>();

    public EnemyTank(int x, int y, int direct) {
        super(x, y, direct);
        // 设置敌方坦克的血量为1
        super.setHP(1);
        // 设置敌方坦克的移动速度
        // 初始移动速度为2，每5关移动速度加1，最高为8
        int nowSpeed = 2 + Recorder.getLevel() / 5;
        if (nowSpeed > 8) {
            nowSpeed = 8;
        }
        super.setSpeed(nowSpeed);
        // 设置敌方坦克的攻击间隔
        // 初始攻击间隔为150，每10关攻击间隔减50，最低为50
        attackGap = 150 - 50 * (Recorder.getLevel() / 10);
        if (attackGap < 50) {
            attackGap = 50;
        }
        // 设置敌方坦克的HP
        // 初始HP为1，每10关HP加1，最高为3
        int HP_upd = 1 + Recorder.getLevel() / 10;
        if (HP_upd > 3) {
            HP_upd = 3;
        }
        super.setHP(HP_upd);
    }

    // 生成一个子弹(发动一次攻击)
    public void attack() {
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
        shot.speed += Recorder.getLevel() / 5;
        if (shot.speed > 20) {
            shot.speed = 20;
        }
        new Thread(shot).start();
    }

    // 判断该敌方坦克是否与其他坦克重叠
    public boolean isOverlap() {
        for (int i = 0; i < otherTanks.size(); i++) {
            Tank otherTank = otherTanks.get(i);
            switch (this.getDirect()) {
                case 0:
                    if (otherTank.getDirect() == 0 || otherTank.getDirect() == 2) {
                        if (this.getX() >= otherTank.getX() && this.getX() <= otherTank.getX() + 40
                                && this.getY() >= otherTank.getY() && this.getY() <= otherTank.getY() + 60) {
                            return true;
                        } else if (this.getX() + 40 >= otherTank.getX() && this.getX() + 40 <= otherTank.getX() + 40
                                && this.getY() >= otherTank.getY() && this.getY() <= otherTank.getY() + 60) {
                            return true;
                        }
                    } else {
                        if (this.getX() >= otherTank.getX() && this.getX() <= otherTank.getX() + 60
                                && this.getY() >= otherTank.getY() && this.getY() <= otherTank.getY() + 40) {
                            return true;
                        } else if (this.getX() + 40 >= otherTank.getX() && this.getX() + 40 <= otherTank.getX() + 60
                                && this.getY() >= otherTank.getY() && this.getY() <= otherTank.getY() + 40) {
                            return true;
                        }
                    }
                    break;
                case 1:
                    if (otherTank.getDirect() == 0 || otherTank.getDirect() == 2) {
                        if (this.getX() + 60 >= otherTank.getX() && this.getX() + 60 <= otherTank.getX() + 40
                                && this.getY() >= otherTank.getY() && this.getY() <= otherTank.getY() + 60) {
                            return true;
                        } else if (this.getX() + 60 >= otherTank.getX() && this.getX() + 60 <= otherTank.getX() + 40
                                && this.getY() + 40 >= otherTank.getY() && this.getY() + 40 <= otherTank.getY() + 60) {
                            return true;
                        }
                    } else {
                        if (this.getX() + 60 >= otherTank.getX() && this.getX() + 60 <= otherTank.getX() + 60
                                && this.getY() >= otherTank.getY() && this.getY() <= otherTank.getY() + 40) {
                            return true;
                        } else if (this.getX() + 60 >= otherTank.getX() && this.getX() + 60 <= otherTank.getX() + 60
                                && this.getY() + 40 >= otherTank.getY() && this.getY() + 40 <= otherTank.getY() + 40) {
                            return true;
                        }
                    }
                    break;
                case 2:
                    if (otherTank.getDirect() == 0 || otherTank.getDirect() == 2) {
                        if (this.getX() >= otherTank.getX() && this.getX() <= otherTank.getX() + 40
                                && this.getY() + 60 >= otherTank.getY() && this.getY() + 60 <= otherTank.getY() + 60) {
                            return true;
                        } else if (this.getX() + 40 >= otherTank.getX() && this.getX() + 40 <= otherTank.getX() + 40
                                && this.getY() + 60 >= otherTank.getY() && this.getY() + 60 <= otherTank.getY() + 60) {
                            return true;
                        }
                    } else {
                        if (this.getX() >= otherTank.getX() && this.getX() <= otherTank.getX() + 60
                                && this.getY() + 60 >= otherTank.getY() && this.getY() + 60 <= otherTank.getY() + 40) {
                            return true;
                        } else if (this.getX() + 40 >= otherTank.getX() && this.getX() + 40 <= otherTank.getX() + 60
                                && this.getY() + 60 >= otherTank.getY() && this.getY() + 60 <= otherTank.getY() + 40) {
                            return true;
                        }
                    }
                    break;
                case 3:
                    if (otherTank.getDirect() == 0 || otherTank.getDirect() == 2) {
                        if (this.getX() >= otherTank.getX() && this.getX() <= otherTank.getX() + 40
                                && this.getY() >= otherTank.getY() && this.getY() <= otherTank.getY() + 60) {
                            return true;
                        } else if (this.getX() >= otherTank.getX() && this.getX() <= otherTank.getX() + 40
                                && this.getY() + 40 >= otherTank.getY() && this.getY() + 40 <= otherTank.getY() + 60) {
                            return true;
                        }
                    } else {
                        if (this.getX() >= otherTank.getX() && this.getX() <= otherTank.getX() + 60
                                && this.getY() >= otherTank.getY() && this.getY() <= otherTank.getY() + 40) {
                            return true;
                        } else if (this.getX() >= otherTank.getX() && this.getX() <= otherTank.getX() + 60
                                && this.getY() + 40 >= otherTank.getY() && this.getY() + 40 <= otherTank.getY() + 40) {
                            return true;
                        }
                    }
                    break;
            }
        }
        return false;
    }

    public void setOtherTanks(Vector<EnemyTank> enemyTanks, MyTank hero) {
        for (int i = 0; i < enemyTanks.size(); i++) {
            Tank otherTank = enemyTanks.get(i);
            if (otherTank != this) {
                otherTanks.add(otherTank);
            }
        }
        otherTanks.add(hero);
    }

    @Override
    public void run() {
        int totalTime = 0;
        int flag;           // flag用于标记当前坦克在向哪个方向移动的时候发生了重叠
        while (true) {
            flag = -1;
            switch (getDirect()) {
                case 0:
                    for (int i = 0; i < 40; i++) {
                        if (i == 20) {
                            // 每隔attackGap时间生成一个子弹(发动一次攻击)
                            if (totalTime % attackGap == 0) {
                                attack();
                            }
                        }
                        if (!isOverlap()) {
                            moveUp();
                        } else {
                            flag = 0;
                        }
                        // 每隔moveGap时间移动一次
                        try {
                            Thread.sleep(moveGap);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 1:
                    for (int i = 0; i < 40; i++) {
                        if (i == 20) {
                            // 每隔attackGap时间生成一个子弹(发动一次攻击)
                            if (totalTime % attackGap == 0) {
                                attack();
                            }
                        }
                        if (!isOverlap()) {
                            moveRight();
                        } else {
                            flag = 1;
                        }
                        try {
                            Thread.sleep(moveGap);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i < 40; i++) {
                        if (i == 20) {
                            // 每隔attackGap时间生成一个子弹(发动一次攻击)
                            if (totalTime % attackGap == 0) {
                                attack();
                            }
                        }
                        if (!isOverlap()) {
                            moveDown();
                        } else {
                            flag = 2;
                        }
                        try {
                            Thread.sleep(moveGap);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 3:
                    for (int i = 0; i < 40; i++) {
                        if (i == 20) {
                            // 每隔attackGap时间生成一个子弹(发动一次攻击)
                            if (totalTime % attackGap == 0) {
                                attack();
                            }
                        }
                        if (!isOverlap()) {
                            moveLeft();
                        } else {
                            flag = 3;
                        }
                        try {
                            Thread.sleep(moveGap);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
            }
            switch (flag) {
                case -1:    //如果没有发生重叠，随机改变坦克方向
                    setDirect((int) (Math.random() * 4));
                    break;
                case 0:
                    setDirect(2);
                    break;
                case 1:
                    setDirect(3);
                    break;
                case 2:
                    setDirect(0);
                    break;
                case 3:
                    setDirect(1);
                    break;
            }
            totalTime += 50;
            // 坦克生命值为0后退出线程
            if (this.getHP() <= 0) {
                break;
            }
        }
    }
}
