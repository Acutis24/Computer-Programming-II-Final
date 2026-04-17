import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakePyTranslation {
    static final int SQUARE_SIZE = 25;
    static final int WIDTH = 400;
    static final int HEIGHT = 400;

    static JFrame frame;
    static JPanel panel;
    static JButton skinsButton;
    static JButton backButton;
    static ArrayList<Rectangle> snakeBody = new ArrayList<>();
    static Rectangle snakeHead;
    static Color snakeColor = Color.GREEN;
    static Rectangle fruit;
    static int snakeMoveX = 0;
    static int snakeMoveY = 0;
    static String snakeDirection = "none";
    static int score = 0;
    static Timer gameTimer;
    static Random random = new Random();

    public static void main(String[] args) {
        frame = new JFrame("Snakegame");
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        startMenu();
    }

    static void startMenu() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Background
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
                // Title
                g.setColor(Color.GREEN);
                g.setFont(new Font("Arial", Font.BOLD, 28));
                g.drawString("SNAKE GAME", 110, 160);
                // Instructions
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 16));
                g.drawString("Press SPACE or ENTER to Start", 85, 210);
                // Controls
                g.setFont(new Font("Arial", Font.PLAIN, 14));
                g.drawString("Use W A S D to Move", 130, 250);
            }
        };
        
        skinsButton = new JButton("Skins");
        panel.add(skinsButton);
        skinsButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                frame.remove(panel);
                skinsMenu();
            }
        });

        panel.setFocusable(true);
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    frame.remove(panel);
                    startGame();
                }
            }
        });

        frame.remove(frame);
        frame.add(panel);
        frame.revalidate();
        panel.requestFocusInWindow();
    }

    static void skinsMenu(){
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Background
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
                // Title
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 28));
                g.drawString("SKINS MENU", 110, 160);
            }
        };

        backButton = new JButton("Back");
        panel.add(backButton);
        backButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                frame.remove(panel);
                startMenu();
            }
        });

        JButton blueButton = new JButton("Blue");
        panel.add(blueButton);
        blueButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                snakeColor = Color.BLUE;
            }
        });

        JButton redButton = new JButton("Red");
        panel.add(redButton);
        redButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                snakeColor = Color.RED;
            }
        });

        JButton greenButton = new JButton("Green");
        panel.add(greenButton);
        greenButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                snakeColor = Color.GREEN;
            }
        });

        JButton orangeButton = new JButton("Orange");
        panel.add(orangeButton);
        orangeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                snakeColor = Color.ORANGE;
            }
        });

        JButton yellowButton = new JButton("Yellow");
        panel.add(yellowButton);
        yellowButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                snakeColor = Color.YELLOW;
            }
        });

        frame.add(panel);
        frame.revalidate();
        panel.requestFocusInWindow();
    }

    static void gameOverScreen(String message) {
        if (gameTimer != null) gameTimer.stop();

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Background
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
                // Border
                g.setColor(Color.RED);
                g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
                // Game Over text
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 24));
                g.drawString("GAME OVER", 130, 150);
                // Reason
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 16));
                g.drawString(message, 100, 200);
                // Score
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.PLAIN, 16));
                g.drawString("Final Score: " + score, 150, 310);
                // Prompt
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 14));
                g.drawString("Press R to Restart or Q to Quit", 85, 250);
            }
        };

        panel.setFocusable(true);
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    frame.remove(panel);
                    snakeBody.clear();
                    score = 0;
                    snakeMoveX = 0;
                    snakeMoveY = 0;
                    snakeDirection = "none";
                    startGame();
                } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                    System.exit(0);
                }
            }
        });

        frame.add(panel);
        frame.revalidate();
        panel.repaint();
        panel.requestFocusInWindow();
    }

    static void startGame() {
        ArrayList<Rectangle> bodyRects = new ArrayList<>();
        int[] headPos = {0, 0};
        int[] fruitPos = spawnFruit(bodyRects, headPos);
        score = 0;

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Checkerboard
                for (int row = 0; row < 16; row++) {
                    for (int col = 0; col < 16; col++) {
                        if ((row + col) % 2 == 0) g.setColor(Color.GRAY);
                        else g.setColor(Color.BLACK);
                        g.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                    }
                }
                // Snake body
                g.setColor(snakeColor);
                for (Rectangle r : bodyRects) {
                    g.fillRect(r.x, r.y, SQUARE_SIZE, SQUARE_SIZE);
                }
                // Snake head
                g.setColor(snakeColor);
                g.fillRect(headPos[0], headPos[1], SQUARE_SIZE, SQUARE_SIZE);
                // Fruit
                g.setColor(Color.RED);
                g.fillRect(fruitPos[0], fruitPos[1], SQUARE_SIZE, SQUARE_SIZE);
                // Score
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.PLAIN, 14));
                g.drawString("Score: " + score, 160, 395);
            }
        };

        panel.setFocusable(true);

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W && !snakeDirection.equals("down")) {
                    snakeMoveX = 0; snakeMoveY = -SQUARE_SIZE; snakeDirection = "up";
                } else if (e.getKeyCode() == KeyEvent.VK_A && !snakeDirection.equals("right")) {
                    snakeMoveX = -SQUARE_SIZE; snakeMoveY = 0; snakeDirection = "left";
                } else if (e.getKeyCode() == KeyEvent.VK_D && !snakeDirection.equals("left")) {
                    snakeMoveX = SQUARE_SIZE; snakeMoveY = 0; snakeDirection = "right";
                } else if (e.getKeyCode() == KeyEvent.VK_S && !snakeDirection.equals("up")) {
                    snakeMoveX = 0; snakeMoveY = SQUARE_SIZE; snakeDirection = "down";
                }
            }
        });

        gameTimer = new Timer(150, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (snakeDirection.equals("none")) return;

                // Save old head position as tail
                bodyRects.add(new Rectangle(headPos[0], headPos[1], SQUARE_SIZE, SQUARE_SIZE));

                // Move head
                headPos[0] += snakeMoveX;
                headPos[1] += snakeMoveY;

                // Wall detection
                if (headPos[0] < 0 || headPos[0] >= WIDTH || headPos[1] < 0 || headPos[1] >= HEIGHT) {
                    frame.remove(panel);
                    gameOverScreen("Game Over: Crash");
                    return;
                }

                // Self collision
                for (int i = 0; i < bodyRects.size() - 1; i++) {
                    Rectangle tail = bodyRects.get(i);
                    if (tail.x == headPos[0] && tail.y == headPos[1]) {
                        frame.remove(panel);
                        gameOverScreen("Game Over: Tail Bite");
                        return;
                    }
                }

                // Fruit eating
                if (headPos[0] == fruitPos[0] && headPos[1] == fruitPos[1]) {
                    score++;
                    int[] newFruit = spawnFruit(bodyRects, headPos);
                    fruitPos[0] = newFruit[0];
                    fruitPos[1] = newFruit[1];
                } else {
                    if (!bodyRects.isEmpty()) bodyRects.remove(0);
                }

                panel.repaint();
            }
        });

        frame.add(panel);
        frame.revalidate();
        panel.requestFocusInWindow();
        gameTimer.start();
    }

    static int[] spawnFruit(ArrayList<Rectangle> bodyRects, int[] headPos) {
        int fx, fy;
        while (true) {
            fx = random.nextInt(16) * SQUARE_SIZE;
            fy = random.nextInt(16) * SQUARE_SIZE;
            boolean onSnake = false;
            if (fx == headPos[0] && fy == headPos[1]) onSnake = true;
            for (Rectangle r : bodyRects) {
                if (r.x == fx && r.y == fy) onSnake = true;
            }
            if (!onSnake) break;
        }
        return new int[]{fx, fy};
    }
}