package snack;

import javax.swing.*;

//游戏启动类
public class StartGames {
    public static void main(String[] args) {
        //1、绘制一个静态窗口 JFrame
        JFrame frame = new JFrame("贪吃蛇小游戏");
        //设置界面大小
        frame.setBounds(10,10,900,720);
        //窗口大小不可以改变
        frame.setResizable(false);
        //设置关闭事件
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //2、面板 JPanel 可以加入到JFrame
        frame.add(new GamePanel());
        //展现窗口
        frame.setVisible(true);
    }
}
