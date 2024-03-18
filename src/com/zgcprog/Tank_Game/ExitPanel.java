package com.zgcprog.Tank_Game;

import javax.swing.*;
import java.awt.*;

/**
 * @author 祝广程
 * @version 1.0
 */
public class ExitPanel extends JPanel {
    JButton button = null;
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
        // 设置画笔颜色为白色
        g.setColor(Color.DARK_GRAY);
        // 填充面板背景
        g.fillRect(0, 0, screenWidth, screenHeight);
        Font font = new Font("楷体", Font.BOLD, 40);
        g.setFont(font);
        g.setColor(Color.YELLOW);
        g.drawString("游戏结束！", 120, 80);
        g.drawString("本次得分：" + Recorder.getScore(), 90, 140);
        button.requestFocus();
    }

    public void setButton(JButton button) {
        this.button = button;
    }
}
