package org.cis120.myGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.Timer;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {
    private Apple apple; // will have three different apples
    private SnakeHead snake; // snake, keyboard control

    private boolean playing = false; // whether the game is running
    private JLabel status; // Current status text(Running, Paused, You Lose)
    private JLabel appleNum; // Current status of apple eaten, start with 0

    public static final int COURT_WIDTH = 400;
    public static final int COURT_HEIGHT = 400;

    private int interval = 200;

    private Timer timer;

    private boolean paused = false;
    private int appleEaten = 0;

    public GameBoard(JLabel status, JLabel appleNum) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.timer = new Timer(interval, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start();

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the snake to move as long as an arrow key
        // is pressed, by changing the its velocity accordingly.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    turnLeft();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    turnRight();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    turnDown();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    turnUp();
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (interval > 80) {
                        speedUp(interval - 8);
                    }

                }

            }
        });

        this.status = status;
        this.appleNum = appleNum;
    }

    /***
     * GETTERS : all the getter methods of this class were made public
     * for testing purpose but will have to be private or unnecessary otherwise.
     **********************************************************************************/
    public int getInterval() {
        return this.interval;
    }

    public int getAppleEaten() {
        return this.appleEaten;
    }

    public boolean getPaused() {
        return this.paused;
    }

    public boolean getPlaying() {
        return this.playing;
    }

    public SnakeHead getSnake() {
        return this.snake;
    }

    public Apple getApple() {
        return this.apple;
    }

    /*
     * The turnLeft(), turnRight(), turnUp(), turnDown(), and speedUp()
     * functions were made public for testing purpose but will have to be private
     * otherwise.
     */
    public void turnLeft() {
        int vx = snake.getVx();
        int vy = snake.getVy();
        int vel = Math.max(Math.abs(vx), Math.abs(vy));
        if (vx <= 0 && vy != 0) {
            snake.setVx(-vel);
            snake.setVy(0);
        }
    }

    public void turnRight() {
        int vx = snake.getVx();
        int vy = snake.getVy();
        int vel = Math.max(Math.abs(vx), Math.abs(vy));
        if (vx >= 0 && vy != 0) {
            snake.setVx(vel);
            snake.setVy(0);
        }

    }

    public void turnUp() {
        int vx = snake.getVx();
        int vy = snake.getVy();
        int vel = Math.max(Math.abs(vx), Math.abs(vy));
        if (vx != 0 && vy >= 0) {
            snake.setVx(0);
            snake.setVy(-vel);
        }

    }

    public void turnDown() {
        int vx = snake.getVx();
        int vy = snake.getVy();
        int vel = Math.max(Math.abs(vx), Math.abs(vy));
        if (vx != 0 && vy <= 0) {
            snake.setVx(0);
            snake.setVy(vel);
        }

    }

    public void speedUp(int time) {

        // set the interval of the game as given
        this.interval = time;
        timer.stop();
        timer = new Timer(time, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start();
    }

    /*
     * The following two functions(validApplepos and setApple) were
     * made public only for testing purpose but will have to be private otherwise.
     */

    public boolean invalidApplePos(int px, int py) {
        LinkedList<Integer> posX = snake.getPosX();
        LinkedList<Integer> posY = snake.getPosY();

        int headPosX = snake.getPx();
        int headPosY = snake.getPy();

        int leftLimit = apple.getWidth();
        int rightLimit = COURT_WIDTH - apple.getWidth();
        int upperLimit = apple.getHeight();
        int underLimit = COURT_HEIGHT - apple.getHeight();

        /*
         * check if the potential position of apple
         * does not intersect with snake
         */
        if (px == headPosX && py == headPosY) {
            return true;
        } else if (posX.contains(px) && posY.contains(py)) {
            for (int i = 0; i < posX.size(); i++) {
                if (px == posX.get(i) && py == posY.get(i)) {
                    return true;
                }
            }
        } else if (px <= leftLimit || px >= rightLimit || py <= upperLimit || py >= underLimit) {
            return true;
        }
        return false;
    }

    public int[] setApple() {
        int px = ((int) (Math.random() * 20)) * 20;
        int py = ((int) (Math.random() * 20)) * 20;

        if (invalidApplePos(px, py)) {
            return setApple();
        } else {
            int[] pos = { px, py };
            return pos;
        }
    }

    public void pause() {
        paused = !paused;
        if (paused) {
            timer.stop();
            status.setText("Paused");

        } else {
            timer.start();
            status.setText("Running");
        }
        requestFocusInWindow();
    }

    public void save() {
        // we can only save the game when the game is paused
        if (paused) {
            LinkedList<Integer> posX = snake.getPosX();
            LinkedList<Integer> posY = snake.getPosY();

            // create the Writer
            String filePath = "files/gameStatus.csv";
            File file = Paths.get(filePath).toFile();
            FileWriter writer = null;

            /*
             * the game can save only one game state
             * cannot save multiple conditions
             */
            try {
                writer = new FileWriter(file, false);
            } catch (IOException e) {
                System.out.println("Invalid file path");
            }
            BufferedWriter bw = new BufferedWriter(writer);

            try {
                // save information about game status

                // number of apple eaten
                bw.write(appleEaten + "");
                bw.newLine();

                // current interval
                bw.write(interval + "");
                bw.newLine();

                // current status text
                String stat = status.getText();
                bw.write(stat);
                bw.newLine();

                // the number of the snake body
                bw.write(posX.size() + "" + ",");

                // save information about apple
                bw.write(apple.getPx() + "" + ",");
                bw.write(apple.getPy() + "" + ",");
                bw.write(apple.getVx() + "" + ",");
                bw.write(apple.getVy() + "" + ",");

                // save information about snake
                bw.write(snake.getPx() + "" + ",");
                bw.write(snake.getPy() + ",");
                bw.write(snake.getVx() + "" + ",");
                bw.write(snake.getVy() + "");
                bw.newLine();
            } catch (IOException e1) {
                // do nothing
            }

            // save the x coordinate of body of the snake
            for (int i : posX) {
                try {
                    bw.write(i + "" + ",");
                } catch (IOException e) {
                    // do nothing
                }
            }
            try {
                bw.newLine();
            } catch (IOException e1) {
                // do nothing
            }

            // save the y coordinates of body of the snake
            for (int i : posY) {
                try {
                    bw.write(i + "" + ",");
                } catch (IOException e) {
                    // do nothing
                }
            }

            try {
                bw.close();
            } catch (IOException e) {
                // do nothing
            }
        }

    }

    public void recall() {
        // set the Reader
        String filePath = "files/gameStatus.csv";
        FileReader reader = null;
        try {
            reader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            // do nothing
        }
        BufferedReader br = new BufferedReader(reader);

        // read from the saved file
        String[] points = null;
        String[] bodiesX = null;
        String[] bodiesY = null;

        try {
            // read basic status(interval, number of apple eaten, etc)
            appleEaten = Integer.parseInt(br.readLine());
            appleNum.setText("Apple : " + appleEaten);
            interval = Integer.parseInt(br.readLine());
            status.setText(br.readLine());
            points = br.readLine().split(",");

            // read the x and y body positions of snake
            bodiesX = br.readLine().split(",");
            bodiesY = br.readLine().split(",");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // from String to int
        int bodyNum = Integer.parseInt(points[0]);
        LinkedList<Integer> posX = new LinkedList<Integer>();
        LinkedList<Integer> posY = new LinkedList<Integer>();

        for (int i = 0; i < bodyNum; i++) {
            System.out.println(bodiesX[i]);
            posX.add(Integer.parseInt(bodiesX[0]));
            posY.add(Integer.parseInt(bodiesY[0]));
        }

        // read the status from file and set the status accordingly
        if (status.getText().equals("Paused")) {
            playing = true;
            status.setText("Running");
            paused = false;
        } else {
            paused = true;
            playing = true;

        }

        // set the snake
        snake = new SnakeHead(
                Integer.parseInt(points[7]), Integer.parseInt(points[8]),
                Integer.parseInt(points[5]), Integer.parseInt(points[6]),
                COURT_WIDTH, COURT_HEIGHT, posX, posY
        );

        // set the apple
        if (Integer.parseInt(points[3]) == 0) {
            if (Integer.parseInt(points[4]) == 0) {
                apple = new Apple(
                        Integer.parseInt(points[1]), Integer.parseInt(points[2]),
                        COURT_WIDTH, COURT_HEIGHT
                );
            } else {
                apple = new GreenApple(
                        Integer.parseInt(points[3]), Integer.parseInt(points[4]),
                        Integer.parseInt(points[1]), Integer.parseInt(points[2]),
                        COURT_WIDTH, COURT_HEIGHT
                );
            }
        } else {
            apple = new BlackApple(
                    Integer.parseInt(points[3]), Integer.parseInt(points[4]),
                    Integer.parseInt(points[1]), Integer.parseInt(points[2]),
                    COURT_WIDTH, COURT_HEIGHT
            );
        }

        // restart the timer and set the interval as saved
        speedUp(interval);

        // recall the keyboard focus
        requestFocusInWindow();

    }

    public void reset() {
        if (paused = true) {
            pause();
        }

        playing = true;
        speedUp(200);

        apple = new Apple(COURT_WIDTH, COURT_HEIGHT);
        snake = new SnakeHead(COURT_WIDTH, COURT_HEIGHT);

        status.setText("Running");
        appleNum.setText("Apple : 0");

        // recall the keyboard focus
        requestFocusInWindow();
    }

    private void tick() {
        if (playing) {

            snake.move();
            apple.move();

            // check which apple the snake eats and react accordingly
            if ((apple).intersects(snake)) {
                appleEaten++;
                apple.collide(snake);
                appleNum.setText("Apple : " + String.valueOf(appleEaten));
                apple.changeState();
                int[] pos = setApple();

                if (Math.random() * 9 <= 4.5) {
                    apple = new Apple(pos[0], pos[1], 400, 400);
                } else if (Math.random() * 9 >= 5.5) {
                    apple = new GreenApple(pos[0], pos[1], 400, 400);
                } else {
                    apple = new BlackApple(pos[0], pos[1], 400, 400);
                }

                // check for the game end conditions
            } else if (snake.collideBody()) {
                playing = false;
                status.setText("You Lose!");
            } else if (snake.hitWall()) {
                playing = false;
                status.setText("You Lose!");
            }

            // update the display
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw the game board
        for (int i = 0; i < COURT_WIDTH; i = i + 20) {
            g.drawLine(i, 0, i, 400);
        }

        for (int j = 0; j < COURT_HEIGHT; j = j + 20) {
            g.drawLine(0, j, 400, j);
        }

        apple.draw(g);
        snake.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}
