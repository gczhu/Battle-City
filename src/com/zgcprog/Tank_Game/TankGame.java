package com.zgcprog.Tank_Game;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author 祝广程
 * @version 1.0
 * 主程序
 */
public class TankGame extends JFrame {
    // 定义GamePanel
    GamePanel gp = null;
    // 定义一个变量记录游戏模式
    static int mode;

    public static void main(String[] args) {
        new Menu();
    }

    public TankGame(int mode) {
        this.mode = mode;
        gp = new GamePanel();
        // 加入面板
        this.add(gp);
        // 设置窗口大小
        this.setBounds(300, 150, 1310, 770);
        // 让JFrame监听键盘事件
        this.addKeyListener(gp);
        // 启动守护线程MyPanel以不断绘制面板
        new Thread(gp).start();
        // 设置窗口关闭后程序自动停止
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 可视化
        this.setVisible(true);
        // 设置关闭窗口时记录游戏信息
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Recorder.saveInfo();
                System.exit(0);
            }
        });
    }
}
