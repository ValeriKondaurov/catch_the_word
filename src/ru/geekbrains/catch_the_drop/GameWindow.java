package ru.geekbrains.catch_the_drop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GameWindow extends JFrame{

    private static GameWindow game_window;
    private static long last_frame_time;
    private static Image background;
    private static Image game_over;
    private static Image drop;

    private static Image plank;
    private static float drop_left = 150;
    private static float drop_top = -100;
    private static float plank_left = 400;
    private static float plank_top = 450;
    private static float drop_v = 50;
    private static int score;
    private static int drop_i;

    private static float plank_right;
    private static float plank_bottom;
    private static float drop_right;
    private static float drop_bottom;

    public static void main(String[] args) throws IOException {
        //загрузка картинок в программу
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        drop_i = 0;
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop"+drop_i+".png"));
        plank = ImageIO.read(GameWindow.class.getResourceAsStream("BUS.png"));
        //создание игрового окна
        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//признак завершение программы при закрытии окна
        game_window.setLocation(200, 100);//координита окна
        game_window.setSize(1006, 611);//размер окна
        game_window.setResizable(false);//запрет на изменение его размера
        last_frame_time = System.nanoTime(); //запись системного времени для движения капли
        try {//установка курсора мыши
            java.awt.Robot robot=new java.awt.Robot();
            robot.mouseMove(600,500);
        }
        catch (java.lang.Exception e) {
        }
        Point location = MouseInfo.getPointerInfo().getLocation(); //координаты мыши
        plank_left = (int) location.getX()-200;

        GameField game_field = new GameField();//создание объекта класса
        game_field.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                // сохранить координаты
                 plank_right = plank_left + plank.getWidth(null);//определения размера капли от точки координаты
                 plank_bottom = plank_top + plank.getHeight(null);
                if (plank_right < game_window.getWidth() ||
                        (plank_right >= game_window.getWidth() && me.getX()<(game_window.getWidth()-plank.getWidth(null)))) {
                    plank_left += ( me.getX() - plank_left );
                    plank_left = me.getX();
                }
            }
        });
       game_window.add(game_field);//обрисовка нового поля
        game_window.setVisible(true);//отображение результаты предыдущих действи на экране
    }
    //задание параметров графических объектов с новыми данными
    private static void onRepaint(Graphics g)  throws IOException  {


        long current_time = System.nanoTime(); //запись текущего времени
        float delta_time = (current_time - last_frame_time) * 0.000000001f; //расчет дельты времени для движения капли
        last_frame_time = current_time;
        drop_right = drop_left + drop.getWidth(null);//определения размера капли от точки координаты
        drop_bottom = drop_top + drop.getHeight(null);
        boolean is_drop = (plank_top<= drop_bottom && plank_bottom >= drop_bottom) //проверка нижней части капли между
                && (plank_left <= drop_left && plank_right >= drop_left)  // граница планки (левый край)
                && (plank_left <= drop_right && plank_right >= drop_right);
        //проверка пред. операции на истину
        game_window.setTitle("расстояние между каплей и планкой  - " + (plank_top - drop_bottom));
        if(is_drop) {
//            //подсчет очков
            score++;
            game_window.setTitle("Score: " + score);

            // смещение координат для новой капли
            drop_top = -100;
            drop_left = (int) (Math.random() * (game_window.getWidth() - drop.getWidth(null)));
            //увеличение скорости
            drop_v = drop_v + 20;

            if (drop_i == 0) {
                drop_i =1;
            } else {
                drop_i =0;
            }
            drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop"+drop_i+".png"));
        }
        drop_top = drop_top + drop_v * delta_time; // расчет координаты капли с учетом скорости
        g.drawImage(background, 0, 0, null); //обрисовка фона
        g.drawImage(plank, (int)plank_left, (int)plank_top, null);// обрисовка планки
        g.drawImage(drop, (int) drop_left, (int) drop_top,
                        (int) drop_left+drop.getWidth(null),
                        (int) drop_top+drop.getHeight(null),
                        0,0,drop.getWidth(null),
                        drop.getHeight(null), null); //обрисовка капли


        //g.drawImage(drop, (int) drop_left, (int) drop_top,  null); //обрисовка капли

        //вывод GameOver при выходе капли за границу экрана
        if (drop_top > game_window.getHeight()) g.drawImage(game_over, 280, 120, null);
    }

    private static class GameField extends JPanel {
        //
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            try {
                onRepaint(g);//обнолвение параментров прорисовки
            } catch (IOException e) {
                e.printStackTrace();
            }
            repaint();//перерисовка с затиранием предыдущего вида

        }
    }
}