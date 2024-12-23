package Data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;
import Material.*;
import Windowing.Draw;

public class Db extends Vector<Piece> {
    int height = 696;
    int unity = 87;
    int d = 0;
    int x = 0, y = 0;
    int y1 = height - unity;
    int y2 = height - 2 * unity;
    private Roi black;
    private Roi white;

    public Roi getBlack() {
        return black;
    }

    public void setBlack(Roi black) {
        this.black = black;
    }

    public Roi getWhite() {
        return white;
    }

    public void setWhite(Roi white) {
        this.white = white;
    }

    public Db() {

    }

    public Db(Draw draw) {
        super(0, 1);

        black = new Roi(348, y, unity, unity, "black", "black", draw);
        white = new Roi(348, y1, unity, unity, "white", "white", draw);

        this.add(new Tour(0, y, unity, unity, "black", "black", draw));
        this.add(new Chevalier(87, y, unity, unity, "black", "black", draw));
        this.add(new Fo(174, y, unity, unity, "black", "black", draw));
        this.add(new Reine(261, y, unity, unity, "black", "black", draw));
        this.add(black);
        this.add(new Fo(435, y, unity, unity, "black", "black", draw));
        this.add(new Chevalier(522, y, unity, unity, "black", "black", draw));
        this.add(new Tour(609, y, unity, unity, "black", "black", draw));
        for (int i = 0; i < height; i += unity) {
            Pion pion1 = new Pion(i, unity, 87, 87, "black", "black", draw);
            this.add(pion1);
        }
        this.add(new Tour(0, y1, unity, unity, "white", "white", draw));
        this.add(new Chevalier(87, y1, unity, unity, "white", "white", draw));
        this.add(new Fo(174, y1, unity, unity, "white", "white", draw));
        this.add(new Reine(261, y1, unity, unity, "white", "white", draw));
        this.add(white);
        this.add(new Fo(435, y1, unity, unity, "white", "white", draw));
        this.add(new Chevalier(522, y1, unity, unity, "white", "white", draw));
        this.add(new Tour(609, y1, unity, unity, "white", "white", draw));
        for (int i = 0; i < height; i += unity) {
            Pion pion2 = new Pion(i, y2, 87, 87, "white", "white", draw);
            this.add(pion2);
        }

    }

    public Piece King(String turn) {
        for (Piece piece : this) {
            if (piece instanceof Roi && piece.getColor().equals(turn)) {
                return piece;
            }

        }
        return null;
    }

    public boolean check(Piece piece) {
        return (piece instanceof Fo || piece instanceof Reine || piece instanceof Tour);
    }

    public boolean inDanger(Piece King) {
        for (Piece piece : this) {
            if (!piece.equals(King) && !piece.getColor().equals(King.getColor())) {
                if (piece.canMove(King.getX(), King.getY(), this)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Vector<Piece> Danger(Piece King) {
        Vector<Piece> pieces = new Vector<>();
        for (Piece piece : this) {
            if (!piece.equals(King) && !piece.getColor().equals(King.getColor())) {
                if (piece.canMove(King.getX(), King.getY(), this)) {
                    if (!pieces.contains(piece)) {
                        pieces.add(piece);
                    }
                }
            }
        }
        return pieces;
    }

    public boolean Friend(Piece King, Piece Test) {

        return (!Test.equals(King) && Test.getColor().equals(King.getColor()));

    }

    public boolean CanProtect(String turn,Piece test) {
        Piece king = King(turn);
        if (inDanger(king)) {
            if (Friend(king, test)) {
                for (Piece piece1 : Danger(king)) {
                    if (!piece1.pos(this).isEmpty()) {
                        for (Point p : piece1.pathPoints(this, king)) {
                            if (test.canMove(p.x, p.y, this)) {
                                return true;
                            }
                            if (test.canMove(piece1.getX(), piece1.getY(), this)) {

                                return true;
                            }

                        }
                    }
                }

            }
        }
        return false;
    }

    public Vector<Point> protect(String turn,Piece test) {
        Vector<Point> ret = new Vector<>();
        Piece king = King(turn);
        if (inDanger(king)) {
            if (Friend(king, test)) {
                for (Piece piece1 : Danger(king)) {
                    if (!piece1.pos(this).isEmpty()) {
                        for (Point p : piece1.pathPoints(this, king)) {
                            if (test.canMove(p.x, p.y, this)) {
                                if (!ret.contains(p))
                                    ret.add(p);
                            }
                            if (test.canMove(piece1.getX(), piece1.getY(), this)) {

                                if (!ret.contains(new Point(piece1.getX(), piece1.getY())))
                                    ret.add(new Point(piece1.getX(), piece1.getY()));
                            }

                        }
                    }
                }

            }
        }
        return ret;
    }

    public Vector<Piece> movable(String turn) {
        Vector<Piece> pieces = new Vector<>();
        Piece king = King(turn);
        if (inDanger(king)) {
            if (CanMove(king)) {
                pieces.add(king);
            }
        }
        for (Piece piece : this) {
            if (CanProtect(turn,piece)) {
                pieces.add(piece);
            }
        }
        return pieces;
    }

    public boolean GameOver(String turn) {
        Piece king = King(turn);
        if (inDanger(king)) {
            if (!CanMove(king)) {
                if(movable(turn).isEmpty())
                {
                    return true;
                }
            }
        }
        return false;
    }

    public void checkK(Graphics g) {
        for (Piece piece : this) {
            if (piece instanceof Roi) {
                if (!Danger(piece).isEmpty()) {
                    piece.setCaseColor(Color.RED);
                    for (Piece piece1 : Danger(piece)) {
                        if (!piece.pos(this).isEmpty()) {
                            for (Point p : piece1.pathPoints(this, piece)) {
                                g.setColor(Color.RED);
                                g.fillRect(p.x + 5, p.y + 5, 75, 75);
                                for (Piece object1 : this) {
                                    if (Friend(piece, object1)) {
                                        if (object1.canMove(p.x, p.y, this)
                                                || object1.canMove(piece1.getX(), piece1.getY(), this)) {
                                            object1.setCaseColor(Color.GREEN);

                                        }
                                    }

                                }
                            }
                        }
                    }

                } else {
                    piece.setCaseColor(null);
                }
            }
        }
    }

    public boolean isKingInCheck(Piece King, int x, int y) {
        Db db = (Db) this.clone();
        db.remove(King);
        Piece p = Piece.getPiece(x, y, db);
        if (p != null) {
            if (King.checkTreat(x, y, db)) {
                db.remove(p);
            }
        }
        for (Piece piece : db) {
            if (!piece.equals(King) && !piece.getColor().equals(King.getColor())) {

                if (piece instanceof Pion) {
                    if (piece.canMoveOnE(x, y, db)) {
                        return true;
                    }
                } else {
                    if (piece.canMove(x, y, db)) {
                        return true;
                    }

                }
            }

        }
        return false;
    }

    public boolean CanMove(Piece King) {
        for (Point point : King.pos(this)) {
            if (!isKingInCheck(King, point.x, point.y)) {
                return true;
            }
        }
        return false;
    }

    public Vector<Point> list(Piece King) {
        Vector<Point> ret = new Vector<>();
        for (Point point : King.pos(this)) {
            if (!isKingInCheck(King, point.x, point.y)) {
                if (!ret.contains(point))
                    ret.add(point);
            }
        }
        return ret;
    }
}
