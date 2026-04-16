//Emmanuel A. Gbadebo
//Snake Game Transaltion from python to java

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int boardSize = 400;
    private final int squareSize = 25;
    private final int totalSquares = (boardSize * boardSize) / (squareSize * squareSize);

    private final ArrayList<Point> snake = new ArrayList<>();
    private Point fruit;
    private char direction = 'N'; // N=None, U=Up, D=Down, L=Left, R=Right
    private boolean running = false;
    private boolean gameOver = false;
    private int score = 0;
    private String gameOverMessage = "";
    private Timer timer;

    public SnakeGame() {
        this.setPreferredSize(new Dimension(boardSize, boardSize));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        snake.clear();
        snake.add(new Point(0, 0)); // Initial position
        score = 0;
        direction = 'N';
        running = false;
        gameOver = false;
        spawnFruit();
        
        if (timer != null) timer.stop();
        timer = new Timer(150, this);
        timer.start();
    }

    public void spawnFruit() {
        Random random = new Random();
        while (true) {
            int x = random.nextInt(boardSize / squareSize) * squareSize;
            int y = random.nextInt(boardSize / squareSize) * squareSize;
            Point p = new Point(x, y);
            if (!snake.contains(p)) {
                fruit = p;
                break;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!running && !gameOver) {
            drawMenu(g);
        } else if (running && !gameOver) {
            drawGame(g);
        } else {
            drawGameOver(g);
        }
    }

    private void drawMenu(Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("SansSerif", Font.BOLD, 28));
        g.drawString("SNAKE GAME", 100, 150);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g.drawString("Press ENTER to Start", 115, 200);
        g.drawString("Use W A S D to Move", 120, 230);
    }

    private void drawGame(Graphics g) {
        // Draw Checkerboard
        for (int i = 0; i < boardSize / squareSize; i++) {
            for (int j = 0; j < boardSize / squareSize; j++) {
                g.setColor((i + j) % 2 == 0 ? Color.DARK_GRAY : Color.BLACK);
                g.fillRect(i * squareSize, j * squareSize, squareSize, squareSize);
            }
        }

        // Draw Fruit
        g.setColor(Color.RED);
        g.fillRect(fruit.x, fruit.y, squareSize, squareSize);

        // Draw Snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x, p.y, squareSize, squareSize);
        }

        // Draw Score
        g.setColor(Color.YELLOW);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.drawString("Score: " + score, 170, 20);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 24));
        g.drawString("GAME OVER", 120, 150);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g.drawString(gameOverMessage, 130, 190);
        g.drawString("Press R to Restart or Q to Quit", 90, 230);
        
        g.setColor(Color.YELLOW);
        g.drawString("Final Score: " + score, 150, 280);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !gameOver) {
            move();
            checkFruit();
            checkCollisions();
        }
        repaint();
    }

    public void move() {
        Point head = snake.get(snake.size() - 1);
        int newX = head.x;
        int newY = head.y;

        switch (direction) {
            case 'U' -> newY -= squareSize;
            case 'D' -> newY += squareSize;
            case 'L' -> newX -= squareSize;
            case 'R' -> newX += squareSize;
        }

        if (direction != 'N') {
            snake.add(new Point(newX, newY));
            if (!checkFruitStatus()) {
                snake.remove(0);
            }
        }
    }

    private boolean checkFruitStatus() {
        Point head = snake.get(snake.size() - 1);
        if (head.equals(fruit)) {
            score++;
            spawnFruit();
            return true;
        }
        return false;
    }

    public void checkFruit() { /* Handled in move for logic flow */ }

    public void checkCollisions() {
        Point head = snake.get(snake.size() - 1);

        // Wall collisions (In Java Y increases downwards)
        if (head.x < 0 || head.x >= boardSize || head.y < 0 || head.y >= boardSize) {
            gameOver("Crash!");
        }

        // Tail collisions
        for (int i = 0; i < snake.size() - 1; i++) {
            if (head.equals(snake.get(i))) {
                gameOver("Tail Bite!");
            }
        }
    }

    private void gameOver(String msg) {
        gameOverMessage = msg;
        gameOver = true;
        running = false;
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> { if (direction != 'D') direction = 'U'; }
                case KeyEvent.VK_S -> { if (direction != 'U') direction = 'D'; }
                case KeyEvent.VK_A -> { if (direction != 'R') direction = 'L'; }
                case KeyEvent.VK_D -> { if (direction != 'L') direction = 'R'; }
                case KeyEvent.VK_ENTER -> { if (!running && !gameOver) running = true; }
                case KeyEvent.VK_R -> { if (gameOver) startGame(); }
                case KeyEvent.VK_Q -> { if (gameOver) System.exit(0); }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snakegame");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}