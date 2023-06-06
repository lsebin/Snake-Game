package org.cis120.myGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

public class SnakeHead extends GameObj {
    public static final int SIZE = 20;
    public static final int INIT_POS_X = 80;
    public static final int INIT_POS_Y = 160;
    public static final int INIT_VEL_X = 20;
    public static final int INIT_VEL_Y = 0;

    private LinkedList<Integer> posX;
    private LinkedList<Integer> posY;
    private int lastX;
    private int lastY;

    /**
     * Constructor
     */

    public SnakeHead(int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);
        posX = new LinkedList<Integer>();
        posY = new LinkedList<Integer>();
        posX.add(60);
        posX.add(40);
        posY.add(160);
        posY.add(160);
    }

    public SnakeHead(
            int vx, int vy, int px, int py, int courtWidth, int courtHeight,
            LinkedList<Integer> posX, LinkedList<Integer> posY
    ) {
        super(vx, vy, px, py, SIZE, SIZE, courtWidth, courtHeight);
        this.posX = posX;
        this.posY = posY;
    }

    /**************************************************************************
     * GETTER, SETTER, AND OTHER METHODS
     **************************************************************************/

    public LinkedList<Integer> getPosX() {
        LinkedList<Integer> xBodies = new LinkedList<Integer>();
        xBodies.addAll(posX);
        return xBodies;
    }

    public LinkedList<Integer> getPosY() {
        LinkedList<Integer> yBodies = new LinkedList<Integer>();
        yBodies.addAll(posY);
        return yBodies;
    }

    public int[] getLastBodyPos() {
        int[] lastPos = { lastX, lastY };
        return lastPos;
    }

    public void setPosX(LinkedList<Integer> posX) {
        this.posX = posX;
    }

    public void setPosY(LinkedList<Integer> posY) {
        this.posY = posY;
    }

    public boolean collideBody() {
        int px = this.getPx();
        int py = this.getPy();

        for (int i = 0; i < posX.size(); i++) {
            if (px == posX.get(i) && py == posY.get(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void move() {
        int px = this.getPx();
        int py = this.getPy();
        super.move();

        posX.addFirst(px);
        posY.addFirst(py);
        lastX = posX.removeLast();
        lastY = posY.removeLast();
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.green);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());

        g.setColor(Color.yellow);
        for (int i = 0; i < posX.size(); i++) {
            int px = posX.get(i);
            int py = posY.get(i);
            g.fillOval(px, py, this.getWidth(), this.getHeight());
        }
    }
}
