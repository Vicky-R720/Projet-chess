package Material;

import java.awt.Point;
import java.util.Vector;
import Data.*;
import Windowing.*;

public class Fo extends Piece {

    public Fo(int x, int y, int dx, int dy, String color, String src, Draw draw) {
        super(x, y, dx, dy, color, "Image\\" + src + "-bishop.png", draw);
    }

    // public boolean CanMove()
    // {
    @Override
    public void move() {
        if (aim != null) {
            this.setX(aim.x);
            this.setY(aim.y);
            this.setStatus("move");
        }
    }

    @Override
    public boolean canMoveOnE(int x, int y, Db db) {
        return true;
    }

    @Override
    public boolean canMove(int x, int y, Db db) {
        if (isWithinBoard(x, y)) {
            if (canMoveInDirection(1, 1, x, y, db) ||
                    canMoveInDirection(-1, -1, x, y, db) ||
                    canMoveInDirection(1, -1, x, y, db) ||
                    canMoveInDirection(-1, 1, x, y, db)) {
                return true;
            }
        }
        return false;
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
        if (isWithinBoard(x, y)) {
            if (canMoveIn(1, 1, x, y, db) ||
                    canMoveIn(-1, -1, x, y, db) ||
                    canMoveIn(1, -1, x, y, db) ||
                    canMoveIn(-1, 1, x, y, db)) {
                return true;
            }
        }
        return false;
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

        Vector<Point> rVector = new Vector<>();
        Vector<Vector<Point>> Paths1 = PathsD(db);
        for (Vector<Point> vector : Paths1) {
            if (vector.contains(new Point(King.getX(), King.getY()))) {
                rVector = vector;
                break;
            }
        }
        return rVector;

    }
}
