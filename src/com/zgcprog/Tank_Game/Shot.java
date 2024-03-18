package com.zgcprog.Tank_Game;

import java.awt.*;

/**
 * @author 祝广程
 * @version 1.0
 */
public class Shot implements Runnable {
    int x;                  // 子弹横坐标
    int y;                  // 子弹纵坐标
    int direct = 0;         // 子弹方向
    int speed = 10;          // 子弹速度
    boolean isLive = true;  //子弹的存活状态
    // 获取屏幕大小
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    final int screenWidth = screenSize.width;
    final int screenHeight = screenSize.height;
    // 获取游戏界面的边界
    final int width = screenWidth - screenWidth / 3;
    final int height = screenHeight - screenHeight / 3;

    @Override
    public void run() {
        while (true) {
            // 休眠50ms
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 根据方向改变子弹的坐标
            switch (direct) {
                case 0:
                    y -= speed;
                    break;
                case 1:
                    x += speed;
                    break;
                case 2:
                    y += speed;
                    break;
                case 3:
                    x -= speed;
                    break;
            }
            // 当子弹移动到边界时销毁线程
            if (x <= 10 || x >= width || y <= 10 || y >= height || isLive == false) {
                isLive = false;
                break;
            }
        }
    }

    public Shot(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }
}
