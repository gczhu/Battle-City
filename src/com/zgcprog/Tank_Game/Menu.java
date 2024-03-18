package com.zgcprog.Tank_Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author 祝广程
 * @version 1.0
 */
public class Menu extends JFrame {
    // 定义MenuPanel
    MenuPanel mp = null;

    public Menu() {
        mp = new MenuPanel();
        mp.setLayout(null);
        // 设置窗口大小
        this.setBounds(500, 150, 810, 600);
        // 创建两个按钮
        JButton button1 = new JButton("新游戏");
        JButton button2 = new JButton("继续游戏");
        JButton button3 = new JButton("退出游戏");
        // 设置字体
        Font font = new Font("楷体", Font.BOLD, 60);
        button1.setFont(font);
        button2.setFont(font);
        button3.setFont(font);
        // 让面板获取button对象
        mp.setButton1(button1);
        mp.setButton2(button2);
        mp.setButton3(button3);
        // 设置按钮的大小
        button1.setBounds(250,150,300,80);
        button2.setBounds(250,260,300,80);
        button3.setBounds(250,370,300,80);
        // 监听按钮事件
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TankGame(1);
                Menu.this.dispose();
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TankGame(2);
                Menu.this.dispose();
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        // 加入面板
        this.add(mp);
        // 向面板中加入按钮
        mp.add(button1);
        mp.add(button2);
        mp.add(button3);
        // 设置窗口关闭后程序自动停止
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 可视化
        this.setVisible(true);
    }
}
