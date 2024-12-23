package Material;

import java.awt.Point;
import java.util.Vector;
import Material.Piece;
import Windowing.*;
import Data.*;

public class Roi extends Piece {
    Vector<Point> points = new Vector<>();
     boolean inDanger=false;
     
    public boolean isInDanger() {
        return inDanger;
    }

    public void setInDanger(boolean inDanger) {
        this.inDanger = inDanger;
    }

    @Override
    public boolean canMoveOnE(int x, int y,Db db) {
        return true;
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
    
    public Vector<Point> getPoints() {
        return points;
    }

    public void setPoints(Vector<Point> points) {
        this.points = points;
    }

    public Roi(int x, int y, int dx, int dy, String color, String src, Draw draw) {
        super(x, y, dx, dy, color, "Image\\" + src + "-king.png", draw);
    }

    @Override
    public boolean canMove(int x, int y,Db db) {
        Point me = new Point(this.getX(), this.getY());
        Point aim = new Point(x, y);
        double sqrt = Math.sqrt(2 * 87 * 87);

        if (me.distance(aim) == 87 || me.distance(aim) == sqrt) {
            if (isCellEmpty(x, y,db)) {
                return true;
            }
            if (checkTreat(x, y,db)) {
                return true;
            }
        }

        return false;
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
    public boolean canMove2(int x, int y,Db db) {
        return true;
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
    public Vector<Point> pathPoints(Db db, Piece King) {
        Vector<Point> ret = new Vector<>();
        ret.add(new Point(this.getX(), this.getY()));
        return ret;
    }

}
