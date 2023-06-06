package org.cis120.myGame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class GreenApple extends Apple {
    public static final String IMG_FILE = "files/greenapple.png";

    private boolean state = true;
    private static BufferedImage img;

    private static int velY = -2;

    /**
     * Constructor
     */

    /*
     * the first constructor is for testing purpose,
     * not used in the game implementation
     */
    public GreenApple(int courtWidth, int courtHeight) {
        super(courtWidth, courtHeight);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public GreenApple(int px, int py, int courtWidth, int courtHeight) {
        super(0, velY, px, py, courtWidth, courtHeight);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public GreenApple(int vx, int vy, int px, int py, int courtWidth, int courtHeight) {
        super(vx, vy, px, py, courtWidth, courtHeight);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    /**************************************************************************
     * UPDATES AND OTHER METHODS
     **************************************************************************/

    @Override
    public void move() {
        int py = this.getPy();
        if (hitWall()) {
            if (getVy() > 0) {
                setVy(-(getVy()));
                py = py - getVy();
                this.setPy(py);

            } else {
                setVy(-(getVy()));
                py = py - getVy();
                this.setPy(py);
            }
        } else {
            py = py + this.getVy();
            this.setPy(py);
        }
    }

    @Override
    public void collide(SnakeHead snake) {

        // the last body gets deleted
        if (intersects(snake)) {
            LinkedList<Integer> beforeX = snake.getPosX();
            LinkedList<Integer> beforeY = snake.getPosY();

            if (beforeX.size() > 1) {
                beforeX.removeLast();
                beforeY.removeLast();

                snake.setPosX(beforeX);
                snake.setPosY(beforeY);
            }

            // randomly set the velocity of apple that will appear after
            velY = -(int) (Math.random() * 19 + 1);
        }
    }

    @Override
    public void draw(Graphics g) {
        if (state) {
            g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
        }
    }
}
