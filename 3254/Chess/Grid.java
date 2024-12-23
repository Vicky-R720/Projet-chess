package Windowing;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import Material.*;
import Data.*;

public class Grid implements Serializable {
    private int width;
    private Draw draw;

    public Draw getDraw() {
        return draw;
    }

    public void setDraw(Draw draw) {
        this.draw = draw;
    }

    

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    private int height;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private int unity;

    public int getUnity() {
        return unity;
    }

    public void setUnity(int unity) {
        this.unity = unity;
    }

    public Grid(int height, int width, int unity, Draw draw) {
        this.width = width;
        this.height = height;
        this.unity = unity;
        this.draw = draw;
    }

    

    public void deploy(Graphics g) {
        int i = 0, j = 0;
        for (i = 0; i < height; i += unity) {
            for (j = 0; j < width; j += unity) {
                int mod = j / unity;
                int mod2 = i / unity;
                if ((mod2 % 2 != 0 && mod % 2 == 0) || (mod2 % 2 == 0 && mod % 2 != 0)) {
                    g.setColor(Color.GRAY);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(j, i, unity, unity);
            }
        }
    }
}
