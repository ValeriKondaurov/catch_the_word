package ru;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Drop {
    // рисунок транспорта
    private Image drop;
    //координаты
    private float drop_left ;
    private float drop_top;
    private float drop_right;
    private float drop_bottom;
    //скорость
    private float drop_v;
    //название
    private String drop_name;

    public Drop(String  drop_id, float game_window_width) throws IOException {
           this.setDropImage(drop_id);
           this.drop_name = drop_id;
           this.setDropNew(-100, true, 0, 50, game_window_width);
    }

    public void setDrop_v(float drop_v) {
        this.drop_v = drop_v;
    }

    public String getDrop_name() {
        return drop_name;
    }

    public float getDrop_left() {
        return drop_left;
    }

    public float getDrop_top() {
        return drop_top;
    }

    public float getDrop_right() {
        return drop_right;
    }

    public float getDrop_bottom() {
        return drop_bottom;
    }

    public Image getDrop() {
        return drop;
    }

    public int getDropHeight() {
        return drop.getHeight(null);
    }

    public int getDropWidth() {
        return drop.getWidth(null);
    }


    public void setDropCurrent(float delta_time) {
        this.drop_top += + this.drop_v * delta_time;
        this.drop_bottom = this.drop_top + drop.getHeight(null);

    }

    public void setDropNew(float drop_top, boolean drop_left_random, float drop_left, float drop_v_add, float game_window_width) {
        this.drop_top = drop_top;
        Random random = new Random();
        ;
        if (drop_left_random)
            this.drop_left = random.nextInt((int) game_window_width - this.drop.getWidth(null));
                    //(int) (Math.random() * (game_window_width - this.drop.getWidth(null)));
        else
            this.drop_left = drop_left;

        this.drop_bottom = this.drop_top + this.drop.getHeight(null);
        this.drop_right = this.drop_left + this.drop.getWidth(null);
        this.drop_v += drop_v_add;
    }

    public void setDropImage (String drop_id) throws IOException {
        drop = ImageIO.read(new File(System.getProperty("user.dir") + "/drop/"+ drop_id));
    }

    public static Drop checkDropwNew (ArrayList<Drop> dropArrayList, String drop_id, int game_window_width) throws IOException {
        boolean check = true;
        Drop dropTemp = null;
        int  diffWindow = game_window_width/50;
        while (check) {
            dropTemp = new Drop(drop_id, game_window_width);
            for (Drop d:dropArrayList){
                if (((dropTemp.getDrop_left() >= d.getDrop_left()-diffWindow) && (dropTemp.getDrop_left() <= d.getDrop_right() + diffWindow))
                        || ((dropTemp.getDrop_right() >= d.getDrop_left()-diffWindow) && (dropTemp.getDrop_right() <= d.getDrop_right() + diffWindow))) {
                    check = false;
                }
            }
            if (check) {
                check = false;
            }
            else check = true;
        }
        return dropTemp;
    }

}
