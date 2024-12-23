package Material;

import java.awt.Point;
import java.util.Vector;
import Data.*;
import Material.Piece;
import Windowing.*;

public class Chevalier extends Piece {

    public Chevalier(int x, int y, int dx, int dy, String color, String src, Draw draw) {
        super(x, y, dx, dy, color, "Image\\" + src + "-knight.png", draw);
    }

    @Override
    public boolean canMove(int x, int y, Db db) {
        Point me = new Point(this.getX(), this.getY());
        Point aim = new Point(x, y);
        double sqrt = Math.sqrt(87 * 87 + Math.pow(2 * 87, 2));

        if (me.distance(aim) == sqrt) {
            if (isCellEmpty(x, y, db)) {
                return true;
            }
            if (checkTreat(x, y, db)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canMoveOnE(int x, int y, Db db) {
        return true;
    }

    @Override
    public void move() {
        if (aim != null) {
            this.setX(aim.x);
            this.setY(aim.y);
            this.setStatus("move");
        }
    }

    @Override
    public Vector<Point> SuggList(Db db) {
        Vector<Point> sugg = new Vector<>();
        Vector<Point> list = PointList();
        for (Point point : list) {
            if (this.canMove(point.x, point.y, db)) {
                if (!checkTreat(point.x, point.y, db)) {
                    sugg.add(point);
                }

            }
        }
        return sugg;
    }

    @Override
    public Vector<Piece> TreatList(Db db) {
        Vector<Piece> sugg = new Vector<>();
        Vector<Point> list = PointList();
        for (Point point : list) {
            if (this.canMove(point.x, point.y, db)) {
                if (checkTreat(point.x, point.y, db)) {
                    Piece piece = getPiece(point.x, point.y, db);
                    if (piece != null) {
                        sugg.add(piece);
                    }
                }
            }
        }
        return sugg;
    }

    @Override
    public boolean canMove2(int x, int y, Db db) {
        return true;
    }

    @Override
    public Vector<Point> pos(Db db) {
        Vector<Point> sugg = new Vector<>();
        Vector<Point> list = PointList();
        for (Point point : list) {
            if (this.canMove(point.x, point.y, db)) {

                sugg.add(point);

            }
        }
        return sugg;
    }

    @Override
    public Vector<Point> pathPoints(Db db, Piece King) {
        Vector<Point> ret = new Vector<>();
        ret.add(new Point(this.getX(), this.getY()));
        return ret;
    }
}
