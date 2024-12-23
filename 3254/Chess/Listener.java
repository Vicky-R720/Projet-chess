package Listening;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

public class Listener implements MouseListener {
    private Win win;

    public Listener(Win win) {
        this.win = win;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        win.getInterpreter().execute(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
