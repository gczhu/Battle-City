package com.zgcprog.Tank_Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.Vector;

/**
 * @author 祝广程
 * @version 1.0
 * 坦克大战面板(绘图区域)
 */
public class GamePanel extends JPanel implements KeyListener, Runnable {
    // 定义一个自己的坦克
    MyTank hero = null;
    // 定义一个集合存放敌人的坦克
    Vector<EnemyTank> enemyTanks = new Vector<>();
    // 定义一个集合存放上局中敌人坦克的位置信息
    Vector<Node> nodes = new Vector<>();
    // 定义一个集合存放爆炸
    Vector<Boom> booms = new Vector<>();
    int enemyTankNum = 3;
    // 获取屏幕大小
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    final int screenWidth = screenSize.width;
    final int screenHeight = screenSize.height;
    // 定义游戏界面的大小为屏幕大小的2/3
    final int width = screenWidth - screenWidth / 3;
    final int height = screenHeight - screenHeight / 3;

    // 定义游戏界面的开始坐标
    final int startX = 0;
    final int startY = 0;
    // 定义三张图片以显示爆炸效果
    Image img1_boom = null;
    Image img2_boom = null;
    Image img3_boom = null;

    public GamePanel() {
        if (TankGame.mode == 1) {
            // 初始化我方坦克
            hero = new MyTank(startX + width / 2, startY + height / 2, 0);
            // 初始化敌方坦克
            for (int i = 1; i <= enemyTankNum; i++) {
                EnemyTank enemyTank = new EnemyTank(startX + width / 100 + 100 * i, startY + 10, 2);
                enemyTanks.add(enemyTank);
                new Thread(enemyTank).start();
            }
            new AePlayWave("src\\start.wav").start();
        } else {
            File recordFile = new File(Recorder.getRecordFile());
            // 如果不存在上局游戏记录，则默认创建新游戏
            if (!recordFile.exists()) {
                // 初始化我方坦克
                hero = new MyTank(startX + width / 2, startY + height / 2, 0);
                // 初始化敌方坦克
                for (int i = 1; i <= enemyTankNum; i++) {
                    EnemyTank enemyTank = new EnemyTank(startX + width / 100 + 100 * i, startY + 10, 2);
                    enemyTanks.add(enemyTank);
                    new Thread(enemyTank).start();
                }
            } else {
                nodes = Recorder.readInfo();
                enemyTankNum = Recorder.getEnemyTankNum();
                Node node = nodes.get(0);
                hero = new MyTank(node.getX(), node.getY(), node.getDirect());
                hero.setHP(Recorder.getHP());
                for (int i = 1; i < nodes.size(); i++) {
                    node = nodes.get(i);
                    EnemyTank enemyTank = new EnemyTank(node.getX(), node.getY(), node.getDirect());
                    enemyTanks.add(enemyTank);
                    if (hero.getHP() > 0) {
                        new Thread(enemyTank).start();
                    }
                }
            }
        }
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            enemyTank.setOtherTanks(enemyTanks, hero);
        }
        // 初始化爆炸图片
        img1_boom = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/Boom1.png"));
        img2_boom = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/Boom2.png"));
        img3_boom = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/Boom3.png"));
        //初始化Record信息
        Recorder.setEnemyTanks(enemyTanks);
        Recorder.setHero(hero);
        String scoreFile = "src\\highScore.txt";
        BufferedReader br = null;
        File file = new File(scoreFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Recorder.setHigh_score(0);
        } else {
            try {
                br = new BufferedReader(new FileReader(scoreFile));
                String line = br.readLine();
                if (line != null) {
                    Recorder.setHigh_score(Integer.parseInt(line));
                } else {
                    Recorder.setHigh_score(0);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // 填充面板背景，默认是黑色
        g.fillRect(0, 0, screenWidth, screenHeight);
        // 设置画笔颜色为白色
        g.setColor(Color.WHITE);
        // 画出围墙
        drawWalls(g);
        // 获取我方坦克的横纵坐标
        int x = hero.getX();
        int y = hero.getY();
        int direct = hero.getDirect();
        // 画出敌方坦克和射出的子弹
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            // 判断两坦克之间是否击中了对方
            isHit(enemyTank, hero);
            isHit(hero, enemyTank);
            // 如果敌方坦克HP耗尽，就从集合中移除，并在该处添加爆炸
            if (enemyTank.getHP() <= 0) {
                new AePlayWave("src\\boom.wav").start();
                enemyTanks.remove(i);
                Recorder.addScore();
                booms.add(new Boom(enemyTank.getX(), enemyTank.getY()));
            } else {
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirect(), 1);
            }
            g.setColor(Color.WHITE);
            for (int j = 0; j < enemyTank.shots.size(); j++) {
                Shot shot = enemyTank.shots.get(j);
                if (shot.isLive == true) {
                    g.fill3DRect(shot.x - 2, shot.y - 2, 4, 4, false);
                } else {
                    enemyTank.shots.remove(j);
                }
            }
        }
        // 画出我方坦克
        if (hero.getHP() > 0) {
            drawTank(x, y, g, direct, 0);
        } else {
            booms.add(new Boom(x, y));
        }
        // 画出我方射出的子弹
        g.setColor(Color.YELLOW);
        for (int j = 0; j < hero.shots.size(); j++) {
            Shot shot = hero.shots.get(j);
            if (shot.isLive == true) {
                g.fill3DRect(shot.x - 2, shot.y - 2, 4, 4, false);
            } else {
                hero.shots.remove(j);
                hero.bulletNum++;
            }
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 显示爆炸效果及音效
        for (int i = 0; i < booms.size(); i++) {
            // 取出炸弹
            Boom boom = booms.get(i);
            // 根据当前boom对象的life值画出相应的图片
            if (boom.life > 6) {
                g.drawImage(img1_boom, boom.x, boom.y, 60, 60, this);
            } else if (boom.life > 3) {
                g.drawImage(img2_boom, boom.x, boom.y, 60, 60, this);
            } else {
                g.drawImage(img3_boom, boom.x, boom.y, 60, 60, this);
            }
            // 让炸弹的生命值减少，如果为0，就从集合中删除
            boom.lifeDown();
            if (boom.life == 0) {
                booms.remove(i);
            }
        }
        // 如果敌方坦克全部被消灭，就进入下一关
        if (enemyTanks.size() == 0) {
            Recorder.addLevel();
            // 初始3个坦克，每3关增加一个坦克，最多10个坦克
            enemyTankNum = 3 + Recorder.getLevel() / 3;
            if (enemyTankNum > 10) {
                enemyTankNum = 10;
            }
            for (int i = 1; i <= enemyTankNum; i++) {
                EnemyTank enemyTank = new EnemyTank(startX + width / 100 + 100 * i, startY + 10, 2);
                enemyTanks.add(enemyTank);
                new Thread(enemyTank).start();
            }
            for (int i = 0; i < enemyTanks.size(); i++) {
                EnemyTank enemyTank = enemyTanks.get(i);
                enemyTank.setOtherTanks(enemyTanks, hero);
            }
        }
        // 显示游戏信息
        showInfo(g);
    }

    /**
     * @param x      坦克左上角的x坐标
     * @param y      坦克右上角的y坐标
     * @param g      画笔
     * @param direct 坦克方向
     * @param type   坦克类型
     */
    public void drawTank(int x, int y, Graphics g, int direct, int type) {
        switch (type) {
            case 0: // 我方坦克
                g.setColor(Color.cyan);
                break;
            case 1: // 敌方坦克
                g.setColor(Color.red);
                break;
        }
        switch (direct) {
            case 0: // 方向向上
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y + 30, x + 20, y);
                break;
            case 1: // 方向向右
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x + 60, y + 20);
                break;
            case 2: // 方向向下
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y + 30, x + 20, y + 60);
                break;
            case 3: // 方向向左
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x, y + 20);
                break;
        }
    }

    public void drawWalls(Graphics g) {
        int row = startX, col = startY;
        for (row = startX; row < startX + width; row += 10) {
            g.fill3DRect(row, col, 10, 10, false);
        }
        for (col = startY; col < startY + height; col += 10) {
            g.fill3DRect(row, col, 10, 10, false);
        }
        for (row = row; row >= startX; row -= 10) {
            g.fill3DRect(row, col, 10, 10, false);
        }
        for (col = col; col >= startY; col -= 10) {
            g.fill3DRect(row + 10, col, 10, 10, false);
        }
    }

    // 判断tank1的子弹是否击中了tank2
    public void isHit(Tank tank1, Tank tank2) {
        for (int i = 0; i < tank1.shots.size(); i++) {
            Shot shot = tank1.shots.get(i);
            switch (tank2.getDirect()) {
                case 0:
                case 2:
                    if (shot.x > tank2.getX() && shot.x < tank2.getX() + 40
                            && shot.y > tank2.getY() && shot.y < tank2.getY() + 60) {
                        shot.isLive = false;
                        tank2.setHP(tank2.getHP() - 1);
                    }
                    break;
                case 1:
                case 3:
                    if (shot.x > tank2.getX() && shot.x < tank2.getX() + 60
                            && shot.y > tank2.getY() && shot.y < tank2.getY() + 40) {
                        shot.isLive = false;
                        tank2.setHP(tank2.getHP() - 1);
                    }
                    break;
            }
        }
    }

    // 判断我方坦克是否与其他坦克重叠
    public boolean isOverlap() {
        int speed = hero.getSpeed();
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank otherTank = enemyTanks.get(i);
            switch (hero.getDirect()) {
                case 0:
                    if (otherTank.getDirect() == 0 || otherTank.getDirect() == 2) {
                        if (hero.getX() >= otherTank.getX() && hero.getX() <= otherTank.getX() + 40
                                && hero.getY() >= otherTank.getY() && hero.getY() <= otherTank.getY() + 60) {
                            return true;
                        } else if (hero.getX() + 40 >= otherTank.getX() && hero.getX() + 40 <= otherTank.getX() + 40
                                && hero.getY() >= otherTank.getY() && hero.getY() <= otherTank.getY() + 60) {
                            return true;
                        }
                    } else {
                        if (hero.getX() >= otherTank.getX() && hero.getX() <= otherTank.getX() + 60
                                && hero.getY() >= otherTank.getY() && hero.getY() <= otherTank.getY() + 40) {
                            return true;
                        } else if (hero.getX() + 40 >= otherTank.getX() && hero.getX() + 40 <= otherTank.getX() + 60
                                && hero.getY() >= otherTank.getY() && hero.getY() <= otherTank.getY() + 40) {
                            return true;
                        }
                    }
                    break;
                case 1:
                    if (otherTank.getDirect() == 0 || otherTank.getDirect() == 2) {
                        if (hero.getX() + 60 >= otherTank.getX() && hero.getX() + 60 <= otherTank.getX() + 40
                                && hero.getY() >= otherTank.getY() && hero.getY() <= otherTank.getY() + 60) {
                            return true;
                        } else if (hero.getX() + 60 >= otherTank.getX() && hero.getX() + 60 <= otherTank.getX() + 40
                                && hero.getY() + 40 >= otherTank.getY() && hero.getY() + 40 <= otherTank.getY() + 60) {
                            return true;
                        }
                    } else {
                        if (hero.getX() + 60 >= otherTank.getX() && hero.getX() + 60 <= otherTank.getX() + 60
                                && hero.getY() >= otherTank.getY() && hero.getY() <= otherTank.getY() + 40) {
                            return true;
                        } else if (hero.getX() + 60 >= otherTank.getX() && hero.getX() + 60 <= otherTank.getX() + 60
                                && hero.getY() + 40 >= otherTank.getY() && hero.getY() + 40 <= otherTank.getY() + 40) {
                            return true;
                        }
                    }
                    break;
                case 2:
                    if (otherTank.getDirect() == 0 || otherTank.getDirect() == 2) {
                        if (hero.getX() >= otherTank.getX() && hero.getX() <= otherTank.getX() + 40
                                && hero.getY() + 60 >= otherTank.getY() && hero.getY() + 60 <= otherTank.getY() + 60) {
                            return true;
                        } else if (hero.getX() + 40 >= otherTank.getX() && hero.getX() + 40 <= otherTank.getX() + 40
                                && hero.getY() + 60 >= otherTank.getY() && hero.getY() + 60 <= otherTank.getY() + 60) {
                            return true;
                        }
                    } else {
                        if (hero.getX() >= otherTank.getX() && hero.getX() <= otherTank.getX() + 60
                                && hero.getY() + 60 >= otherTank.getY() && hero.getY() + 60 <= otherTank.getY() + 40) {
                            return true;
                        } else if (hero.getX() + 40 >= otherTank.getX() && hero.getX() + 40 <= otherTank.getX() + 60
                                && hero.getY() + 60 >= otherTank.getY() && hero.getY() + 60 <= otherTank.getY() + 40) {
                            return true;
                        }
                    }
                    break;
                case 3:
                    if (otherTank.getDirect() == 0 || otherTank.getDirect() == 2) {
                        if (hero.getX() >= otherTank.getX() && hero.getX() <= otherTank.getX() + 40
                                && hero.getY() >= otherTank.getY() && hero.getY() <= otherTank.getY() + 60) {
                            return true;
                        } else if (hero.getX() >= otherTank.getX() && hero.getX() <= otherTank.getX() + 40
                                && hero.getY() + 40 >= otherTank.getY() && hero.getY() + 40 <= otherTank.getY() + 60) {
                            return true;
                        }
                    } else {
                        if (hero.getX() >= otherTank.getX() && hero.getX() <= otherTank.getX() + 60
                                && hero.getY() >= otherTank.getY() && hero.getY() <= otherTank.getY() + 40) {
                            return true;
                        } else if (hero.getX() >= otherTank.getX() && hero.getX() <= otherTank.getX() + 60
                                && hero.getY() + 40 >= otherTank.getY() && hero.getY() + 40 <= otherTank.getY() + 40) {
                            return true;
                        }
                    }
                    break;
            }
        }
        return false;
    }

    // 显示游戏信息
    public void showInfo(Graphics g) {
        int infoX = width + 20;
        int infoY = 30;
        g.setColor(Color.YELLOW);
        Font font = new Font("楷体", Font.BOLD, 30);
        g.setFont(font);
        g.drawString("第" + Recorder.getLevel() + "关", infoX, infoY);
        infoY += 35;
        g.setColor(Color.WHITE);
        font = new Font("楷体", Font.BOLD, 25);
        g.setFont(font);
        g.drawString("当前得分", infoX, infoY);
        infoY += 30;
        g.setColor(Color.YELLOW);
        g.drawString(Recorder.getScore() + "", infoX, infoY);
        infoY += 30;
        g.setColor(Color.WHITE);
        g.drawString("历史最高分", infoX, infoY);
        infoY += 30;
        g.setColor(Color.YELLOW);
        g.drawString(Recorder.getHigh_score() + "", infoX, infoY);
        g.setColor(Color.WHITE);
        infoY += 30;
        g.drawString("剩余生命值", infoX, infoY);
        infoY += 10;
        Image img_HP = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/HP.png"));
        for (int i = 0; i < hero.getHP(); i++) {
            g.drawImage(img_HP, infoX + 25 * i, infoY, 25, 25, this);
        }
        infoY += 55;
        g.drawString("敌方坦克数", infoX, infoY);
        infoY += 10;
        drawTank(infoX, infoY, g, 0, 1);
        g.drawString("X" + enemyTanks.size(), infoX + 50, infoY + 40);
        g.setColor(Color.WHITE);
        infoY += 100;
        g.setColor(Color.YELLOW);
        g.drawString("操作说明:", infoX, infoY);
        g.setColor(Color.WHITE);
        infoY += 35;
        g.drawString("W:向上运动", infoX, infoY);
        infoY += 35;
        g.drawString("A:向左运动", infoX, infoY);
        infoY += 35;
        g.drawString("S:向下运动", infoX, infoY);
        infoY += 35;
        g.drawString("D:向右运动", infoX, infoY);
        infoY += 35;
        g.drawString("J:发射子弹", infoX, infoY);
        infoY += 30;
        g.setFont(new Font("楷体", Font.BOLD, 21));
        g.drawString("(至多五连发)", infoX, infoY);
        infoY += 40;
        g.setFont(font);
        g.setColor(Color.YELLOW);
        g.drawString("温馨提示:", infoX, infoY);
        g.setColor(Color.WHITE);
        g.setFont(new Font("楷体", Font.BOLD, 21));
        infoY += 30;
        g.drawString("随着关卡的增", infoX, infoY);
        infoY += 30;
        g.drawString("加，敌方坦克", infoX, infoY);
        infoY += 30;
        g.drawString("也会更多、更", infoX, infoY);
        infoY += 30;
        g.drawString("强哦！", infoX, infoY);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
            hero.setDirect(0);
            if (!isOverlap()) {
                hero.moveUp();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            hero.setDirect(1);
            if (!isOverlap()) {
                hero.moveRight();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
            hero.setDirect(2);
            if (!isOverlap()) {
                hero.moveDown();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
            hero.setDirect(3);
            if (!isOverlap()) {
                hero.moveLeft();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_J) {
            hero.shotEnemyTank();
            new AePlayWave("src\\shoot.wav").start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        // 每隔50毫秒重绘一次面板
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.repaint();
            if (hero.getHP() <= 0) {
                new ExitInterface();
                break;
            }
        }
    }
}
