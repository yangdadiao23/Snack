package snack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener, ActionListener {

    //贪吃蛇参数
    String fx; //蛇的前进方向
    int length; //当前蛇的长度
    int[] snakeX = new int[600]; //当前蛇的坐标X
    int[] snakeY = new int[500]; //当前蛇的坐标Y
    Timer timer;//蛇前进的定时器

    //食物参数
    int foodX;
    int foodY;
    Random random = new Random();

    //玩家参数
    int score; //当前得分
    boolean isFail = false; //游戏是否结束
    boolean isStart = false; //游戏是否开始
    boolean isPass = false; //游戏是否通关

    //构造器
    public GamePanel() {
        init();
        //获取键盘的监听事件
        this.setFocusable(true);
        this.addKeyListener(this);
        timer = new Timer(100, this);
        timer.start();
    }

    //初始化
    public void init() {
        //贪吃蛇长度＆位置
        length = 3;
        snakeX[0] = 100;
        snakeY[0] = 100;
        snakeX[1] = 75;
        snakeY[1] = 100;
        snakeX[2] = 50;
        snakeY[2] = 100;
        fx = "R";//贪吃蛇方向

        //在合理范围内随机产生食物
        foodX = 25 + 25 * random.nextInt(34);
        foodY = 75 + 25 * random.nextInt(24);

        //从0分开始游戏
        score = 0;
    }

    //画板：画界面，画贪吃蛇
    //Graphics：画笔
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//清屏
        this.setBackground(Color.WHITE);//设置背景的颜色

        //绘制显示长度，得分的图片
        Data.header.paintIcon(this, g, 180, 0);

        //绘制游戏区域
        g.fillRect(25, 75, 850, 600);

        //绘制贪吃蛇
        switch (fx) { //根据方向绘制贪吃蛇头部
            case "U":
                Data.up.paintIcon(this, g, snakeX[0], snakeY[0]);
                break;
            case "D":
                Data.down.paintIcon(this, g, snakeX[0], snakeY[0]);
                break;
            case "L":
                Data.left.paintIcon(this, g, snakeX[0], snakeY[0]);
                break;
            case "R":
                Data.right.paintIcon(this, g, snakeX[0], snakeY[0]);
                break;
        }

        for (int i = 1; i < length; i++) {
            Data.body.paintIcon(this, g, snakeX[i], snakeY[i]);//根据贪吃蛇长度绘制身体
        }

        //画食物
        Data.food.paintIcon(this, g, foodX, foodY);

        //显示分数
        g.setColor(Color.blue);
        g.setFont(new Font("微软雅黑", Font.BOLD, 18));
        g.drawString("长度:" + length, 300, 60);
        g.drawString("得分:" + score, 500, 60);

        //游戏显示：是否开始
        if (!isStart) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));//设置字体
            g.drawString("按下空格开始游戏", 300, 300);
        }

        //游戏显示：失败提醒
        if (isFail) {
            g.setColor(Color.RED);
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));//设置字体
            g.drawString("游戏结束，按下空格重新开始游戏", 150, 300);
            g.drawString("长度为：" + length + "，分数为：" + score, 200, 350);
        }

        //游戏显示：通关提醒
        if (isPass) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));//设置字体
            g.drawString("游戏通关！", 350, 300);
        }
    }

    //监听键盘的输入
    @Override
    public void keyPressed(KeyEvent e) {
        //接受键盘的输入，获取按下的键
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {//如果按下的是空格键
            if (isFail) {
                isFail = false;
                init(); //重新开始游戏
            } else {
                isStart = true;
                repaint();//刷新界面
            }
        }

        //判断前进方向
        if (keyCode == KeyEvent.VK_UP) {
            fx = "U";
        } else if (keyCode == KeyEvent.VK_DOWN) {
            fx = "D";
        } else if (keyCode == KeyEvent.VK_LEFT) {
            fx = "L";
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            fx = "R";
        }
    }

    //定时器：监听时间，帧：执行定时操作
    @Override
    public void actionPerformed(ActionEvent e) {
        //随着分数增加，蛇的运动速度加快，难度增大，并判断是否已通关
        int delay = 100 - score / 5;
        timer.setDelay(delay);
        if (delay <= 20) {
            isPass = true;
            repaint();
        }
        //如果游戏处于开始状态，并且游戏没有结束或通关，移动贪吃蛇
        if (isStart && !isFail && !isPass) {
            for (int i = length - 1; i > 0; i--) { //身体移动（后面的移动到前面）
                snakeX[i] = snakeX[i - 1];
                snakeY[i] = snakeY[i - 1];
            }
            //通过控制方向让头部移动
            switch (fx) {
                case "R":
                    snakeX[0] = snakeX[0] + 25;//头部移动
                    //边界判断
                    if (snakeX[0] > 850) {
                        isFail = true;
                    }
                    break;
                case "L":
                    snakeX[0] = snakeX[0] - 25;//头部移动
                    //边界判断
                    if (snakeX[0] < 25) {
                        isFail = true;
                    }
                    break;
                case "U":
                    snakeY[0] = snakeY[0] - 25;//头部移动
                    //边界判断
                    if (snakeY[0] < 75) {
                        isFail = true;
                    }
                    break;
                case "D":
                    snakeY[0] = snakeY[0] + 25;//头部移动
                    //边界判断
                    if (snakeY[0] > 650) {
                        isFail = true;
                    }
                    break;
            }

            //如果小蛇的头和食物坐标重合，吃到食物
            if (snakeX[0] == foodX && snakeY[0] == foodY) {
                length++;//长度加1
                snakeX[length - 1] = foodX - 1;
                snakeY[length - 1] = foodY - 1;
                score = score + 10;//加10分
                //重新生成食物
                foodX = 25 + 25 * random.nextInt(34);
                foodY = 75 + 25 * random.nextInt(24);
            }

            //如果小蛇的头和身体坐标有重合，游戏结束
            for (int i = 1; i < length; i++) {
                if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                    isFail = true;
                    break;
                }
            }

            repaint();//刷新页面
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //敲击键盘
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //释放某个键
    }
}
