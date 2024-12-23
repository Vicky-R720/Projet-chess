package Material;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.beans.ConstructorProperties;
import java.util.Vector;

import Data.Db;
import Inc.*;
import Windowing.*;

abstract public class Piece implements Cloneable {

    static final int CELL_SIZE = 87;
    static final int BOARD_LIMIT = 696;
    static final int BOARD_WIDTH = 8 * CELL_SIZE; // Largeur du plateau
    static final int BOARD_HEIGHT = 8 * CELL_SIZE; // Hauteur du plateau

    private int x, y, dx, dy;
    private BufferedImage image = null;
    Draw draw;
    String color = null;
    boolean clicked = false;
    Rectangle coord;
    Point aim = null;
    Color caseColor = null;
    boolean has_moved = false;
    boolean movable=true;
    String status="Firstmove";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isMovable() {
        return movable;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public abstract Vector<Point> SuggList(Db d);

    public abstract Vector<Piece> TreatList(Db d);

    public abstract boolean canMoveOnE(int x, int y, Db db);

    public abstract boolean canMove2(int x, int y, Db d);

    public abstract Vector<Point> pos(Db d);

    public static Vector<Point> PointList() {
        Vector<Point> list = new Vector<>();
        for (int i1 = 0; i1 < BOARD_LIMIT; i1 += CELL_SIZE) {
            for (int j1 = 0; j1 < BOARD_LIMIT; j1 += CELL_SIZE) {
                list.add(new Point(j1, i1));
            }

        }
        return list;
    }


    public Vector<Vector<Point>> PathsD(Db db) {
        Vector<Vector<Point>> Paths = new Vector<>(4);
        for (int i = 0; i < 4; i++) {
            Paths.add(new Vector<>());
        }
        for (Point point : PointList()) {
            if (this.canMoveInDirection(1, 1, point.x, point.y, db)) {
                Paths.get(0).add(point);
            }
            if (this.canMoveInDirection(-1, -1, point.x, point.y, db)) {
                Paths.get(1).add(point);
            }
            if (this.canMoveInDirection(-1, 1, point.x, point.y, db)) {
                Paths.get(2).add(point);
            }
            if (this.canMoveInDirection(1, -1, point.x, point.y, db)) {
                Paths.get(3).add(point);
            }
        }
        return Paths;
    }

    public Vector<Vector<Point>> PathsM(Db db) {
        Vector<Vector<Point>> Paths = new Vector<>(4);
        for (int i = 0; i < 4; i++) {
            Paths.add(new Vector<>());
        }
        for (Point point : PointList()) {
            if (this.canMoveInDirection(0, 1, point.x, point.y, db)) {
                Paths.get(0).add(point);
            }
            if (this.canMoveInDirection(0, -1, point.x, point.y, db)) {
                Paths.get(1).add(point);
            }
            if (this.canMoveInDirection(1, 0, point.x, point.y, db)) {
                Paths.get(2).add(point);
            }
            if (this.canMoveInDirection(-1, 0, point.x, point.y, db)) {
                Paths.get(3).add(point);
            }
        }
        return Paths;
    }


    public abstract Vector<Point> pathPoints(Db db,Piece King);
    



    public boolean Has_moved() {
        return has_moved;
    }

    public void setHas_moved(boolean has_moved) {
        this.has_moved = has_moved;
    }

    public Color getCaseColor() {
        return caseColor;
    }

    public void setCaseColor(Color caseColor) {
        this.caseColor = caseColor;
    }

    Vector<Point> lp = new Vector<>();

    public Vector<Point> getLp() {
        return lp;
    }

    public void setLp(Vector<Point> lp) {
        this.lp = lp;
    }

    public abstract boolean canMove(int x, int y, Db db);

    public static Piece getPiece(int x, int y, Db db) {
        Piece piece = null;
        Point aim = new Point(x, y);
        for (int i = 0; i < db.size(); i++) {
            Point point = new Point(db.get(i).getX(), db.get(i).getY());
            if (point.equals(aim)) {
                piece = db.get(i);

            }
        }
        return piece;
    }

    public void check(Db db) {
        Piece piece = null;
        if (this.has_moved) {
            for (int i = 0; i < db.size(); i++) {
                piece = db.get(i);
                if (!this.equals(piece)) {
                    if (!this.getColor().equals(piece.getColor())) {
                        if (this.getCoord().intersects(piece.getCoord())) {
                            db.remove(piece);
                            this.setHas_moved(false);
                            this.setCaseColor(null);
                            this.setClicked(false);
                            getDraw().getSugg().clear();
                            getDraw().getTreat().clear();
                        }
                    }
                }
            }
        }

    }

    public boolean isCellEmpty(int x, int y, Db db) {
        boolean test = true;
        Piece piece = getPiece(x, y, db);
        if (piece != null) {
            test = false;
        }

        return test;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Point getAim() {
        return aim;
    }

    public void setAim(Point aim) {
        this.aim = aim;
    }

    public Rectangle getCoord() {
        coord = new Rectangle(x, y, dy, dx);
        return coord;
    }

    public void setCoord(Rectangle coord) {
        this.coord = coord;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public Piece() {

    }

    public Draw getDraw() {
        return draw;
    }

    public void setDraw(Draw draw) {
        this.draw = draw;
    }

    public Piece(int x, int y, int dx, int dy, String color, String src, Draw draw) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.dx = dx;
        this.dy = dy;
        this.image = Func.getImage(src);
        this.draw = draw;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void drawImg(Graphics g) {
        if (image != null) {
            g.drawImage(image, this.x + 5, this.y + 5, 75, 75, caseColor, draw);
        }
    }

    public abstract void move();

    public boolean check2(int x, int y, Db db) {
        Piece found = getPiece(x, y, db);
        if (found != null) {
            if (!found.getColor().equals(this.getColor())) {
                if (found instanceof Roi) {

                    return true;
                }
            }
        }
        if (found == null) {
            return true;
        }
        return false;
    }

    public boolean canMoveInDirection(int deltaX, int deltaY, int x, int y, Db db) {
        int j = CELL_SIZE;
        for (j = CELL_SIZE; isCellEmpty(this.getX() + j * deltaX, this.getY() + j * deltaY, db); j += CELL_SIZE) {
            if (x == this.getX() + j * deltaX && y == this.getY() + j * deltaY) {
                return true;
            }
            if (!isWithinBoard(this.getX() + j * deltaX, this.getY() + j * deltaY)) {
                break;
            }
        }
        if (checkTreat(this.getX() + j * deltaX, this.getY() + j * deltaY, db)) {
            if (x == this.getX() + j * deltaX && y == this.getY() + j * deltaY) {
                return true;
            }
        }
        return false;
    }

    public boolean canMoveIn(int deltaX, int deltaY, int x, int y, Db db) {
        int j = CELL_SIZE;
        for (j = CELL_SIZE; check2(this.getX() + j * deltaX, this.getY() + j * deltaY, db); j += CELL_SIZE) {
            if (x == this.getX() + j * deltaX && y == this.getY() + j * deltaY) {
                return true;
            }
            if (!isWithinBoard(this.getX() + j * deltaX, this.getY() + j * deltaY)) {
                break;
            }
        }
        return false;
    }

    public boolean isWithinBoard(int x, int y) {
        return x >= 0 && x < BOARD_WIDTH && y >= 0 && y < BOARD_HEIGHT;
    }

    public boolean checkTreat(int x, int y, Db db) {
        Piece found = getPiece(x, y, db);
        if (found != null) {
            if (!found.getColor().equals(this.getColor())) {
                return true;
            }
        }
        return false;
    }

}
