package com.zgcprog.Tank_Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author 祝广程
 * @version 1.0
 */
public class ExitInterface extends JFrame {
    // 定义ExitPanel
    ExitPanel ep = null;

    public ExitInterface() {
        ep = new ExitPanel();
        ep.setLayout(null);
        // 设置窗口大小
        this.setBounds(600, 300, 450, 300);
        // 创建按钮
        JButton button = new JButton("确定");
        // 设置字体
        Font font = new Font("楷体", Font.BOLD, 21);
        button.setFont(font);
        // 让面板获取button对象
        ep.setButton(button);
        // 设置按钮的大小
        button.setBounds(175,170,80,40);
        // 监听按钮事件
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Recorder.saveInfo();
                Recorder.saveHighScore();
                System.exit(0);
            }
        });
        // 加入面板
        this.add(ep);
        // 向面板中加入按钮
        ep.add(button);
        // 设置窗口关闭后程序自动停止
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 可视化
        this.setVisible(true);
        // 设置关闭窗口时记录游戏信息
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Recorder.saveInfo();
                Recorder.saveHighScore();
                System.exit(0);
            }
        });
    }
}
