package com.zgcprog.Tank_Game;

import javax.swing.*;
import java.awt.*;

/**
 * @author 祝广程
 * @version 1.0
 */
public class MenuPanel extends JPanel {
    JButton button1 = null;
    JButton button2 = null;
    JButton button3 = null;

    // 获取屏幕大小
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    final int screenWidth = screenSize.width;
    final int screenHeight = screenSize.height;
    // 定义菜单界面的大小为屏幕大小的2/3
    final int width = screenWidth - screenWidth / 3;
    final int height = screenHeight - screenHeight / 3;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // 填充面板背景，默认是黑色
        g.fillRect(0, 0, screenWidth, screenHeight);
        // 设置画笔颜色为黄色
        g.setColor(Color.YELLOW);
        Font font = new Font("楷体", Font.BOLD, 100);
        g.setFont(font);
        g.drawString("坦克大战", 180, 100);
        button1.requestFocus();
        button2.requestFocus();
        button3.requestFocus();
    }

    public void setButton1(JButton button1) {
        this.button1 = button1;
    }

    public void setButton2(JButton button2) {
        this.button2 = button2;
    }

    public void setButton3(JButton button3) {
        this.button3 = button3;
    }
}
