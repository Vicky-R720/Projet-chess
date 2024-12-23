package Inc;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import Material.Piece;

public class Func {
    public static BufferedImage getImage(String imagePath) {
        BufferedImage image = null;
        try {
            // Charge l'image à partir du chemin spécifié
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static Vector<Point> List(int x, int y) {
        Vector<Point> ret = new Vector<>();
        for (Point point : Piece.PointList()) {
            Rectangle rectangle = new Rectangle(point.x, point.y, 87, 87);
            if (rectangle.contains(x, y)) {
                ret.add(new Point(point.x, point.y));
            }
        }
        return ret;

    }
}
