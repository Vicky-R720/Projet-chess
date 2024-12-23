package Listening;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.channels.Pipe;
import java.util.EventListener;
import java.util.EventListenerProxy;
import java.util.Vector;

import org.w3c.dom.events.Event;

import Data.*;
import Inc.Func;
import Material.*;
import Windowing.*;

public class Interpreter {
    private Win win;
    private Piece current = null;

    public Interpreter(Win win) {
        this.win = win;
    }

    public void swipeTurn() {
        if (win.getDraw().getTurn().equals("white")) {
            win.getDraw().setTurn("black");
        } else {
            win.getDraw().setTurn("white");
        }
    }

    public void undo(Piece piece) {

        piece.setCaseColor(null);
        piece.setClicked(false);
        win.getDraw().getSugg().clear();
        win.getDraw().getTreat().clear();
    }

    public void FirstClick(Piece piece, Db db1) {
        if (piece instanceof Roi) {
            for (Point point : piece.SuggList(db1)) {
                if (!db1.isKingInCheck(piece, point.x, point.y)) {
                    win.getDraw().getSugg().add(point);
                }
            }
            for (Piece p : piece.TreatList(db1)) {
                Point point1 = new Point(p.getX(), p.getY());
                if (!db1.isKingInCheck(piece, point1.x, point1.y)) {
                    win.getDraw().getTreat().add(p);
                }
            }
        }

        else {
            if (!db1.inDanger(db1.King(win.getDraw().getTurn()))) {
                win.getDraw().setSugg(piece.SuggList(db1));
                win.getDraw().setTreat(piece.TreatList(db1));
            } else {
                for (Point point : piece.SuggList(db1)) {
                    if (db1.protect(win.getDraw().getTurn(), piece).contains(point)) {
                        win.getDraw().getSugg().add(point);
                    }
                }
                for (Piece p : piece.TreatList(db1)) {
                    Point point1 = new Point(p.getX(), p.getY());
                    if (db1.protect(win.getDraw().getTurn(), piece).contains(point1)) {
                        win.getDraw().getTreat().add(p);
                    }
                }
            }
        }
    }

    public void SecondClick(Piece piece, Db db1, int x, int y) {
        for (Point p : Func.List(x, y)) {
            if (piece instanceof Roi) {
                if (piece.canMove(p.x, p.y, db1)) {
                    if (!db1.isKingInCheck(piece, p.x, p.y)) {
                        piece.setAim(new Point(p.x, p.y));
                        if (piece.checkTreat(p.x, p.y, db1)) {
                            piece.setHas_moved(true);
                        }
                        swipeTurn();

                    }

                }
            } else {
                if (!db1.inDanger(db1.King(win.getDraw().getTurn()))) {
                    if (piece.canMove(p.x, p.y, db1)) {
                        piece.setAim(new Point(p.x, p.y));
                        if (piece.checkTreat(p.x, p.y, db1)) {
                            piece.setHas_moved(true);
                        }
                        swipeTurn();
                    }
                } else {
                    if (piece.canMove(p.x, p.y, db1)) {
                        if (db1.protect(win.getDraw().getTurn(), piece).contains(new Point(p.x, p.y))) {
                            piece.setAim(new Point(p.x, p.y));
                            if (piece.checkTreat(p.x, p.y, db1)) {
                                piece.setHas_moved(true);
                            }
                            swipeTurn();
                        }
                    }
                }

            }
            undo(piece);
        }

    }

    public void execute(int x, int y) {
        Db db1 = win.getDraw().getAll();
        Piece piece = null;
        String turn = win.getDraw().getTurn();
        for (int i = 0; i < db1.size(); i++) {
            piece = db1.get(i);
            if (piece.getColor().equals(win.getDraw().getTurn()) && piece.getColor().equals(win.getColor())) {
                if (piece.getCoord().contains(x, y)) {
                    if (!db1.movable(turn).isEmpty()) {
                        if (db1.movable(turn).contains(piece)) {
                            piece.setClicked(true);
                            piece.setCaseColor(Color.BLUE);
                            FirstClick(piece, db1);
                        }
                    } else {
                        piece.setClicked(true);
                        piece.setCaseColor(Color.BLUE);
                        FirstClick(piece, db1);
                    }
                }
                if (piece.isClicked()) {
                    if (!piece.getCoord().contains(x, y)) {
                        SecondClick(piece, db1, x, y);
                        win.setMove(new Point(piece.getX(), piece.getY()), piece.getAim());
                    }
                }
            }
        }

    }

    public void execute1(int x, int y) {
        Db db1 = win.getDraw().getAll();
        Piece piece = null;
        String turn = win.getDraw().getTurn();
        for (int i = 0; i < db1.size(); i++) {
            piece = db1.get(i);
            if (piece.getColor().equals(win.getDraw().getTurn())) {
                if (piece.getCoord().contains(x, y)) {
                    if (!db1.movable(turn).isEmpty()) {
                        if (db1.movable(turn).contains(piece)) {
                            piece.setClicked(true);
                            piece.setCaseColor(Color.BLUE);
                            FirstClick(piece, db1);
                        }
                    } else {
                        piece.setClicked(true);
                        piece.setCaseColor(Color.BLUE);
                        FirstClick(piece, db1);
                    }
                }
            }
        }
    }

    public void execute2(int x, int y) {
        Db db1 = win.getDraw().getAll();
        Piece piece = null;
        String turn = win.getDraw().getTurn();
        for (int i = 0; i < db1.size(); i++) {
            piece = db1.get(i);
            if (piece.getColor().equals(win.getDraw().getTurn())) {
                if (piece.isClicked()) {
                    if (!piece.getCoord().contains(x, y)) {
                        SecondClick(piece, db1, x, y);
                    }
                }
            }
        }
    }

}
