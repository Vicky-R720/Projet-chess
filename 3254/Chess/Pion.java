package Material;

import java.awt.Point;
import java.lang.reflect.Method;
import java.util.Vector;
import Data.*;

import Material.Piece;
import Windowing.*;

public class Pion extends Piece {
    boolean firstMove = true;

    public boolean isFirstMove() {
        return firstMove;
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    public Pion(int x, int y, int dx, int dy, String color, String src, Draw draw) {
        super(x, y, dx, dy, color, "Image\\" + src + "-pawn.png", draw);
    }

    public Pion() {

    }

    @Override
    public void move() {
        if (aim != null) {
            this.setX(aim.x);
            this.setY(aim.y);
            this.setFirstMove(false);
            this.setStatus("move");
        }
    }

    @Override
    public boolean canMoveOnE(int x, int y,Db db) {
        int[] diag = new int[3];
        diag[0] = this.getX() + this.getDx();
        diag[1] = this.getX() - this.getDx();
        if (this.getColor().equals("black")) {

            diag[2] = this.getY() + this.getDy();

        }
        if (this.getColor().equals("white")) {
            diag[2] = this.getY() - this.getDy();
        }

        if (y == diag[2]) {
            if (x == diag[0]) {
                return true;
            } else if (x == diag[1]) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }
    @Override
    public boolean canMove2(int x, int y,Db db) {
        return true;
    }
    @Override
    public boolean canMove(int x, int y,Db db) {
        int step1 = 0, step2 = 0;
        if (canMoveOnE(x, y,db) && checkTreat(x, y,db)) {
            return true;
        }

        if (this.getColor().equals("black")) {
            step2 = this.getY() + 2 * this.getDy();
            step1 = this.getY() + this.getDy();
        }
        if (this.getColor().equals("white")) {
            step2 = this.getY() - 2 * this.getDy();
            step1 = this.getY() - this.getDy();
        }

        if (isCellEmpty(x, y,db)) {
            if (isFirstMove()) {
                if (x == this.getX() && y == step2) {
                    if (isCellEmpty(this.getX(), step1,db)) {
                        return true;
                    }
                }
                if (x == this.getX() && y == step1) {

                    return true;
                }
            } else {
                if (x == this.getX() && y == step1) {
                    return true;
                }
            }
        }

        return false;

    }

    @Override
    public Vector<Point> pos(Db db) {
        Vector<Point> sugg = new Vector<>();
        Vector<Point> list = PointList();
        for (Point point : list) {
            if (this.canMove(point.x, point.y,db)) {

                sugg.add(point);

            }
        }
        return sugg;
    }


    @Override
    public Vector<Point> SuggList(Db db) {
        Vector <Point> sugg=new Vector<>(); 
        Vector<Point> list=PointList();
        for (Point point : list) {
            if(this.canMove(point.x, point.y,db))
            {
                if(!checkTreat(point.x, point.y,db))
                {
                    sugg.add(point);
                }

            }
        }
        return sugg;
    }

    @Override
    public Vector<Piece> TreatList(Db db) {
        Vector <Piece> sugg=new Vector<>(); 
        Vector<Point> list=PointList();
        for (Point point : list) {
            if(this.canMove(point.x, point.y,db))
            {
                if(checkTreat(point.x, point.y,db))
                {
                    Piece piece=getPiece(point.x, point.y,db);
                    if(piece!=null)
                    {
                        sugg.add(piece);
                    }
                }
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
