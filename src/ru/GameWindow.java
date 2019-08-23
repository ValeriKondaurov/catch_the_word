package ru;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameWindow extends JFrame{

    private static GameWindow game_window;
    private static long last_frame_time;
    private static long seconds=0;

    private static Image background;
    private static ArrayList<String> backgroundBase ;

    private static Image game_over;

    private static Drop drop;
    private static ArrayList<String> dropBase ;

    //private static ArrayList<Drop> dropArrayList;
    private static Plank plank;
    private static ArrayList<String> plankBase ;

    private static int score;
    private static int drop_id=0;
    private static int id=0;
    private static boolean game_over_check = false;



    public static void main(String[] args) throws IOException {
        //загрузка картинок в программу
        System.out.println(new File(System.getProperty("user.dir") + "/game_over.png").getAbsolutePath());
        game_over = ImageIO.read(new File(System.getProperty("user.dir") + "/game_over.png"));
        //создание игрового окна
        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//признак завершение программы при закрытии окна
        game_window.setLocation(0, 0);//координита окна
        game_window.setSize(906, 461);//размер окна
        game_window.setResizable(false);//запрет на изменение его размера
        game_window.setTitle("Score: " + score); //счетчик очков

        dropBase =  listFileInDir ("/drop");// список падающих картинок
        drop = new Drop(dropBase.get(drop_id), game_window.getWidth());
        //dropArrayList.add(new Drop(drop_id + ".png", game_window.getWidth()));
        plankBase =  listFileInDir ("/plank"); //список транспорта
        plank = new Plank(plankBase.get(id), game_window.getWidth(), game_window.getHeight());
        backgroundBase = listFileInDir ("/background");
        background = ImageIO.read(new File(System.getProperty("user.dir")
                +"/background/"+backgroundBase.get(id)));

        last_frame_time = System.nanoTime(); //запись системного времени для движения капли
        try {//установка курсора мыши
            java.awt.Robot robot=new java.awt.Robot();
            robot.mouseMove(400,300);
        }
        catch (java.lang.Exception e) {
        }
        Point location = MouseInfo.getPointerInfo().getLocation(); //координаты мыши

        GameField game_field = new GameField();//создание объекта класса


        game_field.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {

                if (plank.checkPlankWindow(game_window.getWidth(), me.getX())) {
                    plank.setPlankNew(me.getX(), false, 0);
                }
            }
        });
        game_window.addKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                // сохранить координаты
                if ((ke.getKeyCode() == KeyEvent.VK_RIGHT) && (plank.checkPlankWindow(game_window.getWidth(), (int) plank.getPlank_left() + 10))) {
                    plank.setPlankNew((int) plank.getPlank_left() + 10, false, 0);
                }
            }
        });
        game_window.add(game_field);//обрисовка нового поля
        game_window.setVisible(true);//отображение результаты предыдущих действи на экране
    }
    //задание параметров графических объектов с новыми данными
    private static void onRepaint(Graphics g)  throws IOException  {

        long current_time = System.nanoTime(); //запись текущего времени
        float delta_time = (current_time - last_frame_time); //расчет дельты времени для движения капли
        seconds+= delta_time;
        delta_time *= 0.000000001f;
        last_frame_time = current_time;
        // добавление новой капли
        /*if (seconds/(Math.pow(10, 9)) >= 5) {
            dropArrayList.add(Drop.checkDropwNew(dropArrayList, drop_id + ".png", game_window.getWidth()));
            seconds = 0;
        }*/
        //проверка пред. операции на истину
        if(plank.checkPlankDrop(drop)) {
    //            //подсчет очков
                score++;
                game_window.setTitle("Score: " + score);
                seconds = 0;
                // смещение координат для новой капли
                /*dropArrayList.remove(d);
                dropArrayList.add(Drop.checkDropwNew(dropArrayList, drop_id + ".png", game_window.getWidth()));
                */

                drop.setDropNew(-100, true, 0, 5, game_window.getWidth());
            System.out.println(drop_id + " " + dropBase.size());
                if (++drop_id == dropBase.size()) {
                    drop_id =0;
                    if (id == 0) id = 1;
                        else id = 0;
                    background = ImageIO.read(new File(System.getProperty("user.dir")
                            +"/background/"+backgroundBase.get(id)));
                    plank.setPlankImage(plankBase.get(id));

                }
                drop.setDropImage(dropBase.get(drop_id));

            }
        g.drawImage(background, 0, 0, null); //обрисовка фона
        g.drawImage(plank.getPlank(), (int)plank.getPlank_left(), (int)plank.getPlank_top(), null);// обрисовка планки

         // расчет координаты капли с учетом скорости
            drop.setDropCurrent(delta_time);
            g.drawImage(drop.getDrop(), (int) drop.getDrop_left(), (int) drop.getDrop_top(),
                    (int) drop.getDropWidth(),
                    (int) drop.getDropHeight(), null); //обрисовка капли
            if (drop.getDrop_top() > game_window.getHeight()) {
                game_over_check = true;
            }


        if (game_over_check) {
            g.drawImage(game_over, 280, 120, null);
            drop.setDrop_v(0);
        }

        //вывод GameOver при выходе капли за границу экрана
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

    private static ArrayList<String> listFileInDir (String dir) throws IOException {
        final File folders = new File(System.getProperty("user.dir") +dir);
        ArrayList<String> fileName = new ArrayList<>();;
        System.out.println("Проверка условий " + folders.getAbsolutePath() + " --" + folders.isDirectory());
        for (File fileEntiry:folders.listFiles())
            if (!fileEntiry.isDirectory()) {
                System.out.println("Проверка условий " + fileEntiry.getAbsolutePath());
                String s = fileEntiry.getName();
                System.out.println("Имя файла " + s);
                if (s.contains(".png")) fileName.add(s);
            }
        return  fileName;
    }

}