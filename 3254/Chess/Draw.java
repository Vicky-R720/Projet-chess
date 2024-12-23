package Windowing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javax.swing.JPanel;
import Data.Db;
import Windowing.Grid;
import Windowing.Win;
import Inc.*;
import Material.*;

public class Draw extends JPanel {
    private Win win;
    private int unity = 87;
    private Grid grid = new Grid(696, 696, unity, this);
    private Db all = new Db(this);
    private String turn = "white";
    private Piece current = null;
    private String alert = null;

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public Piece getCurrent() {
        return current;
    }

    public void setCurrent(Piece current) {
        this.current = current;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public Db getAll() {
        return all;
    }

    public void setAll(Db all) {
        this.all = all;
    }

    private Vector<Point> sugg = new Vector<>();
    private Vector<Piece> treat = new Vector<>();

    public Vector<Piece> getTreat() {
        return treat;
    }

    public void setTreat(Vector<Piece> treat) {
        this.treat = treat;
    }

    public Vector<Point> getSugg() {
        return sugg;
    }

    public void setSugg(Vector<Point> sugg) {
        this.sugg = sugg;
    }

    public Win getWin() {
        return win;
    }

    public void setWin(Win win) {
        this.win = win;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Draw(Win win) {
        this.win = win;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        this.grid.deploy(g);
        this.all.checkK(g);
        if (all.GameOver(turn)) {
            System.out.println("echec et mat");
        }

        for (int i = 0; i < all.size(); i++) {
            all.get(i).move();
        }
        for (int i = 0; i < all.size(); i++) {
            all.get(i).check(all);
        }
        for (int i = 0; i < all.size(); i++) {
            all.get(i).drawImg(g);
            g.setColor(Color.BLUE);
            int a = 0;
            if (all.get(i) instanceof Roi) {
                a = all.list(all.get(i)).size();
            } else {
                a = all.get(i).pos(all).size();
            }

            g.drawString(Integer.toString(a), all.get(i).getX(), all.get(i).getY());
        }

        if (!sugg.isEmpty()) {

            for (int i = 0; i < sugg.size(); i++) {
                g.setColor(Color.YELLOW);
                g.fillRect(sugg.get(i).x + 5, sugg.get(i).y + 5, 75, 75);
            }
        }
        if (!treat.isEmpty()) {

            for (int i = 0; i < treat.size(); i++) {
                Piece piece = treat.get(i);
                piece.setCaseColor(Color.RED);
            }
        } else {
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getCaseColor() != Color.BLUE) {
                    all.get(i).setCaseColor(null);
                }
            }
        }
        repaint();
    }

    public int getUnity() {
        return unity;
    }

    public void setUnity(int unity) {
        this.unity = unity;
    }

}
