package org.cis120.myGame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class Apple extends GameObj {
    public static final String IMG_FILE = "files/apple.png";
    public static final int SIZE = 20;
    public static final int INIT_POS_X = 320;
    public static final int INIT_POS_Y = 160;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;

    private boolean state = true;
    private static BufferedImage img;

    /**
     * Constructor
     */
    public Apple(int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public Apple(int px, int py, int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, px, py, SIZE, SIZE, courtWidth, courtHeight);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public Apple(int vx, int vy, int px, int py, int courtWidth, int courtHeight) {
        super(vx, vy, px, py, SIZE, SIZE, courtWidth, courtHeight);
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

    public void changeState() {
        this.state = !this.state;
    }

    public void collide(SnakeHead snake) {

        // adds one more body at the last
        if (intersects(snake)) {
            LinkedList<Integer> beforeX = snake.getPosX();
            LinkedList<Integer> beforeY = snake.getPosY();

            int[] add = snake.getLastBodyPos();

            beforeX.addLast(add[0]);
            beforeY.addLast(add[1]);

            snake.setPosX(beforeX);
            snake.setPosY(beforeY);
        }
    }

    @Override
    public boolean hitWall() {
        int px = this.getPx();
        int py = this.getPy();
        int vx = this.getVx();
        int vy = this.getVy();

        return (px + vx > getMaxX() || px + vx < 0 || py + vy > getMaxY() ||
                py + vy < 0);
    }

    @Override
    public boolean intersects(GameObj that) {
        int px1 = this.getPx();
        int py1 = this.getPy();
        int px2 = that.getPx();
        int py2 = that.getPy();
        return (Math.abs(px1 - px2) < 20 && Math.abs(py2 - py1) < 20);
    }

    @Override
    public void draw(Graphics g) {
        if (state) {
            g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
        }
    }
}
