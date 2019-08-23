package ru;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Plank {
    private Image plank;
    private float plank_left;
    private float plank_top ;
    private float plank_right;
    private float plank_bottom;
    private HashMap<String, Boolean> plankCheckDrop = new HashMap<>();


    public Plank(String plank_name, float game_window_width, float game_window_height) throws IOException {
        this.setPlankImage(plank_name);
        this.setPlankNew(game_window_width/2-60, true, game_window_height-150);
    }

    public Image getPlank() {
        return plank;
    }

    public float getPlank_left() {
        return plank_left;
    }

    public float getPlank_top() {
        return plank_top;
    }

    public float getPlank_right() {
        return plank_right;
    }

    public float getPlank_bottom() {
        return plank_bottom;
    }

    public void setPlankNew(float  plank_left, boolean set, float plank_top) {
        this.plank_left = plank_left;
        this.plank_right = this.plank_left + this.plank.getWidth(null);
        if (set) {
            this.plank_top = plank_top;
            this.plank_bottom = this.plank_top + this.plank.getHeight(null);
        }
    }

    public void setPlankImage (String plank_name) throws IOException {
        plank = ImageIO.read(new File(System.getProperty("user.dir") +"/plank/"+plank_name));
    }

    public boolean checkPlankWindow (float game_window_width, int mouseX) {
         return
                 ((this.plank_right < game_window_width) &&
                         mouseX<(game_window_width-this.plank.getWidth(null)))
                         //|| (this.plank_right >= 0))
         ;
    }

    public boolean checkPlankDrop (Drop drop) {
        return  (drop.getDrop_bottom()>= this.plank_top  && drop.getDrop_bottom() < this.plank_bottom ) //проверка нижней части капли между
                && (drop.getDrop_left() >= this.plank_left  && drop.getDrop_left() <= this.plank_right);

    }
}
