package org.cis120.myGame;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

import javax.swing.JLabel;

public class GameTest {
    private GameBoard board;

    final JLabel status = new JLabel("Running", JLabel.CENTER);
    final JLabel appleEaten = new JLabel("Apple : 0", JLabel.CENTER);

    @BeforeEach
    public void setUp() {
        board = new GameBoard(status, appleEaten);
    }

    @Test
    public void testBasicSetUp() {
        assertEquals(board.getInterval(), 200);
        assertEquals(board.getAppleEaten(), 0);
        assertFalse(board.getPaused());
        assertFalse(board.getPlaying());
        assertNull(board.getSnake());
        assertNull(board.getApple());

        board.reset();
        SnakeHead snake = board.getSnake();

        // when called reset
        assertEquals(snake.getVx(), 20);
        assertEquals(snake.getVy(), 0);
        assertEquals(snake.getPx(), 80);
        assertEquals(snake.getPy(), 160);
    }

    @Test
    public void testReset() {
        board.reset();
        SnakeHead snake = board.getSnake();
        Apple apple = board.getApple();

        // status of snake
        assertEquals(snake.getVx(), 20);
        assertEquals(snake.getVy(), 0);
        assertEquals(snake.getPx(), 80);
        assertEquals(snake.getPy(), 160);

        LinkedList<Integer> posX = new LinkedList<Integer>();
        LinkedList<Integer> posY = new LinkedList<Integer>();
        posX.add(60);
        posX.add(40);
        posY.add(160);
        posY.add(160);

        assertEquals(snake.getPosX(), posX);
        assertEquals(snake.getPosY(), posY);

        // status of apple
        assertEquals(apple.getVx(), 0);
        assertEquals(apple.getVy(), 0);
        assertEquals(apple.getPx(), 320);
        assertEquals(apple.getPy(), 160);

        // status of board
        assertTrue(board.getPlaying());
        assertEquals(board.getInterval(), 200);
    }

    @Test
    public void testSnakeChangeDirection() {
        board.reset();
        SnakeHead snake = board.getSnake();

        // when turn up
        board.turnUp();
        assertEquals(snake.getVx(), 0);
        assertEquals(snake.getVy(), -20);

        // when turn left
        board.turnLeft();
        assertEquals(snake.getVx(), -20);
        assertEquals(snake.getVy(), 0);

        // when turn left
        board.turnDown();
        assertEquals(snake.getVx(), 0);
        assertEquals(snake.getVy(), 20);

        // when turn left
        board.turnRight();
        assertEquals(snake.getVx(), 20);
        assertEquals(snake.getVy(), 0);
    }

    @Test
    public void testSpeedUp() {
        board.reset();

        board.speedUp(100);
        assertEquals(board.getInterval(), 100);

        board.speedUp(70);
        assertEquals(board.getInterval(), 70);
    }

    @Test
    public void testPause() {
        board.reset();

        // when not paused
        board.pause();
        assertTrue(board.getPaused());

        // when paused
        board.pause();
        assertFalse(board.getPaused());
    }

    @Test
    public void testInvalidApplePos() {
        board.reset();
        SnakeHead snake = board.getSnake();
        LinkedList<Integer> posX = snake.getPosX();
        LinkedList<Integer> posY = snake.getPosY();

        // invalid when apple's position is within the acceptable boundary
        assertTrue(board.invalidApplePos(0, 0));
        assertTrue(board.invalidApplePos(380, 380));
        assertTrue(board.invalidApplePos(160, 385));
        assertTrue(board.invalidApplePos(16, 200));

        // invalid when apple intersects with any snake body parts
        assertTrue(board.invalidApplePos(snake.getPx(), snake.getPy()));
        assertTrue(board.invalidApplePos(posX.getFirst(), posY.getFirst()));
        assertTrue(board.invalidApplePos(posX.getLast(), posY.getLast()));
    }

    @Test
    public void testValidApplePos() {
        board.reset();

        assertFalse(board.invalidApplePos(40, 40));
        assertFalse(board.invalidApplePos(379, 21));
    }

    @Test
    public void testSnakeAppleInvalidIntersection() {

        /*
         * when we call collide when apple and snake
         * does not actually intersect, nothing happens
         */
        board.reset();

        SnakeHead snake = board.getSnake();
        Apple apple = board.getApple();

        LinkedList<Integer> beforeX = snake.getPosX();
        LinkedList<Integer> beforeY = snake.getPosY();

        apple.collide(snake);

        LinkedList<Integer> afterX = snake.getPosX();
        LinkedList<Integer> afterY = snake.getPosY();

        assertEquals(afterX.size(), beforeX.size());
        assertEquals(afterY.size(), beforeY.size());
        assertEquals(afterX.getLast(), beforeX.getLast());
        assertEquals(afterY.getLast(), beforeY.getLast());
    }

    @Test
    public void testSnakeAppleValidIntersection() {
        SnakeHead snake = new SnakeHead(400, 400);
        Apple apple = new Apple(snake.getPx(), snake.getPy(), 400, 400);

        LinkedList<Integer> beforeX = snake.getPosX();
        LinkedList<Integer> beforeY = snake.getPosY();
        int[] lastPos = snake.getLastBodyPos();

        apple.collide(snake);

        LinkedList<Integer> afterX = snake.getPosX();
        LinkedList<Integer> afterY = snake.getPosY();

        assertEquals(afterX.size(), beforeX.size() + 1);
        assertEquals(afterY.size(), beforeY.size() + 1);
        assertEquals(afterX.getLast(), lastPos[0]);
        assertEquals(afterY.getLast(), lastPos[1]);
    }

    @Test
    public void testSnakeGreenAppleInvalidIntersection() {

        /*
         * when we call collide when apple and snake
         * does not actually intersect, nothing happens
         */
        board.reset();

        SnakeHead snake = board.getSnake();
        Apple apple = new GreenApple(400, 400);

        LinkedList<Integer> beforeX = snake.getPosX();
        LinkedList<Integer> beforeY = snake.getPosY();

        apple.collide(snake);

        LinkedList<Integer> afterX = snake.getPosX();
        LinkedList<Integer> afterY = snake.getPosY();

        assertEquals(afterX.size(), beforeX.size());
        assertEquals(afterY.size(), beforeY.size());
        assertEquals(afterX.getLast(), beforeX.getLast());
        assertEquals(afterY.getLast(), beforeY.getLast());
    }

    @Test
    public void testSnakeGreenAppleValidIntersection() {
        SnakeHead snake = new SnakeHead(400, 400);
        Apple apple = new GreenApple(snake.getPx(), snake.getPy(), 400, 400);

        LinkedList<Integer> beforeX = snake.getPosX();
        LinkedList<Integer> beforeY = snake.getPosY();

        apple.collide(snake);

        LinkedList<Integer> afterX = snake.getPosX();
        LinkedList<Integer> afterY = snake.getPosY();

        assertEquals(afterX.size(), beforeX.size() - 1);
        assertEquals(afterY.size(), beforeY.size() - 1);
        assertEquals(afterX.getLast(), beforeX.get(afterX.size() - 1));
        assertEquals(afterY.getLast(), beforeY.get(afterY.size() - 1));
    }

    @Test
    public void testSnakeBlackAppleInvalidIntersection() {

        /*
         * when we call collide when apple and snake
         * does not actually intersect, nothing happens
         */
        board.reset();

        SnakeHead snake = board.getSnake();
        Apple apple = new BlackApple(400, 400);

        LinkedList<Integer> beforeX = snake.getPosX();
        LinkedList<Integer> beforeY = snake.getPosY();

        apple.collide(snake);

        LinkedList<Integer> afterX = snake.getPosX();
        LinkedList<Integer> afterY = snake.getPosY();

        assertEquals(afterX.size(), beforeX.size());
        assertEquals(afterY.size(), beforeY.size());
        assertEquals(afterX.getLast(), beforeX.getLast());
        assertEquals(afterY.getLast(), beforeY.getLast());
    }

    @Test
    public void testSnakeBlackAppleValidIntersection() {
        board.reset();

        SnakeHead snake = board.getSnake();
        Apple apple = new BlackApple(
                snake.getPx() + snake.getVx(),
                snake.getPy() + snake.getVy(),
                400, 400
        );
        snake.move();

        LinkedList<Integer> beforeX = snake.getPosX();
        LinkedList<Integer> beforeY = snake.getPosY();
        int[] lastPos = snake.getLastBodyPos();

        apple.collide(snake);

        LinkedList<Integer> afterX = snake.getPosX();
        LinkedList<Integer> afterY = snake.getPosY();

        assertEquals(afterX.size(), beforeX.size());
        assertEquals(afterY.size(), beforeY.size());
        assertEquals(snake.getPx(), lastPos[0]);
        assertEquals(snake.getPy(), lastPos[1]);
    }

    @Test
    public void testSnakeMove() {
        board.reset();
        SnakeHead snake = board.getSnake();

        int px = snake.getPx();
        int py = snake.getPy();
        int vx = snake.getVx();
        int vy = snake.getVy();

        snake.move();

        assertEquals(snake.getPx(), px + vx);
        assertEquals(snake.getPy(), py + vy);
    }

    @Test
    public void testAppleMove() {
        board.reset();
        Apple apple = board.getApple();

        // normal apple
        int px = apple.getPx();
        int py = apple.getPy();
        int vx = apple.getVx();
        int vy = apple.getVy();

        apple.move();

        assertEquals(apple.getPx(), px + vx);
        assertEquals(apple.getPy(), py + vy);
    }

    @Test
    public void testGreenAppleMove() {
        GreenApple apple = new GreenApple(400, 400);

        int px = apple.getPx();
        int py = apple.getPy();
        int vx = apple.getVx();
        int vy = apple.getVy();

        apple.move();

        assertEquals(apple.getPx(), px + vx);
        assertEquals(apple.getPy(), py + vy);
    }

    @Test
    public void testBlackAppleMove() {
        BlackApple apple = new BlackApple(400, 400);

        int px = apple.getPx();
        int py = apple.getPy();
        int vx = apple.getVx();
        int vy = apple.getVy();

        apple.move();

        assertEquals(apple.getPx(), px + vx);
        assertEquals(apple.getPy(), py + vy);
    }

    @Test
    public void testSnakeEncapsulation() {
        board.reset();
        SnakeHead snake = board.getSnake();

        LinkedList<Integer> beforeX = snake.getPosX();
        LinkedList<Integer> beforeY = snake.getPosY();

        beforeX.removeFirst();
        beforeY.removeLast();

        LinkedList<Integer> afterX = snake.getPosX();
        LinkedList<Integer> afterY = snake.getPosY();

        assertFalse(beforeX.equals(afterX));
        assertFalse(beforeY.equals(afterY));
    }

}
