package org.cis120.myGame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class BlackApple extends Apple {
    public static final String IMG_FILE = "files/blackapple.png";

    private boolean state = true;
    private static BufferedImage img;

    private static int velY = 5;
    private static int velX = 5;

    /**
     * Constructor
     */

    /*
     * the first constructor is for testing purpose,
     * not used in the game implementation
     */
    public BlackApple(int courtWidth, int courtHeight) {
        super(courtWidth, courtHeight);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public BlackApple(int px, int py, int courtWidth, int courtHeight) {
        super(velX, velY, px, py, courtWidth, courtHeight);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public BlackApple(int vx, int vy, int px, int py, int courtWidth, int courtHeight) {
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
        int px = this.getPx();
        int vy = this.getVy();
        int vx = this.getVx();
        if (hitWall()) {
            if (vy > 0 && vx > 0) {
                setVy(-getVy());
                py = py + getVy();
                px = px + getVx();
                this.setPy(py);
                this.setPx(px);
            } else if (vy < 0 && vx > 0) {
                setVx(-getVx());
                py = py + getVy();
                px = px + getVx();
                this.setPy(py);
                this.setPx(px);
            } else if (vy < 0 && vx < 0) {
                setVy(-getVy());
                py = py + getVy();
                px = px + getVx();
                this.setPy(py);
                this.setPx(px);
            } else {
                setVx(-getVx());
                py = py + getVy();
                px = px + getVx();
                this.setPy(py);
                this.setPx(px);
            }
        } else {
            py = py + this.getVy();
            px = px + this.getVx();
            this.setPy(py);
            this.setPx(px);
        }
    }

    @Override
    public void collide(SnakeHead snake) {

        // reverses the head and tail
        if (intersects(snake)) {
            LinkedList<Integer> beforeX = snake.getPosX();
            LinkedList<Integer> beforeY = snake.getPosY();

            int[] last = snake.getLastBodyPos();

            LinkedList<Integer> reversedX = new LinkedList<Integer>();
            LinkedList<Integer> reversedY = new LinkedList<Integer>();

            for (int i = 0; i < beforeX.size(); i++) {
                reversedX.add(beforeX.getLast());
                reversedY.add(beforeY.getLast());
            }

            snake.setPosX(reversedX);
            snake.setPosY(reversedY);

            snake.setVx(last[0] - beforeX.getLast());
            snake.setVy(last[1] - beforeY.getLast());

            snake.setPx(last[0]);
            snake.setPy(last[1]);

            // randomly set the velocity of apple that will appear after
            velX = -(int) (Math.random() * 19 + 1);
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
